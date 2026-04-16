package org.rdlinux.ezmybatis.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisExecutorInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisResultSetHandlerInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisStatementHandlerInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisUpdateInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.listener.*;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProviderLoader;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Ez-MyBatis 运行时上下文中心。
 *
 * <p>该类负责维护 {@link Configuration} 与 {@link EzContentConfig} 的关联关系，并统一承载：</p>
 * <p>1. 方言与 {@link Converter} 注册。</p>
 * <p>2. Mapper 与拦截器初始化。</p>
 * <p>3. 各类监听器的注册与触发。</p>
 * <p>4. 与实体元数据缓存协作的上下文销毁。</p>
 *
 * <p>该类以静态方式工作，调用方通常在框架启动阶段通过 {@link #init(EzMybatisConfig)}
 * 完成初始化，后续再按 {@link Configuration} 获取运行时能力。</p>
 */
public class EzMybatisContent {
    /**
     * MyBatis {@link Configuration} 与 Ez-MyBatis 上下文配置的映射表。
     */
    private static final ConcurrentMap<Configuration, EzContentConfig> CFG_CONFIG_MAP = new ConcurrentHashMap<>();


    /**
     * 为指定数据库类型注册 SQL 结构转换器。
     *
     * @param dbType    数据库类型
     * @param sqlStruct 结构节点类型
     * @param converter 对应的 SQL 转换器实现
     */
    public static <T extends SqlStruct> void addConverter(DbType dbType, Class<T> sqlStruct,
                                                          Converter<T> converter) {
        DbDialectProvider provider = DbDialectProviderLoader.getProvider(dbType);
        provider.addConverter(sqlStruct, converter);
    }

    /**
     * 获取指定数据库类型下某个结构节点的转换器。
     *
     * @param dbType    数据库类型
     * @param sqlStruct 结构节点类型
     * @return 对应的 SQL 转换器
     */
    public static <T extends SqlStruct> Converter<T> getConverter(DbType dbType, Class<T> sqlStruct) {
        DbDialectProvider provider = DbDialectProviderLoader.getProvider(dbType);
        return provider.getConverter(sqlStruct);
    }

    /**
     * 按 {@link Configuration} 获取当前方言下某个结构节点的转换器。
     *
     * @param configuration MyBatis 配置对象
     * @param sqlStruct     结构节点类型
     * @return 当前配置对应方言下的 SQL 转换器
     */
    public static <T extends SqlStruct> Converter<T> getConverter(Configuration configuration, Class<T> sqlStruct) {
        return getDbDialectProvider(configuration).getConverter(sqlStruct);
    }

    /**
     * 为指定 {@link Configuration} 显式设置数据库类型。
     *
     * <p>该方法会同步刷新当前配置关联的 {@link DbDialectProvider}，并重新触发方言转换器注册。</p>
     *
     * @param configuration MyBatis 配置对象
     * @param dbType        数据库类型
     */
    public static void setDbType(Configuration configuration, DbType dbType) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        configurationConfig.setDbType(dbType);
        if (configurationConfig.getDbDialectProvider() == null
                || configurationConfig.getDbDialectProvider().getDbType() != dbType) {
            DbDialectProvider provider = DbDialectProviderLoader.getProvider(dbType);
            configurationConfig.setDbDialectProvider(provider);
        }
        initConverterRegister(configuration);
    }

    /**
     * 获取指定 {@link Configuration} 当前绑定的数据库类型。
     *
     * @param configuration MyBatis 配置对象
     * @return 当前配置对应的数据库类型
     */
    public static DbType getDbType(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        DbType dbType = configurationConfig.getDbType();
        if (dbType == null) {
            throw new RuntimeException("The database type is not recognized. Please configure it manually");
        }
        return dbType;
    }

    /**
     * 初始化指定配置的 Ez-MyBatis 运行时上下文。
     *
     * <p>初始化过程会创建并缓存 {@link EzContentConfig}，然后依次初始化基础 Mapper、拦截器与数据库类型。</p>
     *
     * @param config Ez-MyBatis 配置对象
     */
    public static void init(EzMybatisConfig config) {
        EzContentConfig configurationConfig = new EzContentConfig();
        configurationConfig.setEzMybatisConfig(config);
        CFG_CONFIG_MAP.put(config.getConfiguration(), configurationConfig);
        initMapper(config);
        initInterceptor(config);
        initDbType(config);
    }

    /**
     * 获取当前配置下数据库关键字转义所使用的引号字符。
     *
     * <p>当未启用关键字转义时返回空字符串；启用后由当前方言提供实际字符，
     * 例如 MySQL 返回 <code>`</code>，Oracle 返回 <code>"</code>。</p>
     *
     * @param configuration MyBatis 配置对象
     * @return 关键字引号字符，或空字符串
     */
    public static String getKeywordQuoteMark(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig ezContentConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(ezContentConfig, "please init");
        if (!ezContentConfig.getEzMybatisConfig().isEscapeKeyword()) {
            return "";
        }
        return getDbDialectProvider(configuration).getKeywordQuoteMark();
    }

    /**
     * 获取当前配置下已注册的插入监听器列表。
     *
     * @param configuration MyBatis 配置对象
     * @return 插入监听器列表
     */
    public static List<EzMybatisInsertListener> getInsertListeners(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getInsertListeners();
    }

    /**
     * 获取当前配置下已注册的更新监听器列表。
     *
     * @param configuration MyBatis 配置对象
     * @return 更新监听器列表
     */
    public static List<EzMybatisUpdateListener> getUpdateListeners(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getUpdateListeners();
    }

    /**
     * 获取当前配置下已注册的查询结果构造完成监听器列表。
     *
     * @param configuration MyBatis 配置对象
     * @return 查询结果监听器列表
     */
    public static List<EzMybatisQueryRetListener> getQueryRetListeners(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getQueryRetListeners();
    }

    /**
     * 为指定配置添加插入监听器。
     *
     * @param config   Ez-MyBatis 配置对象
     * @param listener 插入监听器
     */
    public static void addInsertListener(EzMybatisConfig config, EzMybatisInsertListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addInsertListener(listener);
    }

    /**
     * 为指定配置添加更新监听器。
     *
     * @param config   Ez-MyBatis 配置对象
     * @param listener 更新监听器
     */
    public static void addUpdateListener(EzMybatisConfig config, EzMybatisUpdateListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addUpdateListener(listener);
    }

    /**
     * 为指定配置添加查询结果构造完成监听器。
     *
     * @param config   Ez-MyBatis 配置对象
     * @param listener 查询结果监听器
     */
    public static void addQueryRetListener(EzMybatisConfig config, EzMybatisQueryRetListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addQueryRetListener(listener);
    }

    /**
     * 为指定配置添加删除监听器。
     *
     * @param config   Ez-MyBatis 配置对象
     * @param listener 删除监听器
     */
    public static void addDeleteListener(EzMybatisConfig config, EzMybatisDeleteListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addDeleteListener(listener);
    }

    /**
     * 为指定配置添加 SQL 构建阶段字段取值监听器。
     *
     * @param config   Ez-MyBatis 配置对象
     * @param listener 字段取值监听器
     */
    public static void addOnBuildSqlGetFieldListener(EzMybatisConfig config,
                                                     EzMybatisOnBuildSqlGetFieldListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addOnBuildSqlGetFieldListener(listener);
    }

    /**
     * 检查当前配置是否已初始化，未初始化时自动完成初始化。
     *
     * @param config Ez-MyBatis 配置对象
     */
    private static void checkInit(EzMybatisConfig config) {
        if (CFG_CONFIG_MAP.get(config.getConfiguration()) == null) {
            init(config);
        }
    }

    /**
     * 向当前 {@link Configuration} 注册内置通用 Mapper。
     *
     * @param config Ez-MyBatis 配置对象
     */
    private static void initMapper(EzMybatisConfig config) {
        config.getConfiguration().addMapper(EzMapper.class);
    }

    /**
     * 尝试根据数据源驱动信息自动识别数据库类型并完成方言绑定。
     *
     * @param config Ez-MyBatis 配置对象
     */
    private static void initDbType(EzMybatisConfig config) {
        DbType dbType = config.getDbType();
        if (dbType == null) {
            Configuration configuration = config.getConfiguration();
            Environment environment = configuration.getEnvironment();
            if (environment == null) {
                return;
            }
            DataSource dataSource = environment.getDataSource();
            if (dataSource == null) {
                return;
            }
            String driver;
            if (PooledDataSource.class.isAssignableFrom(dataSource.getClass())) {
                driver = ((PooledDataSource) dataSource).getDriver();
            } else {
                if (dataSource.getClass().getName().contains("druid")) {
                    driver = ReflectionUtils.getFieldValue(dataSource, "driverClass");
                } else {
                    driver = ReflectionUtils.getFieldValue(dataSource, "driverClassName");
                }
            }
            if (StringUtils.isBlank(driver)) {
                return;
            }
            dbType = DbDialectProviderLoader.matchDbType(driver);
            config.setDbType(dbType);
        }
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.setDbType(dbType);
        configurationConfig.setDbDialectProvider(DbDialectProviderLoader.getProvider(dbType));
        initConverterRegister(config.getConfiguration());
    }

    /**
     * 初始化当前配置所属方言的结构转换器注册。
     *
     * @param configuration MyBatis 配置对象
     */
    private static void initConverterRegister(Configuration configuration) {
        DbDialectProvider provider = getDbDialectProvider(configuration);
        if (provider == null) {
            return;
        }
        provider.registerConverters();
    }

    /**
     * 初始化当前配置需要接入的 Ez-MyBatis 拦截器链。
     *
     * @param config Ez-MyBatis 配置对象
     */
    private static void initInterceptor(EzMybatisConfig config) {
        Configuration configuration = config.getConfiguration();
        InterceptorChain interceptorChain = ReflectionUtils.getFieldValue(configuration, "interceptorChain");
        EzMybatisInterceptorChain ezMybatisInterceptorChain = new EzMybatisInterceptorChain(interceptorChain);
        ReflectionUtils.setFieldValue(configuration, "interceptorChain", ezMybatisInterceptorChain);
        ezMybatisInterceptorChain.addEzInterceptor(new EzMybatisStatementHandlerInterceptor());
        ezMybatisInterceptorChain.addEzInterceptor(new EzMybatisResultSetHandlerInterceptor());
        ezMybatisInterceptorChain.addEzInterceptor(new EzMybatisExecutorInterceptor());
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.setUpdateInterceptor(new EzMybatisUpdateInterceptor());
        ezMybatisInterceptorChain.addInterceptor(configurationConfig.getUpdateInterceptor());
    }

    /**
     * 获取指定 {@link Configuration} 关联的上下文配置。
     *
     * @param configuration MyBatis 配置对象
     * @return 对应的 Ez-MyBatis 上下文配置
     */
    public static EzContentConfig getContentConfig(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "configurationConfig non-existent");
        return configurationConfig;
    }

    /**
     * 在 SQL 构建过程中读取实体字段值时触发监听器链。
     *
     * @param configuration mybatis配置对象
     * @param accessScope   实体属性访问域
     * @param ntType        实体对象类型
     * @param field         设置属性
     * @param value         设置值
     * @return 返回新的设置值
     */
    public static Object onBuildSqlGetField(Configuration configuration, FieldAccessScope accessScope, Class<?> ntType,
                                            Field field, Object value) {
        EzContentConfig contentConfig = getContentConfig(configuration);
        List<EzMybatisOnBuildSqlGetFieldListener> listeners = contentConfig.getOnBuildSqlGetFieldListeners();
        if (listeners != null) {
            for (EzMybatisOnBuildSqlGetFieldListener listener : listeners) {
                value = listener.onGet(accessScope, ntType, field, value);
            }
        }
        return value;
    }

    /**
     * 在单条查询结果对象构造完成后触发监听器链。
     *
     * @param configuration mybatis配置对象
     * @param model         值
     * @return 返回新的设置值
     */
    public static Object onRetBuildDone(Configuration configuration, Object model) {
        EzContentConfig contentConfig = getContentConfig(configuration);
        List<EzMybatisQueryRetListener> listeners = contentConfig.getQueryRetListeners();
        if (listeners != null) {
            for (EzMybatisQueryRetListener listener : listeners) {
                model = listener.onBuildDone(model);
            }
        }
        return model;
    }

    /**
     * 在整批查询结果构造完成后触发监听器链。
     *
     * @param configuration mybatis配置对象
     * @param models        结果集
     */
    public static void onBatchRetBuildDone(Configuration configuration, List<Object> models) {
        EzContentConfig contentConfig = getContentConfig(configuration);
        List<EzMybatisQueryRetListener> listeners = contentConfig.getQueryRetListeners();
        if (listeners != null) {
            for (EzMybatisQueryRetListener listener : listeners) {
                listener.onBatchBuildDone(models);
            }
        }
    }

    /**
     * 按 {@link Configuration} 获取数据库方言提供者。
     *
     * @param configuration mybatis配置对象
     * @return 数据库方言提供者
     */
    public static DbDialectProvider getDbDialectProvider(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        DbDialectProvider provider = configurationConfig.getDbDialectProvider();
        if (provider != null) {
            return provider;
        }
        DbType dbType = getDbType(configuration);
        provider = DbDialectProviderLoader.getProvider(dbType);
        configurationConfig.setDbDialectProvider(provider);
        return provider;
    }

    /**
     * 按数据库类型获取方言提供者。
     *
     * @param dbType 数据库类型
     * @return 数据库方言提供者
     */
    public static DbDialectProvider getDbDialectProvider(DbType dbType) {
        return DbDialectProviderLoader.getProvider(dbType);
    }

    /**
     * 为指定数据库类型设置全局方言提供者。
     *
     * @param dbType   数据库类型
     * @param provider 方言提供者
     */
    public static void setProvider(DbType dbType, DbDialectProvider provider) {
        DbDialectProviderLoader.setProvider(dbType, provider);
    }

    /**
     * 为指定 {@link Configuration} 设置方言提供者。
     *
     * <p>该方法会同步刷新当前配置的数据库类型，并重新注册该方言下的结构转换器。</p>
     *
     * @param configuration mybatis配置
     * @param provider      方言提供者
     */
    public static void setProvider(Configuration configuration, DbDialectProvider provider) {
        Assert.notNull(configuration, "configuration can not be null");
        Assert.notNull(provider, "provider can not be null");
        EzContentConfig configurationConfig = getContentConfig(configuration);
        configurationConfig.setDbType(provider.getDbType());
        configurationConfig.setDbDialectProvider(provider);
        initConverterRegister(configuration);
    }

    /**
     * 销毁指定 {@link Configuration} 关联的上下文缓存与实体元数据缓存。
     *
     * @param configuration MyBatis 配置对象
     */
    public static void destroy(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        CFG_CONFIG_MAP.remove(configuration);
        EzEntityClassInfoFactory.clear(configuration);
    }

    /**
     * 清理全部运行时上下文缓存与实体元数据缓存。
     */
    public static void destroyAll() {
        CFG_CONFIG_MAP.clear();
        EzEntityClassInfoFactory.clear();
    }
}
