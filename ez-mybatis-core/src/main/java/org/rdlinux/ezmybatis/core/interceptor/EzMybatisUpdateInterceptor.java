package org.rdlinux.ezmybatis.core.interceptor;

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
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.mapper.EzBaseMapper;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
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
public class EzMybatisUpdateInterceptor implements Interceptor {
    private static final Log log = LogFactory.getLog(EzMybatisUpdateInterceptor.class);
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

    protected void onInsert(Object model) {
        this.insertListeners.forEach(e -> e.onInsert(model));
    }

    protected void onBatchInsert(Collection<Object> models) {
        this.insertListeners.forEach(e -> e.onBatchInsert(models));
    }

    protected void onUpdate(Object model) {
        this.updateListeners.forEach(e -> e.onUpdate(model));
    }

    protected void onBatchUpdate(Collection<Object> models) {
        this.updateListeners.forEach(e -> e.onBatchUpdate(models));
    }

    protected void onReplace(Object entity) {
        this.updateListeners.forEach(e -> e.onReplace(entity));
    }

    protected void onBatchReplace(Collection<Object> models) {
        this.updateListeners.forEach(e -> e.onBatchReplace(models));
    }

    protected void onEzUpdate(EzUpdate ezUpdate) {
        this.updateListeners.forEach(e -> e.onEzUpdate(ezUpdate));
    }

    protected void onEzBatchUpdate(Collection<EzUpdate> ezUpdates) {
        this.updateListeners.forEach(e -> e.onEzBatchUpdate(ezUpdates));
    }

    protected void onEzDelete(EzDelete ezDelete) {
        this.deleteListeners.forEach(e -> e.onEzDelete(ezDelete));
    }

    protected void onEzBatchDelete(Collection<EzDelete> ezDeletes) {
        this.deleteListeners.forEach(e -> e.onEzBatchDelete(ezDeletes));
    }

    protected void onDelete(Object entity) {
        this.deleteListeners.forEach(e -> e.onDelete(entity));
    }

    protected void onBatchDelete(Collection<Object> models) {
        this.deleteListeners.forEach(e -> e.onBatchDelete(models));
    }

    protected void onDeleteById(Object id, Class<?> ntClass) {
        this.deleteListeners.forEach(e -> e.onDeleteById(id, ntClass));
    }

    protected void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass) {
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
        if (mapperMethod == null) {
            return invocation.proceed();
        }
        String declaringClass = mapperMethod.getDeclaringClass().getName();
        if (!declaringClass.equals(EzBaseMapper.class.getName()) && !declaringClass.equals(EzMapper.class.getName())) {
            return invocation.proceed();
        }
        String statementId = mappedStatement.getId();
        if (sqlCommandType == SqlCommandType.INSERT) {
            Map<String, Object> param = (Map<String, Object>) args[1];
            if (statementId.endsWith("." + EzMybatisConstant.INSERT_METHOD_NAME) ||
                    statementId.endsWith("." + EzMybatisConstant.INSERT_BY_TABLE_METHOD_NAME)) {
                log.debug("on insert");
                this.onInsert(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_INSERT_METHOD_NAME) ||
                    statementId.endsWith("." + EzMybatisConstant.BATCH_INSERT_BY_TABLE_METHOD_NAME)) {
                log.debug("on batch insert");
                this.onBatchInsert((Collection<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            }
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
            Map<String, Object> param = (Map<String, Object>) args[1];
            if (statementId.endsWith("." + EzMybatisConstant.UPDATE_METHOD_NAME)) {
                log.debug("on update");
                this.onUpdate(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.REPLACE_METHOD_NAME)) {
                log.debug("on replace");
                this.onReplace(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_UPDATE_METHOD_NAME)) {
                log.debug("on batch update");
                this.onBatchUpdate((Collection<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_REPLACE_METHOD_NAME)) {
                log.debug("on batch replace");
                this.onBatchReplace((Collection<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            } else if (statementId.endsWith("." + EzMybatisConstant.EZ_UPDATE_METHOD_NAME)) {
                log.debug("on ezUpdate");
                this.onEzUpdate((EzUpdate) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM));
            } else if (statementId.endsWith("." + EzMybatisConstant.EZ_BATCH_UPDATE_METHOD_NAME)) {
                log.debug("on ezBatchUpdate");
                this.onEzBatchUpdate((Collection<EzUpdate>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM));
            }
        } else if (sqlCommandType == SqlCommandType.DELETE) {
            Map<String, Object> param = (Map<String, Object>) args[1];
            if (statementId.endsWith("." + EzMapper.EZ_DELETE_METHOD)) {
                log.debug("on ezDelete");
                this.onEzDelete((EzDelete) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM));
            } else if (statementId.endsWith("." + EzMapper.EZ_BATCH_DELETE_METHOD)) {
                log.debug("on ezBatchDelete");
                this.onEzBatchDelete((Collection<EzDelete>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM));
            } else if (statementId.endsWith("." + EzMybatisConstant.DELETE_METHOD_NAME)) {
                log.debug("on delete");
                this.onDelete(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_DELETE_METHOD_NAME)) {
                log.debug("on batch delete");
                this.onBatchDelete((Collection<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            } else {
                String className = statementId.substring(0, statementId.lastIndexOf("."));
                Class<?> mapperClass = Class.forName(className);
                Class<?> etType;
                //如果是baseMapper, 实例类型从接口泛型参数上获取
                if (declaringClass.equals(EzBaseMapper.class.getName())) {
                    etType = ReflectionUtils.getGenericSuperinterface(mapperClass,
                            0, 0);
                }
                //如果是ezMapper, 实体类型从入参获取
                else {
                    etType = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
                }
                if (statementId.endsWith("." + EzMybatisConstant.DELETE_BY_ID_METHOD_NAME)) {
                    log.debug("on delete by primary key");
                    this.onDeleteById(param.get(EzMybatisConstant.MAPPER_PARAM_ID), etType);
                } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_DELETE_BY_ID_METHOD_NAME)) {
                    log.debug("on batch delete by primary key");
                    this.onBatchDeleteById((Collection<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_IDS), etType);
                }
            }
        }
        return invocation.proceed();
    }
}