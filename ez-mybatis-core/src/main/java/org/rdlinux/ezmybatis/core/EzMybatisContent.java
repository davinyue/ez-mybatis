package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EzMybatisContent {
    /**
     * 配置映射
     */
    private static final ConcurrentMap<Configuration, ConfigurationConfig> CFG_CONFIG_MAP = new ConcurrentHashMap<>();
    /**
     * 转换器映射
     */
    private static final Map<DbType, Map<Class<?>, Converter<?>>> CONVERT_MAP = new HashMap<>();

    /**
     * 注册转换器
     */
    public static <T extends SqlPart> void addConverter(DbType dbType, Class<T> sqlStruct, Converter<T> converter) {
        CONVERT_MAP.putIfAbsent(dbType, new HashMap<>());
        CONVERT_MAP.get(dbType).put(sqlStruct, converter);
    }

    /**
     * 获取转换器
     */
    @SuppressWarnings("unchecked")
    public static <T extends SqlPart> Converter<T> getConverter(DbType dbType, Class<T> sqlStruct) {
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
    public static <T extends SqlPart> Converter<T> getConverter(Configuration configuration, Class<T> sqlStruct) {
        DbType dbType = DbTypeUtils.getDbType(configuration);
        return getConverter(dbType, sqlStruct);
    }

    /**
     * 设置数据库类型
     */
    public static void setDbType(Configuration configuration, DbType dbType) {
        DbTypeUtils.setDbType(configuration, dbType);
    }

    /**
     * 初始化content
     */
    public static void init(EzMybatisConfig config) {
        ConfigurationConfig configurationConfig = new ConfigurationConfig();
        configurationConfig.setConfiguration(config.getConfiguration());
        configurationConfig.setDbKeywordQMFactory(new DbKeywordQMFactory(config));
        CFG_CONFIG_MAP.put(config.getConfiguration(), configurationConfig);
        initMapper(config);
        initInterceptor(config);
    }

    /**
     * 获取关键词引号
     */
    public static String getKeywordQM(Configuration configuration) {
        Assert.notNull(configuration, "configuration can not be null");
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        Assert.notNull(configurationConfig, "please init");
        return configurationConfig.getDbKeywordQMFactory().getKeywordQM();
    }

    /**
     * 添加插入监听器
     */
    public static void addInsertListener(EzMybatisConfig config, EzMybatisInsertListener listener) {
        checkInit(config);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.getUpdateInterceptor().addInsertListener(listener);
    }

    /**
     * 添加更新监听器
     */
    public static void addUpdateListener(EzMybatisConfig config, EzMybatisUpdateListener listener) {
        checkInit(config);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.getUpdateInterceptor().addUpdateListener(listener);
    }

    /**
     * 添加删除监听器
     */
    public static void addDeleteListener(EzMybatisConfig config, EzMybatisDeleteListener listener) {
        checkInit(config);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
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

    private static void initInterceptor(EzMybatisConfig config) {
        Configuration configuration = config.getConfiguration();
        InterceptorChain interceptorChain = ReflectionUtils.getFieldValue(configuration, "interceptorChain");
        EzMybatisInterceptorChain ezMybatisInterceptorChain = new EzMybatisInterceptorChain(interceptorChain);
        ReflectionUtils.setFieldValue(configuration, "interceptorChain", ezMybatisInterceptorChain);
        ezMybatisInterceptorChain.addEzInterceptor(new EzMybatisResultSetHandlerInterceptor());
        ezMybatisInterceptorChain.addEzInterceptor(new EzMybatisExecutorInterceptor());
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.setUpdateInterceptor(new EzMybatisUpdateInterceptor());
        ezMybatisInterceptorChain.addInterceptor(configurationConfig.getUpdateInterceptor());
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    private static class ConfigurationConfig {
        private Configuration configuration;
        private EzMybatisUpdateInterceptor updateInterceptor;
        private DbKeywordQMFactory dbKeywordQMFactory;
    }
}
