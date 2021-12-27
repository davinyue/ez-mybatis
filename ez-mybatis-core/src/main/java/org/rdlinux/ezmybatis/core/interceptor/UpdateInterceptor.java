package org.rdlinux.ezmybatis.core.interceptor;

import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.mapper.EzBaseMapper;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class UpdateInterceptor implements Interceptor {
    private static final Log log = LogFactory.getLog(UpdateInterceptor.class);
    private static final Field MAPPER_METHOD_FIELD = ReflectionUtils.getField(ProviderSqlSource.class,
            "mapperMethod");
    private final List<EzMybatisInsertListener> insertListeners = new LinkedList<>();
    private final List<EzMybatisUpdateListener> updateListeners = new LinkedList<>();
    private final List<EzMybatisDeleteListener> deleteListeners = new LinkedList<>();

    /**
     * 添加插入监听器
     */
    public void addInsertListener(EzMybatisInsertListener insertListener) {
        this.insertListeners.add(insertListener);
    }

    /**
     * 添加更新监听器
     */
    public void addUpdateListener(EzMybatisUpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    /**
     * 添加删除监听器
     */
    public void addDeleteListener(EzMybatisDeleteListener deleteListener) {
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

    protected void onDeleteById(Object id, Class<?> ntClass) {
        this.deleteListeners.forEach(e -> e.onDeleteById(id, ntClass));
    }

    protected void onBatchDeleteById(List<Object> ids, Class<?> ntClass) {
        this.deleteListeners.forEach(e -> e.onBatchDeleteById(ids, ntClass));
    }

    @Override
    @SuppressWarnings(value = {"rawtype", "unchecked"})
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        SqlSource sqlSource = mappedStatement.getSqlSource();
        if (!(sqlSource instanceof ProviderSqlSource)) {
            return invocation.proceed();
        }
        Method mapperMethod = ReflectionUtils.getFieldValue(sqlSource, MAPPER_METHOD_FIELD);
        if (!mapperMethod.getDeclaringClass().getName().equals(EzBaseMapper.class.getName())) {
            return invocation.proceed();
        }
        String statementId = mappedStatement.getId();
        if (sqlCommandType == SqlCommandType.INSERT) {
            Map<String, Object> param = (Map<String, Object>) args[1];
            if (statementId.endsWith("." + EzMybatisConstant.INSERT_METHOD_NAME)) {
                log.debug("on insert");
                this.onInsert(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_INSERT_METHOD_NAME)) {
                log.debug("on batch insert");
                this.onBatchInsert((List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            }
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
            Map<String, Object> param = (Map<String, Object>) args[1];
            if (statementId.endsWith("." + EzMybatisConstant.UPDATE_METHOD_NAME) ||
                    statementId.endsWith("." + EzMybatisConstant.REPLACE_METHOD_NAME)) {
                log.debug("on update");
                this.onUpdate(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_UPDATE_METHOD_NAME) ||
                    statementId.endsWith("." + EzMybatisConstant.BATCH_REPLACE_METHOD_NAME)) {
                log.debug("on batch update");
                this.onBatchUpdate((List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            }
        } else if (sqlCommandType == SqlCommandType.DELETE) {
            Map<String, Object> param = (Map<String, Object>) args[1];
            if (statementId.endsWith("." + EzMybatisConstant.DELETE_METHOD_NAME)) {
                log.debug("on delete");
                this.onDelete(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_DELETE_METHOD_NAME)) {
                log.debug("on batch delete");
                this.onBatchDelete((List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            } else {
                String className = statementId.substring(0, statementId.lastIndexOf("."));
                Class<?> mapperClass = Class.forName(className);
                Class<?> etType = ReflectionUtils.getGenericSuperinterface(mapperClass,
                        0, 0);
                if (statementId.endsWith("." + EzMybatisConstant.DELETE_BY_ID_METHOD_NAME)) {
                    log.debug("on delete by primary key");
                    this.onDeleteById(param.get(EzMybatisConstant.MAPPER_PARAM_ID), etType);
                } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_DELETE_BY_ID_METHOD_NAME)) {
                    log.debug("on batch delete by primary key");
                    this.onBatchDeleteById((List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_IDS), etType);
                }
            }
        }
        return invocation.proceed();
    }
}