package org.rdlinux.ezmybatis.spring.boot;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.rdlinux.ezmybatis.core.interceptor.ExecutorInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.UpdateInterceptor;
import org.rdlinux.ezmybatis.spring.EzMybatisConfigurationCustom;

public class EzConfigurationCustomizer implements ConfigurationCustomizer {
    private ResultSetHandlerInterceptor resultSetHandlerInterceptor;
    private ExecutorInterceptor executorInterceptor;
    private UpdateInterceptor updateInterceptor;

    public EzConfigurationCustomizer(ResultSetHandlerInterceptor resultSetHandlerInterceptor,
                                     ExecutorInterceptor executorInterceptor,
                                     UpdateInterceptor updateInterceptor) {
        this.resultSetHandlerInterceptor = resultSetHandlerInterceptor;
        this.executorInterceptor = executorInterceptor;
        this.updateInterceptor = updateInterceptor;
    }

    @Override
    public void customize(Configuration configuration) {
        new EzMybatisConfigurationCustom(configuration, this.resultSetHandlerInterceptor, this.executorInterceptor,
                this.updateInterceptor);
    }
}
