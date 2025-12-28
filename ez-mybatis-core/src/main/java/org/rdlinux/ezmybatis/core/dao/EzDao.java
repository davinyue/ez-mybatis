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

/**
 * Data access object that provides convenient CRUD operations.
 * This class wraps {@link EzMapper} and offers a simplified API for common
 * database operations
 * including querying, inserting, updating, and deleting records.
 */
public class EzDao {
    private final EzMapper ezMapper;

    public EzDao(EzMapper ezMapper) {
        Assert.notNull(ezMapper, "ezMapper can not be null");
        this.ezMapper = ezMapper;
    }

    /**
     * Query by primary key
     *
     * @param <NT>   Entity type
     * @param <Id>   Primary key type
     * @param etType Entity class
     * @param id     Primary key value
     */
    public <Id extends Serializable, NT> NT selectById(Class<NT> etType, Id id) {
        return this.ezMapper.selectById(etType, id);
    }

    /**
     * Query by table and primary key
     *
     * @param <NT>   Entity type
     * @param <Id>   Primary key type
     * @param table  Table
     * @param etType Entity class
     * @param id     Primary key value
     */
    public <Id extends Serializable, NT> NT selectByTableAndId(Table table, Class<NT> etType, Id id) {
        return this.ezMapper.selectByTableAndId(table, etType, id);
    }

    /**
     * Batch query by primary keys
     *
     * @param <NT>   Entity type
     * @param <Id>   Primary key type
     * @param etType Entity class
     * @param ids    Primary key values
     */
    public <Id extends Serializable, NT> List<NT> selectByIds(Class<NT> etType, Collection<Id> ids) {
        return this.ezMapper.selectByIds(etType, ids);
    }

    /**
     * Batch query by table and primary keys
     *
     * @param <NT>   Entity type
     * @param <Id>   Primary key type
     * @param table  Table
     * @param etType Entity class
     * @param ids    Primary key values
     */
    public <Id extends Serializable, NT> List<NT> selectByTableAndIds(Table table, Class<NT> etType,
            Collection<Id> ids) {
        return this.ezMapper.selectByTableAndIds(table, etType, ids);
    }

    /**
     * Query by entity field
     *
     * @param <RtType> Result type
     * @param rtType   Result class
     * @param table    Table
     * @param field    Entity field name
     * @param value    Field value
     */
    @SuppressWarnings({ "unchecked" })
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
     * Query one record by entity field
     *
     * @param <RtType> Result type
     * @param rtType   Result class
     * @param table    Table
     * @param field    Entity field name
     * @param value    Field value
     */
    public <RtType> RtType selectOneByField(Class<RtType> rtType, EntityTable table, String field, Object value) {
        List<RtType> ret = this.selectByField(rtType, table, field, value);
        return ret.isEmpty() ? null : ret.get(0);
    }

    /**
     * Query by table column
     *
     * @param <RtType> Result type
     * @param rtType   Result class
     * @param table    Table
     * @param column   Column name
     * @param value    Column value
     */
    @SuppressWarnings({ "unchecked" })
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
     * Query one record by table column
     *
     * @param <RtType> Result type
     * @param rtType   Result class
     * @param table    Table
     * @param column   Column name
     * @param value    Column value
     */
    public <RtType> RtType selectOneByColumn(Class<RtType> rtType, Table table, String column, Object value) {
        List<RtType> ret = this.selectByColumn(rtType, table, column, value);
        return ret.isEmpty() ? null : ret.get(0);
    }

    /**
     * Query one record by SQL and return as map
     *
     * @param sql   SQL statement
     * @param param Parameters
     */
    public Map<String, Object> selectOneMapBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.selectOneMapBySql(sql, param);
    }

    /**
     * Query multiple records by SQL and return as list of maps
     *
     * @param sql   SQL statement
     * @param param Parameters
     */
    public List<Map<String, Object>> selectMapBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.selectMapBySql(sql, param);
    }

    /**
     * Query one record by SQL and return as object
     *
     * @param clazz Result class
     * @param sql   SQL statement
     * @param param Parameters
     */
    public <T> T selectOneObjectBySql(Class<T> clazz, String sql, Map<String, Object> param) {
        return this.ezMapper.selectOneObjectBySql(clazz, sql, param);
    }

    /**
     * Query records by SQL and return as list of objects
     *
     * @param clazz Result class
     * @param sql   SQL statement
     * @param param Parameters
     */
    public <T> List<T> selectObjectBySql(Class<T> clazz, String sql, Map<String, Object> param) {
        return this.ezMapper.selectObjectBySql(clazz, sql, param);
    }

    /**
     * Query by EzQuery
     *
     * @param <Rt>  Result type
     * @param query Query parameters
     */
    public <Rt> List<Rt> query(EzQuery<Rt> query) {
        return this.ezMapper.query(query);
    }

    /**
     * Query one record by EzQuery
     *
     * @param <Rt>  Result type
     * @param query Query parameters
     */
    public <Rt> Rt queryOne(EzQuery<Rt> query) {
        return this.ezMapper.queryOne(query);
    }

    /**
     * Query count by EzQuery
     *
     * @param query Query parameters
     */
    public int queryCount(EzQuery<?> query) {
        return this.ezMapper.queryCount(query);
    }

    /**
     * Query data and count
     *
     * @param <RetType> Result type
     * @param param     Query parameters
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
     * Update by EzUpdate parameters
     *
     * @param update Update parameters
     */
    public int ezUpdate(EzUpdate update) {
        return this.ezMapper.ezUpdate(update);
    }

    /**
     * Batch update by EzUpdate parameters
     *
     * @param updates Update parameters
     */
    public void ezBatchUpdate(Collection<EzUpdate> updates) {
        this.ezMapper.ezBatchUpdate(updates);
    }

    /**
     * Update by SQL
     *
     * @param sql   SQL statement
     * @param param Parameters
     */
    public Integer updateBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.updateBySql(sql, param);
    }

    /**
     * Extended update
     *
     * @param expand Extended SQL object
     */
    public Integer expandUpdate(SqlExpand expand) {
        return this.ezMapper.expandUpdate(expand);
    }

    /**
     * Delete by EzDelete parameters
     *
     * @param delete Delete parameters
     */
    public int ezDelete(EzDelete delete) {
        return this.ezMapper.ezDelete(delete);
    }

    /**
     * Batch delete by EzDelete parameters
     *
     * @param deletes Delete parameters
     */
    public void ezBatchDelete(Collection<EzDelete> deletes) {
        this.ezMapper.ezBatchDelete(deletes);
    }

    /**
     * Delete by SQL
     *
     * @param sql   SQL statement
     * @param param Parameters
     */
    public Integer deleteBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.deleteBySql(sql, param);
    }

    /**
     * Delete by entity field
     *
     * @param table Table to delete from
     * @param field Entity field name
     * @param value Field value
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
     * Delete by table column
     *
     * @param table  Table to delete from
     * @param column Column name
     * @param value  Column value
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
     * Insert a record. Note: This method can only insert a single entity,
     * cannot accept map, collection or array.
     *
     * @param model Object to insert
     */
    public int insert(Object model) {
        return this.ezMapper.insert(model);
    }

    /**
     * Insert a record into the specified table. Note: This method can only insert a
     * single entity,
     * cannot accept map, collection or array.
     *
     * @param table Table to insert into
     * @param model Object to insert
     */
    public int insertByTable(Table table, Object model) {
        return this.ezMapper.insertByTable(table, model);
    }

    /**
     * Batch insert records. For optimal performance, keep total entity properties
     * under 5000 per batch.
     *
     * @param models Objects to insert
     */
    public int batchInsert(Collection<?> models) {
        return this.ezMapper.batchInsert(models);
    }

    /**
     * Batch insert records into the specified table. For optimal performance,
     * keep total entity properties under 5000 per batch.
     *
     * @param table  Table to insert into
     * @param models Objects to insert
     */
    public int batchInsertByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchInsertByTable(table, models);
    }

    /**
     * Insert records by SQL
     *
     * @param sql   SQL statement
     * @param param Parameters
     */
    public Integer insertBySql(String sql, Map<String, Object> param) {
        return this.ezMapper.insertBySql(sql, param);
    }

    /**
     * Insert into the specified table by query
     *
     * @param table Table to insert into
     * @param query Query parameters
     */
    public Integer insertByQuery(Table table, EzQuery<?> query) {
        return this.ezMapper.insertByQuery(table, query);
    }

    /**
     * Update a record, only updates non-null fields
     *
     * @param model Object to update
     */
    public int update(Object model) {
        return this.ezMapper.update(model);
    }

    /**
     * Update a record in the specified table, only updates non-null fields
     *
     * @param table Table to update
     * @param model Object to update
     */
    public int updateByTable(Table table, Object model) {
        return this.ezMapper.updateByTable(table, model);
    }

    /**
     * Batch update records, only updates non-null fields
     *
     * @param models Objects to update
     */
    public int batchUpdate(Collection<?> models) {
        return this.ezMapper.batchUpdate(models);
    }

    /**
     * Batch update records in the specified table, only updates non-null fields
     *
     * @param table  Table to update
     * @param models Objects to update
     */
    public int batchUpdateByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchUpdateByTable(table, models);
    }

    /**
     * Replace a record, updates all fields
     *
     * @param model Object to replace
     */
    public int replace(Object model) {
        return this.ezMapper.replace(model);
    }

    /**
     * Replace a record in the specified table, updates all fields
     *
     * @param table Table to update
     * @param model Object to replace
     */
    public int replaceByTable(Table table, Object model) {
        return this.ezMapper.replaceByTable(table, model);
    }

    /**
     * Batch replace records, updates all fields
     *
     * @param models Objects to replace
     */
    public int batchReplace(Collection<?> models) {
        return this.ezMapper.batchReplace(models);
    }

    /**
     * Batch replace records in the specified table, updates all fields
     *
     * @param table  Table to update
     * @param models Objects to replace
     */
    public int batchReplaceByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchReplaceByTable(table, models);
    }

    /**
     * Delete a record
     *
     * @param model Object to delete
     */
    public int delete(Object model) {
        return this.ezMapper.delete(model);
    }

    /**
     * Delete a record from the specified table
     *
     * @param table Table to delete from
     * @param model Object to delete
     */
    public int deleteByTable(Table table, Object model) {
        return this.ezMapper.deleteByTable(table, model);
    }

    /**
     * Batch delete records
     *
     * @param models Objects to delete
     */
    public int batchDelete(Collection<?> models) {
        return this.ezMapper.batchDelete(models);
    }

    /**
     * Batch delete records from the specified table
     *
     * @param table  Table to delete from
     * @param models Objects to delete
     */
    public int batchDeleteByTable(Table table, Collection<?> models) {
        return this.ezMapper.batchDeleteByTable(table, models);
    }

    /**
     * Delete by primary key
     *
     * @param etType Entity class
     * @param id     Primary key value
     */
    public <T extends Serializable> int deleteById(Class<?> etType, T id) {
        return this.ezMapper.deleteById(etType, id);
    }

    /**
     * Delete by table and primary key
     *
     * @param table  Table to delete from
     * @param etType Entity class
     * @param id     Primary key value
     */
    public <T extends Serializable> int deleteByTableAndId(Table table, Class<?> etType, T id) {
        return this.ezMapper.deleteByTableAndId(table, etType, id);
    }

    /**
     * Batch delete by primary keys
     *
     * @param etType Entity class
     * @param ids    Primary key values
     */
    public <T extends Serializable> int batchDeleteById(Class<?> etType, Collection<T> ids) {
        return this.ezMapper.batchDeleteById(etType, ids);
    }

    /**
     * Batch delete by table and primary keys
     *
     * @param table  Table to delete from
     * @param etType Entity class
     * @param ids    Primary key values
     */
    public <T extends Serializable> int batchDeleteByTableAndId(Table table, Class<?> etType, Collection<T> ids) {
        return this.ezMapper.batchDeleteByTableAndId(table, etType, ids);
    }
}
