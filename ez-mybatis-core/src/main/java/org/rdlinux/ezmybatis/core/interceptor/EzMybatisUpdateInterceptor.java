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

/**
 * 写操作监听拦截器。
 *
 * <p>该拦截器拦截 MyBatis {@link Executor#update(MappedStatement, Object)} 调用，
 * 仅在目标语句来自 Ez-MyBatis 通用 Mapper 体系时生效。它会根据具体的方法名和 SQL 类型，
 * 在真正执行 SQL 前触发插入、更新、删除相关监听器。</p>
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class EzMybatisUpdateInterceptor implements Interceptor {
    /**
     * 拦截器日志对象。
     */
    private static final Log log = LogFactory.getLog(EzMybatisUpdateInterceptor.class);
    /**
     * {@link ProviderSqlSource} 中 mapper 方法字段的反射句柄。
     */
    private static final Field MAPPER_METHOD_FIELD = ReflectionUtils.getField(ProviderSqlSource.class,
            "mapperMethod");
    /**
     * 插入监听器列表。
     */
    private final List<EzMybatisInsertListener> insertListeners = new LinkedList<>();
    /**
     * 更新监听器列表。
     */
    private final List<EzMybatisUpdateListener> updateListeners = new LinkedList<>();
    /**
     * 删除监听器列表。
     */
    private final List<EzMybatisDeleteListener> deleteListeners = new LinkedList<>();

    /**
     * 添加插入监听器。
     *
     * @param insertListener 插入监听器
     */
    public void addInsertListener(EzMybatisInsertListener insertListener) {
        this.insertListeners.add(insertListener);
    }

    /**
     * 添加更新监听器。
     *
     * @param updateListener 更新监听器
     */
    public void addUpdateListener(EzMybatisUpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    /**
     * 添加删除监听器。
     *
     * @param deleteListener 删除监听器
     */
    public void addDeleteListener(EzMybatisDeleteListener deleteListener) {
        this.deleteListeners.add(deleteListener);
    }

    /**
     * 触发单条插入监听事件。
     *
     * @param model 被插入的实体对象
     */
    protected void onInsert(Object model) {
        this.insertListeners.forEach(e -> e.onInsert(model));
    }

    /**
     * 触发批量插入监听事件。
     *
     * @param models 被插入的实体集合
     */
    protected void onBatchInsert(Collection<Object> models) {
        this.insertListeners.forEach(e -> e.onBatchInsert(models));
    }

    /**
     * 触发单条更新监听事件。
     *
     * @param model 被更新的实体对象
     */
    protected void onUpdate(Object model) {
        this.updateListeners.forEach(e -> e.onUpdate(model));
    }

    /**
     * 触发批量更新监听事件。
     *
     * @param models 被更新的实体集合
     */
    protected void onBatchUpdate(Collection<Object> models) {
        this.updateListeners.forEach(e -> e.onBatchUpdate(models));
    }

    /**
     * 触发整对象替换监听事件。
     *
     * @param entity 被替换的实体对象
     */
    protected void onReplace(Object entity) {
        this.updateListeners.forEach(e -> e.onReplace(entity));
    }

    /**
     * 触发批量整对象替换监听事件。
     *
     * @param models 被替换的实体集合
     */
    protected void onBatchReplace(Collection<Object> models) {
        this.updateListeners.forEach(e -> e.onBatchReplace(models));
    }

    /**
     * 触发 EzUpdate DSL 更新监听事件。
     *
     * @param ezUpdate EzUpdate 对象
     */
    protected void onEzUpdate(EzUpdate ezUpdate) {
        this.updateListeners.forEach(e -> e.onEzUpdate(ezUpdate));
    }

    /**
     * 触发批量 EzUpdate DSL 更新监听事件。
     *
     * @param ezUpdates EzUpdate 集合
     */
    protected void onEzBatchUpdate(Collection<EzUpdate> ezUpdates) {
        this.updateListeners.forEach(e -> e.onEzBatchUpdate(ezUpdates));
    }

    /**
     * 触发 EzDelete DSL 删除监听事件。
     *
     * @param ezDelete EzDelete 对象
     */
    protected void onEzDelete(EzDelete ezDelete) {
        this.deleteListeners.forEach(e -> e.onEzDelete(ezDelete));
    }

    /**
     * 触发批量 EzDelete DSL 删除监听事件。
     *
     * @param ezDeletes EzDelete 集合
     */
    protected void onEzBatchDelete(Collection<EzDelete> ezDeletes) {
        this.deleteListeners.forEach(e -> e.onEzBatchDelete(ezDeletes));
    }

    /**
     * 触发单条实体删除监听事件。
     *
     * @param entity 被删除实体
     */
    protected void onDelete(Object entity) {
        this.deleteListeners.forEach(e -> e.onDelete(entity));
    }

    /**
     * 触发批量实体删除监听事件。
     *
     * @param models 被删除实体集合
     */
    protected void onBatchDelete(Collection<Object> models) {
        this.deleteListeners.forEach(e -> e.onBatchDelete(models));
    }

    /**
     * 触发按主键删除监听事件。
     *
     * @param id      主键值
     * @param ntClass 实体类型
     */
    protected void onDeleteById(Object id, Class<?> ntClass) {
        this.deleteListeners.forEach(e -> e.onDeleteById(id, ntClass));
    }

    /**
     * 触发按主键批量删除监听事件。
     *
     * @param ids     主键集合
     * @param ntClass 实体类型
     */
    protected void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass) {
        this.deleteListeners.forEach(e -> e.onBatchDeleteById(ids, ntClass));
    }

    /**
     * 拦截写操作执行，并在真正执行 SQL 前按语句类型触发对应监听器。
     *
     * @param invocation 当前拦截调用
     * @return 原始执行结果
     * @throws Throwable 反射或执行异常
     */
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
            } else if (statementId.endsWith("." + EzMybatisConstant.REPLACE_METHOD_NAME) ||
                    statementId.endsWith("." + EzMybatisConstant.REPLACE_BY_TABLE_METHOD_NAME)) {
                log.debug("on replace");
                this.onReplace(param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_UPDATE_METHOD_NAME)) {
                log.debug("on batch update");
                this.onBatchUpdate((Collection<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS));
            } else if (statementId.endsWith("." + EzMybatisConstant.BATCH_REPLACE_METHOD_NAME) ||
                    statementId.endsWith("." + EzMybatisConstant.BATCH_REPLACE_BY_TABLE_METHOD_NAME)) {
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
                if (statementId.endsWith("." + EzMybatisConstant.DELETE_BY_ID_METHOD_NAME) ||
                        statementId.endsWith("." + EzMybatisConstant.BATCH_DELETE_BY_ID_METHOD_NAME)) {
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
                        this.onBatchDeleteById((Collection<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_IDS),
                                etType);
                    }
                }
            }
        }
        return invocation.proceed();
    }
}
