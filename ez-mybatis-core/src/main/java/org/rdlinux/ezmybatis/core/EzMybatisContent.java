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
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisUpdateInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.dm.DmConverterRegister;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlConverterRegister;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleConverterRegister;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
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
     * 注册转换器
     */
    public static <T extends SqlStruct> void addConverter(DbType dbType, Class<T> sqlStruct, Converter<T> converter) {
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
        configurationConfig.setConfiguration(config.getConfiguration());
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
     * 添加插入监听器
     */
    public static void addInsertListener(EzMybatisConfig config, EzMybatisInsertListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.getUpdateInterceptor().addInsertListener(listener);
    }

    /**
     * 添加更新监听器
     */
    public static void addUpdateListener(EzMybatisConfig config, EzMybatisUpdateListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.getUpdateInterceptor().addUpdateListener(listener);
    }

    /**
     * 添加删除监听器
     */
    public static void addDeleteListener(EzMybatisConfig config, EzMybatisDeleteListener listener) {
        checkInit(config);
        EzContentConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.getUpdateInterceptor().addDeleteListener(listener);
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
}
