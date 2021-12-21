package ink.dvc.ezmybatis.spring.boot;

import ink.dvc.ezmybatis.core.interceptor.ExecutorInterceptor;
import ink.dvc.ezmybatis.core.interceptor.ResultSetHandlerInterceptor;
import ink.dvc.ezmybatis.core.mapper.EzMapper;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

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
