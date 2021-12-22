package ink.dvc.ezmybatis.spring;

import ink.dvc.ezmybatis.core.interceptor.ExecutorInterceptor;
import ink.dvc.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import ink.dvc.ezmybatis.core.interceptor.UpdateInterceptor;
import ink.dvc.ezmybatis.core.mapper.EzMapper;
import ink.dvc.ezmybatis.core.utils.Assert;
import org.apache.ibatis.session.Configuration;

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
