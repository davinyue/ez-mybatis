package org.rdlinux.ezmybatis.core.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.interceptor.listener.DeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.InsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.UpdateListener;

import java.util.LinkedList;
import java.util.List;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class MybatisUpdateInterceptor implements Interceptor {
    private static final Log log = LogFactory.getLog(MybatisUpdateInterceptor.class);
    private final List<InsertListener> insertListeners = new LinkedList<>();
    private final List<UpdateListener> updateListeners = new LinkedList<>();
    private final List<DeleteListener> deleteListeners = new LinkedList<>();

    /**
     * 添加插入监听器
     */
    public void addInsertListener(InsertListener insertListener) {
        this.insertListeners.add(insertListener);
    }

    /**
     * 添加更新监听器
     */
    public void addUpdateListener(UpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    /**
     * 添加删除监听器
     */
    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListeners.add(deleteListener);
    }

    protected void onInsert(Object entity) {
        this.insertListeners.forEach(e -> e.onInsert(entity));
    }

    protected void onBatchInsert(List<Object> entitys) {
        this.insertListeners.forEach(e -> e.onBatchInsert(entitys));
    }

    protected void onUpdate(Object entity) {
        this.updateListeners.forEach(e -> e.onUpdate(entity));
    }

    protected void onBatchUpdate(List<Object> entitys) {
        this.updateListeners.forEach(e -> e.onBatchUpdate(entitys));
    }

    protected void onDelete(Object entity) {
        this.deleteListeners.forEach(e -> e.onDelete(entity));
    }

    protected void onBatchDelete(List<Object> entitys) {
        this.deleteListeners.forEach(e -> e.onBatchDelete(entitys));
    }

    @Override
    @SuppressWarnings(value = {"rawtype", "unchecked"})
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.INSERT) {
            if (mappedStatement.getId().endsWith("." + EzMybatisConstant.INSERT_METHOD_NAME)) {
                log.debug("on insert");
                this.onInsert(args[1]);
            } else if (mappedStatement.getId().endsWith("." + EzMybatisConstant.BATCH_INSERT_METHOD_NAME)) {
                log.debug("on batch insert");
                this.onBatchInsert((List<Object>) args[1]);
            }
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
            if (mappedStatement.getId().endsWith("." + EzMybatisConstant.UPDATE_METHOD_NAME)) {
                log.debug("on update");
                this.onUpdate(args[1]);
            } else if (mappedStatement.getId().endsWith("." + EzMybatisConstant.BATCH_UPDATE_METHOD_NAME)) {
                log.debug("on batch update");
                this.onBatchUpdate((List<Object>) args[1]);
            }
        } else if (sqlCommandType == SqlCommandType.DELETE) {
            if (mappedStatement.getId().endsWith("." + EzMybatisConstant.DELETE_METHOD_NAME)) {
                log.debug("on delete");
                this.onDelete(args[1]);
            } else if (mappedStatement.getId().endsWith("." + EzMybatisConstant.BATCH_DELETE_METHOD_NAME)) {
                log.debug("on batch delete");
                this.onBatchDelete((List<Object>) args[1]);
            } else if (mappedStatement.getId().endsWith(".deleteById")) {
                log.debug("on delete by primary key");
            } else if (mappedStatement.getId().endsWith(".batchDeleteById")) {
                log.debug("on batch delete by primary key");
            }
        }
        return invocation.proceed();
    }
}
