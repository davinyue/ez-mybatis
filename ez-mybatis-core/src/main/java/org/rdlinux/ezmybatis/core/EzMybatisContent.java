package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.interceptor.ExecutorInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.UpdateInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EzMybatisContent {
    private static final ConcurrentMap<Configuration, ConfigurationConfig> CFG_CONFIG_MAP = new ConcurrentHashMap<>();

    public static void setDbType(Configuration configuration, DbType dbType) {
        DbTypeUtils.setDbType(configuration, dbType);
    }

    public static void init(Configuration configuration) {
        initMapper(configuration);
        initInterceptor(configuration);
    }

    public static void addInsertListener(Configuration configuration, EzMybatisInsertListener listener) {
        checkInit(configuration);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        configurationConfig.getUpdateInterceptor().addInsertListener(listener);
    }

    public static void addUpdateListener(Configuration configuration, EzMybatisUpdateListener listener) {
        checkInit(configuration);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        configurationConfig.getUpdateInterceptor().addUpdateListener(listener);
    }

    public static void addDeleteListener(Configuration configuration, EzMybatisDeleteListener listener) {
        checkInit(configuration);
        ConfigurationConfig configurationConfig = CFG_CONFIG_MAP.get(configuration);
        configurationConfig.getUpdateInterceptor().addDeleteListener(listener);
    }

    private static void checkInit(Configuration configuration) {
        if (CFG_CONFIG_MAP.get(configuration) == null) {
            init(configuration);
        }
    }

    private static void initMapper(Configuration configuration) {
        configuration.addMapper(EzMapper.class);
    }

    private static void initInterceptor(Configuration configuration) {
        configuration.addInterceptor(new ResultSetHandlerInterceptor());
        configuration.addInterceptor(new ExecutorInterceptor());
        ConfigurationConfig configurationConfig = new ConfigurationConfig()
                .setConfiguration(configuration).setUpdateInterceptor(new UpdateInterceptor());
        CFG_CONFIG_MAP.put(configuration, configurationConfig);
        configuration.addInterceptor(configurationConfig.getUpdateInterceptor());
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    private static class ConfigurationConfig {
        private Configuration configuration;
        private UpdateInterceptor updateInterceptor;
    }
}
