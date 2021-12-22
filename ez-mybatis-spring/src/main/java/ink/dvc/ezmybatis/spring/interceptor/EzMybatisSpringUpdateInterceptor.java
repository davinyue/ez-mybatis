package ink.dvc.ezmybatis.spring.interceptor;

import ink.dvc.ezmybatis.core.interceptor.UpdateInterceptor;
import ink.dvc.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import ink.dvc.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import ink.dvc.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Comparator;
import java.util.Map;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class EzMybatisSpringUpdateInterceptor extends UpdateInterceptor implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, EzMybatisInsertListener> insertListenerMap = applicationContext.getBeansOfType(
                EzMybatisInsertListener.class);
        insertListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisInsertListener::order))
                .forEach(this::addInsertListener);
        Map<String, EzMybatisUpdateListener> updateListenerMap = applicationContext.getBeansOfType(
                EzMybatisUpdateListener.class);
        updateListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisUpdateListener::order))
                .forEach(this::addUpdateListener);
        Map<String, EzMybatisDeleteListener> deleteListenerMap = applicationContext.getBeansOfType(
                EzMybatisDeleteListener.class);
        deleteListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisDeleteListener::order))
                .forEach(this::addDeleteListener);
    }
}
