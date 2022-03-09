package org.rdlinux.ezmybatis.core;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.util.List;

public class EzMybatisInterceptorChain extends InterceptorChain {
    public EzMybatisInterceptorChain(InterceptorChain interceptorChain) {
        List<Interceptor> interceptors = interceptorChain.getInterceptors();
        interceptors.forEach(this::addInterceptor);
    }

    @Override
    public void addInterceptor(Interceptor interceptor) {
        List<Interceptor> interceptors = ReflectionUtils.getFieldValue(this, "interceptors");
        interceptors.add(0, interceptor);
    }

    public void addEzInterceptor(Interceptor interceptor) {
        super.addInterceptor(interceptor);
    }
}
