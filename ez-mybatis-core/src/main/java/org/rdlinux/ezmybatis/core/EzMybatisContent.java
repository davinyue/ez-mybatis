package org.rdlinux.ezmybatis.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisExecutorInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisResultSetHandlerInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisStatementHandlerInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisUpdateInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.listener.*;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.*;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EzMybatisContent {
    /**
     * mybatis配置 映射 content配置
     */
    private static final ConcurrentMap<Configuration, EzContentConfig> CFG_CONFIG_MAP = new ConcurrentHashMap<>();
    /**
     * 转换器映射
     */
    private static final Map<DbType, Map<Class<?>, Converter<?>>> CONVERT_MAP = new HashMap<>();
    /**
     * 当前访问filed, 用于查询, 更新, 保存时处理回调以支持用户对参数进行 处理
     */
    private static final ThreadLocal<Deque<EntityField>> CURRENT_ACCESS_FIELD = new ThreadLocal<>();

    public static EntityField getCurrentAccessField() {
        Deque<EntityField> deque = CURRENT_ACCESS_FIELD.get();
        if (deque == null || deque.isEmpty()) {
            return null;
        }
        return CURRENT_ACCESS_FIELD.get().element();
    }

    public static void setCurrentAccessField(EntityField entityField) {
        Deque<EntityField> deque = CURRENT_ACCESS_FIELD.get();
        if (deque == null) {
            deque = new LinkedList<>();
            CURRENT_ACCESS_FIELD.set(deque);
        }
        deque.push(entityField);
    }

    public static void cleanCurrentAccessField() {
        Deque<EntityField> deque = CURRENT_ACCESS_FIELD.get();
        if (deque != null) {
            if (!deque.isEmpty()) {
                deque.poll();
            }
            if (deque.isEmpty()) {
                CURRENT_ACCESS_FIELD.remove();
            }
        }
    }

    /**
     * 注册转换器
     */
    public synchronized static <T extends SqlStruct> void addConverter(DbType dbType, Class<T> sqlStruct,
                                                                       Converter<T> converter) {
        CONVERT_MAP.putIfAbsent(dbType, new HashMap<>());
        CONVERT_MAP.get(dbType).put(sqlStruct, converter);
    }

    /**
     * 获取转换器
     */
    @SuppressWarnings("unchecked")
    public static <T extends SqlStruct> Converter<T> getConverter(DbType dbType, Class<T> sqlStruct) {
        Map<Class<?>, Converter<?>> convertMap = CONVERT_MAP.get(dbType);
        if (convertMap == null) {
            throw new RuntimeException("cannot find the converter of " + dbType.name());
        }
        Converter<T> converter = (Converter<T>) convertMap.get(sqlStruct);
        if (converter == null) {
            throw new RuntimeException(String.format("%s cannot find the converter of %s", dbType.name(),
                    sqlStruct.getSimpleName()));
        }
        return converter;
    }

    /**
     * 获取转换器
     */
    public static <T extends SqlStruct> Converter<T> getConverter(Configuration configuration, Class<T> sqlStruct) {
        DbType dbType = getDbType(configuration);
        return getConverter(dbType, sqlStruct);
    }

    /**
     * 设置数据库类型
     */
    public static void setDbType(Configuration configuration, DbType dbType) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        configurationConfig.setDbType(dbType);
        initConverterRegister(dbType);
    }

    /**
     * 获取数据库类型
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
     * 初始化content
     */
    public static void init(EzMybatisConfig config) {
        EzContentConfig configurationConfig = new EzContentConfig();
        configurationConfig.setDbKeywordQMFactory(new DbKeywordQMFactory(config));
        configurationConfig.setEzMybatisConfig(config);
        CFG_CONFIG_MAP.put(config.getConfiguration(), configurationConfig);
        initMapper(config);
        initInterceptor(config);
        initDbType(config);
    }


    /**
     * 获取关键词引号
     */
    public static String getKeywordQM(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getDbKeywordQMFactory().getKeywordQM();
    }

    /**
     * 获取插入监听器
     */
    public static List<EzMybatisInsertListener> getInsertListeners(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getInsertListeners();
    }

    /**
     * 获取更新监听器
     */
    public static List<EzMybatisUpdateListener> getUpdateListeners(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getUpdateListeners();
    }

    /**
     * 获取查询结构构造结束监听器
     */
    public static List<EzMybatisQueryRetListener> getQueryRetListeners(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getQueryRetListeners();
    }


    /**
     * 添加插入监听器
     */
    public static void addInsertListener(EzMybatisConfig config, EzMybatisInsertListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addInsertListener(listener);
    }

    /**
     * 添加更新监听器
     */
    public static void addUpdateListener(EzMybatisConfig config, EzMybatisUpdateListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addUpdateListener(listener);
    }

    /**
     * 添加查询结构构造结束监听器
     */
    public static void addQueryRetListener(EzMybatisConfig config, EzMybatisQueryRetListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addQueryRetListener(listener);
    }

    /**
     * 添加删除监听器
     */
    public static void addDeleteListener(EzMybatisConfig config, EzMybatisDeleteListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addDeleteListener(listener);
    }

    /**
     * 添加当构建sql获取属性时的监听器
     */
    public static void addOnBuildSqlGetFieldListener(EzMybatisConfig config, EzMybatisOnBuildSqlGetFieldListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.addOnBuildSqlGetFieldListener(listener);
    }

    private static void checkInit(EzMybatisConfig config) {
        if (CFG_CONFIG_MAP.get(config.getConfiguration()) == null) {
            init(config);
        }
    }

    private static void initMapper(EzMybatisConfig config) {
        config.getConfiguration().addMapper(EzMapper.class);
    }

    /**
     * 初始化数据库类型
     */
    private static void initDbType(EzMybatisConfig config) {
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
        DbType dbType = null;
        if (driver.contains("mysql")) {
            dbType = DbType.MYSQL;
        } else if (driver.contains("oracle")) {
            dbType = DbType.ORACLE;
        } else if (driver.toLowerCase().contains("dmdriver")) {
            dbType = DbType.DM;
        } else if (driver.toLowerCase().contains("postgresql")) {
            dbType = DbType.POSTGRE_SQL;
        } else if (driver.toLowerCase().contains("sqlserver")) {
            dbType = DbType.SQL_SERVER;
        }
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.setDbType(dbType);
        initConverterRegister(dbType);
    }

    /**
     * 初始化类型转换器
     */
    private static void initConverterRegister(DbType dbType) {
        if (dbType == null) {
            return;
        }
        if (dbType == DbType.MYSQL) {
            MySqlConverterRegister.register();
        } else if (dbType == DbType.ORACLE) {
            OracleConverterRegister.register();
        } else if (dbType == DbType.DM) {
            DmConverterRegister.register();
        } else if (dbType == DbType.POSTGRE_SQL) {
            PostgreSqlConverterRegister.register();
        } else if (dbType == DbType.SQL_SERVER) {
            SqlServerConverterRegister.register();
        }
    }

    /**
     * 初始化拦截器
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
     * 获取content配置
     */
    public static EzContentConfig getContentConfig(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "configurationConfig non-existent");
        return configurationConfig;
    }

    /**
     * 当调用set方法时
     *
     * @param configuration mybatis配置对象
     * @param isSimple      是否是简单模式, 只有当使用JdbcInsertDao, JdbcUpdateDao下面的所有方法和
     *                      mapper的insert、insertByTable、batchInsert、batchInsertByTable、 update、 batchUpdate、
     *                      updateByTable、 batchUpdateByTable、 replace、 replaceByTable、batchReplace、
     *                      batchReplaceByTable方法时,该值才为true, 否则为false
     * @param ntType        实体对象类型
     * @param field         设置属性
     * @param value         设置值
     * @return 返回新的设置值
     */
    public static Object onBuildSqlGetField(Configuration configuration, boolean isSimple, Class<?> ntType,
                                            Field field, Object value) {
        EzContentConfig contentConfig = getContentConfig(configuration);
        List<EzMybatisOnBuildSqlGetFieldListener> listeners = contentConfig.getOnBuildSqlGetFieldListeners();
        if (listeners != null) {
            for (EzMybatisOnBuildSqlGetFieldListener listener : listeners) {
                value = listener.onGet(isSimple, ntType, field, value);
            }
        }
        return value;
    }

    /**
     * 当查询结果单条构造结束时
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
     * 当查询结果全部构造结束时
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
}
