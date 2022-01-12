package org.rdlinux.ezmybatis.core.interceptor;

public class InterceptorLogicResult {
    private boolean goOn;
    private Object result;

    public InterceptorLogicResult(boolean goOn, Object result) {
        this.goOn = goOn;
        this.result = result;
    }

    public boolean isGoOn() {
        return this.goOn;
    }

    public Object getResult() {
        return this.result;
    }
}
