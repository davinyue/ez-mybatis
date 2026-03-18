package org.rdlinux.ezmybatis.core.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.rdlinux.ezmybatis.core.interceptor.executor.TypeHandlerInitLogic;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "parameterize", args = {Statement.class})
})
public class EzMybatisStatementHandlerInterceptor extends AbstractInterceptor {
    protected List<InterceptorLogic> logics = new LinkedList<>();

    public EzMybatisStatementHandlerInterceptor() {
        this.logics.add(new TypeHandlerInitLogic());
    }

    @Override
    public List<InterceptorLogic> getLogics() {
        return this.logics;
    }
}
