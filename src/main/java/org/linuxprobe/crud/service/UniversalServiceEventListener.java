package org.linuxprobe.crud.service;

import org.linuxprobe.crud.core.query.BaseQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 通用service方法调用事件,可用于在操作数据库之前进行一些操作,比如设置审计信息
 */
public interface UniversalServiceEventListener {
    /**
     * 保存前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeSave(UniversalService<?, ?, ?> target, Object model) {
        return true;
    }

    /**
     * 保存后
     *
     * @return 返回结果
     */
    default <T> T afterSave(UniversalService<?, ?, ?> target, T model, T result) {
        return result;
    }

    /**
     * 批量保存前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeBatchSave(UniversalService<?, ?, ?> target, List<?> models, boolean loop) {
        return true;
    }

    /**
     * 批量保存后
     *
     * @return 返回结果
     */
    default <T> List<T> afterBatchSave(UniversalService<?, ?, ?> target, List<T> models, List<T> result, boolean loop) {
        return result;
    }

    /**
     * 删除前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeRemoveByPrimaryKey(UniversalService<?, ?, ?> target, Class<?> type, Serializable id) {
        return true;
    }

    /**
     * 删除后
     */
    default int afterRemoveByPrimaryKey(UniversalService<?, ?, ?> target, Class<?> type, Serializable id, int result) {
        return result;
    }

    /**
     * 批量删除前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default <IdType extends Serializable> boolean beforeBatchRemoveByPrimaryKey(UniversalService<?, ?, ?> target, Class<?> type, List<IdType> ids) {
        return true;
    }

    /**
     * 批量删除后
     */
    default <IdType extends Serializable> long afterBatchRemoveByPrimaryKey(UniversalService<?, ?, ?> target, Class<?> type, List<IdType> ids, long result) {
        return result;
    }

    /**
     * 删除前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeRemove(UniversalService<?, ?, ?> target, Object record) {
        return true;
    }

    /**
     * 删除后
     */
    default int afterRemove(UniversalService<?, ?, ?> target, Object record, int result) {
        return result;
    }

    /**
     * 批量删除前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeBatchRemove(UniversalService<?, ?, ?> target, List<?> records) {
        return true;
    }

    /**
     * 批量删除后
     */
    default int afterBatchRemove(UniversalService<?, ?, ?> target, List<?> records, int result) {
        return result;
    }

    /**
     * 根据主键查询前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeGetByPrimaryKey(UniversalService<?, ?, ?> target, Class<?> type, Serializable id) {
        return true;
    }

    /**
     * 根据主键查询后
     */
    default <T> T afterGetByPrimaryKey(UniversalService<?, ?, ?> target, Class<?> type, Serializable id, T result) {
        return result;
    }


    /**
     * 查询前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default <Query extends BaseQuery> boolean beforeGetByQueryParam(UniversalService<?, ?, ?> target, Query query) {
        return true;
    }

    /**
     * 查询后
     */
    default <Query extends BaseQuery, T> List<T> afterGetByQueryParam(UniversalService<?, ?, ?> target, Query query, List<T> result) {
        return result;
    }

    /**
     * 获取数量前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default <Query extends BaseQuery> boolean beforeGetCountByQueryParam(UniversalService<?, ?, ?> target, Query query) {
        return true;
    }

    /**
     * 获取数量后
     */
    default <Query extends BaseQuery> long afterGetCountByQueryParam(UniversalService<?, ?, ?> target, Query query, long result) {
        return result;
    }

    /**
     * 替换前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeGlobalUpdate(UniversalService<?, ?, ?> target, Object record) {
        return true;
    }

    /**
     * 替换后
     */
    default <T> T afterGlobalUpdate(UniversalService<?, ?, ?> target, T record, T result) {
        return result;
    }

    /**
     * 更新前
     *
     * @return 返回true继续执行, 返回false中断执行
     */
    default boolean beforeLocalUpdate(UniversalService<?, ?, ?> target, Object record) {
        return true;
    }

    /**
     * 更新后
     */
    default <T> T afterLocalUpdate(UniversalService<?, ?, ?> target, T record, T result) {
        return result;
    }
}
