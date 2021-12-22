package ink.dvc.ezmybatis.spring.boot;

import ink.dvc.ezmybatis.core.interceptor.ExecutorInterceptor;
import ink.dvc.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import ink.dvc.ezmybatis.core.interceptor.UpdateInterceptor;
import ink.dvc.ezmybatis.core.mapper.EzMapper;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

public class EzConfigurationCustomizer implements ConfigurationCustomizer {
    private ResultSetHandlerInterceptor resultSetHandlerInterceptor;
    private ExecutorInterceptor executorInterceptor;
    private UpdateInterceptor updateInterceptor;

    public EzConfigurationCustomizer(ResultSetHandlerInterceptor resultSetHandlerInterceptor,
                                     ExecutorInterceptor executorInterceptor,
                                     UpdateInterceptor updateInterceptor) {
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
    }

    @Override
    public void customize(Configuration configuration) {
        configuration.addInterceptor(this.resultSetHandlerInterceptor);
        configuration.addInterceptor(this.executorInterceptor);
        configuration.addInterceptor(this.updateInterceptor);
        if (!configuration.hasMapper(EzMapper.class)) {
            configuration.addMapper(EzMapper.class);
        }
    }
}
