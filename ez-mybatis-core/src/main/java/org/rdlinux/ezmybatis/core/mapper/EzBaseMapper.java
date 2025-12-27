package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.*;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.provider.EzDeleteProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzInsertProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzSelectProvider;
import org.rdlinux.ezmybatis.core.mapper.provider.EzUpdateProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Base mapper interface providing type-safe CRUD operations for MyBatis.
 * <p>
 * This interface provides generic database operations with compile-time type
 * safety.
 * It supports common CRUD operations including insert, update, delete, and
 * query operations.
 * </p>
 *
 * @param <Nt> the entity type
 * @param <Pt> the primary key type, must extend Serializable
 */
public interface EzBaseMapper<Nt, Pt extends Serializable> {
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
    int insert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

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
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

    /**
     * Batch insert entities.
     *
     * @param models the collection of entity objects to insert
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_METHOD)
    int batchInsert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Batch insert entities into a specified table.
     *
     * @param table  the target table
     * @param models the collection of entity objects to insert
     * @return the number of affected rows
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_BY_TABLE_METHOD)
    int batchInsertByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Update an entity, only updating non-null fields.
     *
     * @param model the entity object to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_METHOD)
    int update(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

    /**
     * Update an entity in a specified table, only updating non-null fields.
     *
     * @param table the target table
     * @param model the entity object to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_TABLE_METHOD)
    int updateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

    /**
     * Batch update entities, only updating non-null fields.
     *
     * @param models the collection of entity objects to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_METHOD)
    int batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Batch update entities in a specified table, only updating non-null fields.
     *
     * @param table  the target table
     * @param models the collection of entity objects to update
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_BY_TABLE_METHOD)
    int batchUpdateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Replace an entity, updating all fields.
     *
     * @param model the entity object to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD)
    int replace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

    /**
     * Replace an entity in a specified table, updating all fields.
     *
     * @param table the target table
     * @param model the entity object to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD_BY_TABLE)
    int replaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                       @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

    /**
     * Batch replace entities, updating all fields.
     *
     * @param models the collection of entity objects to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_METHOD)
    int batchReplace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Batch replace entities in a specified table, updating all fields.
     *
     * @param table  the target table
     * @param models the collection of entity objects to replace
     * @return the number of affected rows
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_BY_TABLE_METHOD)
    int batchReplaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Delete an entity.
     *
     * @param model the entity object to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_METHOD)
    int delete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

    /**
     * Delete an entity from a specified table.
     *
     * @param table the target table
     * @param model the entity object to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_METHOD)
    int deleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt model);

    /**
     * Batch delete entities.
     *
     * @param models the collection of entity objects to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_METHOD)
    int batchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Batch delete entities from a specified table.
     *
     * @param table  the target table
     * @param models the collection of entity objects to delete
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_METHOD)
    int batchDeleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<Nt> models);

    /**
     * Delete an entity by primary key.
     *
     * @param id the primary key value
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_ID_METHOD)
    int deleteById(@Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * Delete an entity by primary key from a specified table.
     *
     * @param table the target table
     * @param id    the primary key value
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_AND_ID_METHOD)
    int deleteByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * Batch delete entities by primary keys.
     *
     * @param ids the collection of primary key values
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_ID_METHOD)
    int batchDeleteById(@Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * Batch delete entities by primary keys from a specified table.
     *
     * @param table the target table
     * @param ids   the collection of primary key values
     * @return the number of affected rows
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_AND_ID_METHOD)
    int batchDeleteByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                                @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * Query an entity by primary key.
     *
     * @param id the primary key value
     * @return the entity object, or null if not found
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_ID_METHOD)
    Nt selectById(@Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * Query an entity by primary key from a specified table.
     *
     * @param table the table to query from
     * @param id    the primary key value
     * @return the entity object, or null if not found
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_ID_METHOD)
    Nt selectByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                          @Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * Batch query entities by primary keys.
     *
     * @param ids the collection of primary key values
     * @return list of entity objects
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_IDS_METHOD)
    List<Nt> selectByIds(@Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * Batch query entities by primary keys from a specified table.
     *
     * @param table the table to query from
     * @param ids   the collection of primary key values
     * @return list of entity objects
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_IDS_METHOD)
    List<Nt> selectByTableAndIds(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                                 @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Pt> ids);

    /**
     * Query a single record by SQL.
     *
     * @param sql   the SQL query string
     * @param param the SQL parameters
     * @return the entity object, or null if not found
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    Nt selectOneBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                      @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Query multiple records by SQL.
     *
     * @param sql   the SQL query string
     * @param param the SQL parameters
     * @return list of entity objects
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    List<Nt> selectBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                         @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * Query records using EzQuery.
     *
     * @param query the EzQuery object containing query conditions
     * @return list of entity objects
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    List<Nt> query(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Nt> query);

    /**
     * Query a single record using EzQuery.
     *
     * @param query the EzQuery object containing query conditions
     * @return the entity object, or null if not found
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    Nt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Nt> query);

    /**
     * Query the count of records using EzQuery.
     *
     * @param query the EzQuery object containing query conditions
     * @return the count of matching records
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_COUNT_METHOD)
    int queryCount(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Nt> query);
}
