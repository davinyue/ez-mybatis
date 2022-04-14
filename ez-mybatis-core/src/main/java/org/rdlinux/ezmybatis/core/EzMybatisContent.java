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
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EzMybatisContent {
    private static final ConcurrentMap<Configuration, ConfigurationConfig> CFG_CONFIG_MAP = new ConcurrentHashMap<>();

    public static void setDbType(Configuration configuration, DbType dbType) {
        DbTypeUtils.setDbType(configuration, dbType);
    }

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
        return configurationConfig.getDbKeywordQMFactory().getKeywordQM(DbTypeUtils.getDbType(configuration));
    }

    public static void addInsertListener(EzMybatisConfig config, EzMybatisInsertListener listener) {
        checkInit(config);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.getUpdateInterceptor().addInsertListener(listener);
    }

    public static void addUpdateListener(EzMybatisConfig config, EzMybatisUpdateListener listener) {
        checkInit(config);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(config.getConfiguration());
        configurationConfig.getUpdateInterceptor().addUpdateListener(listener);
    }

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
