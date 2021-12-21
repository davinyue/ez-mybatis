package ink.dvc.ezmybatis.core.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.List;

public abstract class AbstractInterceptor implements Interceptor {

    public abstract List<InterceptorLogic> getLogics();


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<InterceptorLogic> logics = this.getLogics();
        for (InterceptorLogic logic : logics) {
            InterceptorLogicResult interceptorLogicResult = logic.invokeBefore(invocation);
            if (!interceptorLogicResult.isGoOn()) {
                return interceptorLogicResult.getResult();
            }
        }
        return invocation.proceed();
    }
}
