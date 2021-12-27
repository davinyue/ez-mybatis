package org.rdlinux.ezmybatis.spring;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.interceptor.ExecutorInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.UpdateInterceptor;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.utils.Assert;

public class EzMybatisConfigurationCustom {
    private Configuration configuration;
    private ResultSetHandlerInterceptor resultSetHandlerInterceptor;
    private ExecutorInterceptor executorInterceptor;
    private UpdateInterceptor updateInterceptor;

    public EzMybatisConfigurationCustom(Configuration configuration,
                                        ResultSetHandlerInterceptor resultSetHandlerInterceptor,
                                        ExecutorInterceptor executorInterceptor,
                                        UpdateInterceptor updateInterceptor) {
        Assert.notNull(configuration, "configuration can not be null");
        this.configuration = configuration;
        if (resultSetHandlerInterceptor == null) {
            resultSetHandlerInterceptor = new ResultSetHandlerInterceptor();
        }
        if (executorInterceptor == null) {
            executorInterceptor = new ExecutorInterceptor();
        }
        if (updateInterceptor == null) {
            updateInterceptor = new UpdateInterceptor();
        }
        this.resultSetHandlerInterceptor = resultSetHandlerInterceptor;
        this.executorInterceptor = executorInterceptor;
        this.updateInterceptor = updateInterceptor;
        this.init();
    }

    protected void init() {
        this.configuration.addInterceptor(this.resultSetHandlerInterceptor);
        this.configuration.addInterceptor(this.executorInterceptor);
        this.configuration.addInterceptor(this.updateInterceptor);
        if (!this.configuration.hasMapper(EzMapper.class)) {
            this.configuration.addMapper(EzMapper.class);
        }
    }
}
