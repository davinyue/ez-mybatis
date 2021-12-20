package org.rdlinux.ezmybatis.spring.boot;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.rdlinux.ezmybatis.core.interceptor.ExecutorInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;

public class EzConfigurationCustomizer implements ConfigurationCustomizer {
    @Override
    public void customize(Configuration configuration) {
        configuration.addInterceptor(new ResultSetHandlerInterceptor());
        configuration.addInterceptor(new ExecutorInterceptor());
        if (!configuration.hasMapper(EzMapper.class)) {
            configuration.addMapper(EzMapper.class);
        }
    }
}
