package org.rdlinux.ezmybatis.core.dao;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlExpand;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.dto.DcDTO;
import org.rdlinux.ezmybatis.utils.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EzDao {
    private final EzMapper ezMapper;

    public EzDao(EzMapper ezMapper) {
        Assert.notNull(ezMapper, "ezMapper can not be null");
        this.ezMapper = ezMapper;
    }


    /**
     * 根据主键查询
     *
     * @param <NT>   实体类型
     * @param <Id>   主键类型
     * @param etType 实体类型
     * @param id     主键
     */
    public <Id extends Serializable, NT> NT selectById(Class<NT> etType, Id id) {
        return this.ezMapper.selectById(etType, id);
    }

    /**
     * 根据主键查询
     *
     * @param <NT>   实体类型
     * @param <Id>   主键类型
     * @param table  表
     * @param etType 实体类型
     * @param id     主键
     */
    public <Id extends Serializable, NT> NT selectByTableAndId(Table table, Class<NT> etType, Id id) {
        return this.ezMapper.selectByTableAndId(table, etType, id);
    }

    /**
     * 根据主键批量查询
     *
     * @param <NT>   实体类型
     * @param <Id>   主键类型
     * @param etType 实体类型
     * @param ids    主键
     */
    public <Id extends Serializable, NT> List<NT> selectByIds(Class<NT> etType, Collection<Id> ids) {
        return this.ezMapper.selectByIds(etType, ids);
    }

    /**
     * 根据主键批量查询
     *
     * @param <NT>   实体类型
     * @param <Id>   主键类型
     * @param table  表
     * @param etType 实体类型
     * @param ids    主键
     */
    public <Id extends Serializable, NT> List<NT> selectByTableAndIds(Table table, Class<NT> etType,
                                                                      Collection<Id> ids) {
        return this.ezMapper.selectByTableAndIds(table, etType, ids);
    }

    /**
     * 根据field查询结果
     *
     * @param <RtType> 结果类型
     * @param rtType   结果类型
     * @param table    表
     * @param field    实体filed名称
     * @param value    field值
     */
    @SuppressWarnings({"unchecked"})
    public <RtType> List<RtType> selectByField(Class<RtType> rtType, EntityTable table, String field, Object value) {
        Assert.notNull(rtType, "rtType can not be null");
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(rtType).from(table)
                .select().addAll().done()
                .where()
                .addFieldCondition(field, value).done().build();
        return (List<RtType>) this.ezMapper.query(query);
    }

    /**
     * 根据field查询一个结果
     *
     * @param <RtType> 结果类型
     * @param rtType   结果类型
     * @param table    表
     * @param field    实体filed名称
     * @param value    field值
     */
    public <RtType> RtType selectOneByField(Class<RtType> rtType, EntityTable table, String field, Object value) {
        List<RtType> ret = this.selectByField(rtType, table, field, value);
        return ret.isEmpty() ? null : ret.get(0);
    }

    /**
     * 根据column查询结果
     *
     * @param <RtType> 结果类型
     * @param rtType   结果类型
     * @param table    表
     * @param column   列名称
     * @param value    列值
     */
    @SuppressWarnings({"unchecked"})
    public <RtType> List<RtType> selectByColumn(Class<RtType> rtType, Table table, String column, Object value) {
        Assert.notNull(rtType, "rtType can not be null");
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(rtType)
                .from(table)
                .select().addAll().done()
                .where()
                .addColumnCondition(column, value)
                .done().build();
        return (List<RtType>) this.ezMapper.query(query);
    }

    /**
     * 根据column查询一个结果
     *
     * @param <RtType> 结果类型
     * @param rtType   结果类型
     * @param table    表
     * @param column   列名称
     * @param value    列值
     */
    public <RtType> RtType selectOneByColumn(Class<RtType> rtType, Table table, String column, Object value) {
        List<RtType> ret = this.selectByColumn(rtType, table, column, value);
        return ret.isEmpty() ? null : ret.get(0);
    }

    /**
     * 根据sql查询一条数据并返回map
     *
     * @param sql   sql
     * @param param 参数
     */
    public Map<String, Object> selectOneMapBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.selectOneMapBySql(sql, param);
    }

    /**
     * 根据sql查询多条数据, 并返回list map
     *
     * @param sql   sql
     * @param param 参数
     */
    public List<Map<String, Object>> selectMapBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.selectMapBySql(sql, param);
    }

    /**
     * 根据sql查询一条数据并返回对象
     *
     * @param clazz 结果类型
     * @param sql   sql
     * @param param 参数
     */
    public <T> T selectOneObjectBySql(Class<T> clazz, String sql, Map<String, Object> param) {
        return this.ezMapper.selectOneObjectBySql(clazz, sql, param);
    }

    /**
     * 根据sql查询数据并返回对象列表
     *
     * @param clazz 结果类型
     * @param sql   sql
     * @param param 参数
     */
    public <T> List<T> selectObjectBySql(Class<T> clazz, String sql, Map<String, Object> param) {
        return this.ezMapper.selectObjectBySql(clazz, sql, param);
    }

    /**
     * 根据ezQuery查询
     *
     * @param <Rt>  结果类型
     * @param query 查询参数
     */
    public <Rt> List<Rt> query(EzQuery<Rt> query) {
        return this.ezMapper.query(query);
    }

    /**
     * 根据ezQuery查询单条结果
     *
     * @param <Rt>  结果类型
     * @param query 查询参数
     */
    public <Rt> Rt queryOne(EzQuery<Rt> query) {
        return this.ezMapper.queryOne(query);
    }

    /**
     * 根据ezQuery查询count
     *
     * @param query 查询参数
     */
    public int queryCount(EzQuery<?> query) {
        return this.ezMapper.queryCount(query);
    }

    /**
     * 根据条件数据和总数
     *
     * @param <RetType> 结果类型
     * @param param     查询参数
     */
    public <RetType> DcDTO<RetType> queryDataAndCount(EzQuery<RetType> param) {
        Assert.notNull(param, "param can not be null");
        int count = this.ezMapper.queryCount(param);
        if (count > 0) {
            List<RetType> data = this.ezMapper.query(param);
            return new DcDTO<>(count, data);
        } else {
            return new DcDTO<>(0, Collections.emptyList());
        }
    }

    /**
     * 根据更新参数更新
     *
     * @param update 更新参数
     */
    public int ezUpdate(EzUpdate update) {
        return this.ezMapper.ezUpdate(update);
    }

    /**
     * 根据更新参数更新
     *
     * @param updates 更新参数
     */
    public void ezBatchUpdate(Collection<EzUpdate> updates) {
        this.ezMapper.ezBatchUpdate(updates);
    }

    /**
     * 根据sql更新
     *
     * @param sql   sql
     * @param param 参数
     */
    public Integer updateBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.updateBySql(sql, param);
    }

    /**
     * 扩展更新
     *
     * @param expand 扩展sql对象
     */
    public Integer expandUpdate(SqlExpand expand) {
        return this.ezMapper.expandUpdate(expand);
    }

    /**
     * 批量删除
     *
     * @param delete 删除参数
     */
    public int ezDelete(EzDelete delete) {
        return this.ezMapper.ezDelete(delete);
    }

    /**
     * 批量删除
     *
     * @param deletes 删除参数
     */
    public void ezBatchDelete(Collection<EzDelete> deletes) {
        this.ezMapper.ezBatchDelete(deletes);
    }

    /**
     * 根据sql删除
     *
     * @param sql   sql
     * @param param 参数
     */
    public Integer deleteBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.deleteBySql(sql, param);
    }

    /**
     * 根据属性删除
     *
     * @param table 删除表
     * @param field 实体属性
     * @param value 属性值
     */
    public int deleteByField(EntityTable table, String field, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        EzDelete delete = EzDelete.delete(table)
                .where().addFieldCondition(field, value).done().build();
        return this.ezMapper.ezDelete(delete);
    }

    /**
     * 根据列删除
     *
     * @param table  删除表
     * @param column 表列
     * @param value  列值
     */
    public int deleteByColumn(Table table, String column, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzDelete delete = EzDelete.delete(table)
                .where().addColumnCondition(column, value).done().build();
        return this.ezMapper.ezDelete(delete);
    }

    /**
     * 插入, 注意, 该接口仅能插入单条实体数据, 不能传入map或collection或array
     *
     * @param model 插入对象
     */
    public int insert(Object model) {
        return this.ezMapper.insert(model);
    }

    /**
     * 插入,指定表, 注意, 该接口仅能插入单条实体数据, 不能传入map或collection或array
     *
     * @param table 插入表
     * @param model 插入对象
     */
    public int insertByTable(Table table, Object model) {
        return this.ezMapper.insertByTable(table, model);
    }

    /**
     * 批量插入, 实体总属性控制在5000个以下, 一个批次保存效率最高
     *
     * @param models 插入对象
     */
    public int batchInsert(Collection<?> models) {
        return this.ezMapper.batchInsert(models);
    }

    /**
     * 批量插入, 指定表, 实体总属性控制在5000个以下, 一个批次保存效率最高
     *
     * @param table  插入表
     * @param models 插入对象
     */
    public int batchInsertByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchInsertByTable(table, models);
    }

    /**
     * 根据sql插入记录
     *
     * @param sql   sql
     * @param param 参数
     */
    public Integer insertBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.insertBySql(sql, param);
    }

    /**
     * 根据Query插入指定表
     *
     * @param table 插入表
     * @param query 查询参数
     */
    public Integer insertByQuery(Table table, EzQuery<?> query) {
        return this.ezMapper.insertByQuery(table, query);
    }

    /**
     * 更新, 只更新非空字段
     *
     * @param model 更新对象
     */
    public int update(Object model) {
        return this.ezMapper.update(model);
    }

    /**
     * 更新, 只更新非空字段
     *
     * @param table 更新表
     * @param model 更新对象
     */
    public int updateByTable(Table table, Object model) {
        return this.ezMapper.updateByTable(table, model);
    }

    /**
     * 批量更新, 只更新非空字段
     *
     * @param models 更新对象
     */
    public int batchUpdate(Collection<?> models) {
        return this.ezMapper.batchUpdate(models);
    }

    /**
     * 批量更新, 只更新非空字段
     *
     * @param table  更新表
     * @param models 更新对象
     */
    public int batchUpdateByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchUpdateByTable(table, models);
    }

    /**
     * 更新, 更新所有字段
     *
     * @param model 更新对象
     */
    public int replace(Object model) {
        return this.ezMapper.replace(model);
    }

    /**
     * 更新, 更新所有字段
     *
     * @param table 更新表
     * @param model 更新对象
     */
    public int replaceByTable(Table table, Object model) {
        return this.ezMapper.replaceByTable(table, model);
    }

    /**
     * 批量更新, 更新所有字段
     *
     * @param models 更新对象
     */
    public int batchReplace(Collection<?> models) {
        return this.ezMapper.batchReplace(models);
    }

    /**
     * 批量更新, 更新所有字段
     *
     * @param table  更新表
     * @param models 更新对象
     */
    public int batchReplaceByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchReplaceByTable(table, models);
    }

    /**
     * 删除
     *
     * @param model 删除对象
     */
    public int delete(Object model) {
        return this.ezMapper.delete(model);
    }

    /**
     * 删除
     *
     * @param table 删除表
     * @param model 删除对象
     */
    public int deleteByTable(Table table, Object model) {
        return this.ezMapper.deleteByTable(table, model);
    }

    /**
     * 批量删除
     *
     * @param models 删除对象
     */
    public int batchDelete(Collection<?> models) {
        return this.ezMapper.batchDelete(models);
    }

    /**
     * 批量删除
     *
     * @param table  删除表
     * @param models 删除对象
     */
    public int batchDeleteByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchDeleteByTable(table, models);
    }

    /**
     * 根据主键删除
     *
     * @param etType 删除对象类型
     * @param id     主键
     */
    public <T extends Serializable> int deleteById(Class<?> etType, T id) {
        return this.ezMapper.deleteById(etType, id);
    }

    /**
     * 根据主键删除
     *
     * @param table  删除表
     * @param etType 删除对象类型
     * @param id     主键
     */
    public <T extends Serializable> int deleteByTableAndId(Table table, Class<?> etType, T id) {
        return this.ezMapper.deleteByTableAndId(table, etType, id);
    }

    /**
     * 根据主键批量删除
     *
     * @param etType 删除对象类型
     * @param ids    主键
     */
    public <T extends Serializable> int batchDeleteById(Class<?> etType, Collection<T> ids) {
        return this.ezMapper.batchDeleteById(etType, ids);
    }

    /**
     * 根据主键批量删除
     *
     * @param table  删除表
     * @param etType 删除对象类型
     * @param ids    主键
     */
    public <T extends Serializable> int batchDeleteByTableAndId(Table table, Class<?> etType, Collection<T> ids) {
        return this.ezMapper.batchDeleteByTableAndId(table, etType, ids);
    }
}
