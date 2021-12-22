package ink.dvc.ezmybatis.spring.boot;

import ink.dvc.ezmybatis.core.interceptor.ExecutorInterceptor;
import ink.dvc.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import ink.dvc.ezmybatis.core.interceptor.UpdateInterceptor;
import ink.dvc.ezmybatis.spring.EzMybatisConfigurationCustom;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

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
