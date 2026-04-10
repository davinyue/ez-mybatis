package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.*;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.mapper.provider.EzDeleteProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzInsertProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzSelectProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzUpdateProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlExpand;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用动态 Mapper 接口。
 *
 * <p>该接口面向“运行时决定实体类型或返回类型”的场景，提供统一的增删改查能力，
 * 包括：</p>
 * <p>1. 基于实体类型和主键的通用 CRUD。</p>
 * <p>2. 基于 {@link EzQuery} / {@link EzUpdate} / {@link EzDelete} 的 DSL 操作。</p>
 * <p>3. 直接执行 SQL 或 SQL 扩展结构的能力。</p>
 *
 * <p>与 {@link EzBaseMapper} 相比，该接口不依赖编译期泛型绑定，更适合作为基础通用 Mapper
 * 或框架内部统一入口使用。</p>
 */
@Mapper
public interface EzMapper {
    /**
     * DSL 列表查询方法名常量。
     */
    String QUERY_METHOD = "query";
    /**
     * DSL 单条查询方法名常量。
     */
    String QUERY_ONE_METHOD = "queryOne";
    /**
     * DSL 统计查询方法名常量。
     */
    String QUERY_COUNT_METHOD = "queryCount";
    /**
     * 按主键查询方法名常量。
     */
    String SELECT_BY_ID_METHOD = "selectById";
    /**
     * 按指定表和主键查询方法名常量。
     */
    String SELECT_BY_TABLE_AND_ID_METHOD = "selectByTableAndId";
    /**
     * 按主键集合查询方法名常量。
     */
    String SELECT_BY_IDS_METHOD = "selectByIds";
    /**
     * 按指定表和主键集合查询方法名常量。
     */
    String SELECT_BY_TABLE_AND_IDS_METHOD = "selectByTableAndIds";
    /**
     * DSL 删除方法名常量。
     */
    String EZ_DELETE_METHOD = "ezDelete";
    /**
     * DSL 批量删除方法名常量。
     */
    String EZ_BATCH_DELETE_METHOD = "ezBatchDelete";
    /**
     * 原生 SQL 单对象查询方法名常量。
     */
    String SELECT_ONE_OBJECT_BY_SQL_METHOD = "selectOneObjectBySql";
    /**
     * 原生 SQL 对象列表查询方法名常量。
     */
    String SELECT_OBJECT_BY_SQL_METHOD = "selectObjectBySql";

    /**
     * Query entity by primary key.
     *
     * @param <Id>   the type of primary key, must extend Serializable
     * @param <NT>   the type of entity to return
     * @param etType the entity class type
     * @param id     the primary key value
     * @return the entity object, or null if not found
     */
    @MethodName(SELECT_BY_ID_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_ID_METHOD)
    <Id extends Serializable, NT> NT selectById(
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_ID) Id id);

    /**
     * Query entity by primary key from a specified table.
     *
     * @param <Id>   the type of primary key, must extend Serializable
     * @param <NT>   the type of entity to return
     * @param table  the table to query from
     * @param etType the entity class type
     * @param id     the primary key value
     * @return the entity object, or null if not found
     */
    @MethodName(SELECT_BY_TABLE_AND_ID_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_ID_METHOD)
    <Id extends Serializable, NT> NT selectByTableAndId(
            @Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_ID) Id id);

    /**
     * Batch query entities by primary keys.
     *
     * @param <Id>   the type of primary key, must extend Serializable
     * @param <NT>   the type of entity to return
     * @param etType the entity class type
     * @param ids    the collection of primary key values
     * @return list of entity objects
     */
    @MethodName(SELECT_BY_IDS_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_IDS_METHOD)
    <Id extends Serializable, NT> List<NT> selectByIds(
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Id> ids);

    /**
     * Batch query entities by primary keys from a specified table.
     *
     * @param <Id>   the type of primary key, must extend Serializable
     * @param <NT>   the type of entity to return
     * @param table  the table to query from
     * @param etType the entity class type
     * @param ids    the collection of primary key values
     * @return list of entity objects
     */
    @MethodName(SELECT_BY_TABLE_AND_IDS_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_IDS_METHOD)
    <Id extends Serializable, NT> List<NT> selectByTableAndIds(
            @Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Id> ids);

    /**
     * Query a single record by SQL and return as a map.
     *
     * @param sql   the SQL query string
     * @param param the SQL parameters
     * @return a map containing the query result, or null if not found
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    Map<String, Object> selectOneMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                          @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Query multiple records by SQL and return as a list of maps.
     *
     * @param sql   the SQL query string
     * @param param the SQL parameters
     * @return list of maps containing the query results
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    List<Map<String, Object>> selectMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                             @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Query a single record by SQL and return as an object.
     *
     * @param <T>   the type of object to return
     * @param clazz the class type of the return object
     * @param sql   the SQL query string
     * @param param the SQL parameters
     * @return the object instance, or null if not found
     */
    @MethodName(SELECT_ONE_OBJECT_BY_SQL_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    <T> T selectOneObjectBySql(@Param(EzMybatisConstant.MAPPER_PARAM_RET) Class<T> clazz,
                               @Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                               @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Query multiple records by SQL and return as a list of objects.
     *
     * @param <T>   the type of object to return
     * @param clazz the class type of the return objects
     * @param sql   the SQL query string
     * @param param the SQL parameters
     * @return list of object instances
     */
    @MethodName(SELECT_OBJECT_BY_SQL_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    <T> List<T> selectObjectBySql(@Param(EzMybatisConstant.MAPPER_PARAM_RET) Class<T> clazz,
                                  @Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                  @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Query records using EzQuery.
     *
     * @param <Rt>  the type of result to return
     * @param query the EzQuery object containing query conditions
     * @return list of result objects
     */
    @MethodName(QUERY_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    <Rt> List<Rt> query(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    /**
     * Query a single record using EzQuery.
     *
     * @param <Rt>  the type of result to return
     * @param query the EzQuery object containing query conditions
     * @return the result object, or null if not found
     */
    @MethodName(QUERY_ONE_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    <Rt> Rt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    /**
     * Query the count of records using EzQuery.
     *
     * @param query the EzQuery object containing query conditions
     * @return the count of matching records
     */
    @MethodName(QUERY_COUNT_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_COUNT_METHOD)
    int queryCount(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<?> query);

    /**
     * Update records using EzUpdate.
     *
     * @param update the EzUpdate object containing update conditions and values
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_EZ_UPDATE_METHOD)
    int ezUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzUpdate update);

    /**
     * Batch update records using EzUpdate.
     *
     * @param updates the collection of EzUpdate objects containing update
     *                conditions and values
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_BY_EZ_UPDATE_METHOD)
    void ezBatchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) Collection<EzUpdate> updates);

    /**
     * Update records by SQL.
     *
     * @param sql   the SQL update statement
     * @param param the SQL parameters
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_SQL_METHOD)
    Integer updateBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Update records using SQL expansion.
     *
     * @param expand the SqlExpand object containing update logic
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.EXPAND_UPDATE_METHOD)
    Integer expandUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_UPDATE_EXPAND) SqlExpand expand);

    /**
     * Delete records using EzDelete.
     *
     * @param delete the EzDelete object containing delete conditions
     * @return the number of affected rows
     */
    @MethodName(EZ_DELETE_METHOD)
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_EZ_DELETE_METHOD)
    int ezDelete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzDelete delete);

    /**
     * Batch delete records using EzDelete.
     *
     * @param deletes the collection of EzDelete objects containing delete
     *                conditions
     */
    @MethodName(EZ_BATCH_DELETE_METHOD)
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_EZ_DELETE_METHOD)
    void ezBatchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) Collection<EzDelete> deletes);

    /**
     * Delete records by SQL.
     *
     * @param sql   the SQL delete statement
     * @param param the SQL parameters
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_SQL_METHOD)
    Integer deleteBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Insert a single entity.
     * <p>
     * Note: This method can only insert a single entity object.
     * Map, Collection, or Array types are not supported.
     * </p>
     *
     * @param model the entity object to insert
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_METHOD)
    int insert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Insert a single entity into a specified table.
     * <p>
     * Note: This method can only insert a single entity object.
     * Map, Collection, or Array types are not supported.
     * </p>
     *
     * @param table the target table
     * @param model the entity object to insert
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_BY_TABLE_METHOD)
    int insertByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Batch insert entities.
     * <p>
     * For optimal performance, keep the total number of entity properties below
     * 5000 per batch.
     * </p>
     *
     * @param models the collection of entity objects to insert
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_METHOD)
    int batchInsert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Batch insert entities into a specified table.
     * <p>
     * For optimal performance, keep the total number of entity properties below
     * 5000 per batch.
     * </p>
     *
     * @param table  the target table
     * @param models the collection of entity objects to insert
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_BY_TABLE_METHOD)
    int batchInsertByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Insert records by SQL.
     *
     * @param sql   the SQL insert statement
     * @param param the SQL parameters
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_BY_SQL_METHOD)
    Integer insertBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Insert records into a specified table using EzQuery.
     *
     * @param table the target table
     * @param query the EzQuery object containing query conditions
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_BY_QUERY_METHOD)
    Integer insertByQuery(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                          @Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<?> query);

    /**
     * Update an entity, only updating non-null fields.
     *
     * @param model the entity object to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_METHOD)
    int update(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Update an entity in a specified table, only updating non-null fields.
     *
     * @param table the target table
     * @param model the entity object to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_TABLE_METHOD)
    int updateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Batch update entities, only updating non-null fields.
     *
     * @param models the collection of entity objects to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_METHOD)
    int batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Batch update entities in a specified table, only updating non-null fields.
     *
     * @param table  the target table
     * @param models the collection of entity objects to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_BY_TABLE_METHOD)
    int batchUpdateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Replace an entity, updating all fields.
     *
     * @param model the entity object to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD)
    int replace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Replace an entity in a specified table, updating all fields.
     *
     * @param table the target table
     * @param model the entity object to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD_BY_TABLE)
    int replaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                       @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Batch replace entities, updating all fields.
     *
     * @param models the collection of entity objects to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_METHOD)
    int batchReplace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Batch replace entities in a specified table, updating all fields.
     *
     * @param table  the target table
     * @param models the collection of entity objects to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_BY_TABLE_METHOD)
    int batchReplaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Delete an entity.
     *
     * @param model the entity object to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_METHOD)
    int delete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Delete an entity from a specified table.
     *
     * @param table the target table
     * @param model the entity object to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_METHOD)
    int deleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object model);

    /**
     * Batch delete entities.
     *
     * @param models the collection of entity objects to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_METHOD)
    int batchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Batch delete entities from a specified table.
     *
     * @param table  the target table
     * @param models the collection of entity objects to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_METHOD)
    int batchDeleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> models);

    /**
     * Delete an entity by primary key.
     *
     * @param <T>    the type of primary key, must extend Serializable
     * @param etType the entity class type
     * @param id     the primary key value
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_ID_METHOD)
    <T extends Serializable> int deleteById(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
                                            @Param(EzMybatisConstant.MAPPER_PARAM_ID) T id);

    /**
     * Delete an entity by primary key from a specified table.
     *
     * @param <T>    the type of primary key, must extend Serializable
     * @param table  the target table
     * @param etType the entity class type
     * @param id     the primary key value
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_AND_ID_METHOD)
    <T extends Serializable> int deleteByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                                                    @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
                                                    @Param(EzMybatisConstant.MAPPER_PARAM_ID) T id);

    /**
     * Batch delete entities by primary keys.
     *
     * @param <T>    the type of primary key, must extend Serializable
     * @param etType the entity class type
     * @param ids    the collection of primary key values
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_ID_METHOD)
    <T extends Serializable> int batchDeleteById(
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<T> ids);

    /**
     * Batch delete entities by primary keys from a specified table.
     *
     * @param <T>    the type of primary key, must extend Serializable
     * @param table  the target table
     * @param etType the entity class type
     * @param ids    the collection of primary key values
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_AND_ID_METHOD)
    <T extends Serializable> int batchDeleteByTableAndId(
            @Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<T> ids);
}
