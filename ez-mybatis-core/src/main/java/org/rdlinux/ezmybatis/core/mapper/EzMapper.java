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
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用mapper
 */
@Mapper
public interface EzMapper {
    String QUERY_METHOD = "query";
    String QUERY_ONE_METHOD = "queryOne";
    String QUERY_COUNT_METHOD = "queryCount";
    String SELECT_BY_ID_METHOD = "selectById";
    String SELECT_BY_TABLE_AND_ID_METHOD = "selectByTableAndId";
    String SELECT_BY_IDS_METHOD = "selectByIds";
    String SELECT_BY_TABLE_AND_IDS_METHOD = "selectByTableAndIds";
    String EZ_DELETE_METHOD = "ezDelete";
    String EZ_BATCH_DELETE_METHOD = "ezBatchDelete";
    String SELECT_ONE_OBJECT_BY_SQL_METHOD = "selectOneObjectBySql";
    String SELECT_OBJECT_BY_SQL_METHOD = "selectObjectBySql";

    /**
     * 根据主键查询
     */
    @MethodName(SELECT_BY_ID_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_ID_METHOD)
    <Id extends Serializable, NT> NT selectById(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
                                                @Param(EzMybatisConstant.MAPPER_PARAM_ID) Id id);

    /**
     * 根据主键查询
     */
    @MethodName(SELECT_BY_TABLE_AND_ID_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_ID_METHOD)
    <Id extends Serializable, NT> NT selectByTableAndId(
            @Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_ID) Id id);

    /**
     * 根据主键批量查询
     */
    @MethodName(SELECT_BY_IDS_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_IDS_METHOD)
    <Id extends Serializable, NT> List<NT> selectByIds(
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Id> ids);

    /**
     * 根据主键批量查询
     */
    @MethodName(SELECT_BY_TABLE_AND_IDS_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_TABLE_AND_IDS_METHOD)
    <Id extends Serializable, NT> List<NT> selectByTableAndIds(
            @Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<NT> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<Id> ids);

    /**
     * 根据sql查询一条数据并返回map
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    Map<String, Object> selectOneMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                          @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 根据sql查询多条数据, 并返回list map
     */
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    List<Map<String, Object>> selectMapBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                             @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 根据sql查询一条数据并返回对象
     */
    @MethodName(SELECT_ONE_OBJECT_BY_SQL_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    <T> T selectOneObjectBySql(@Param(EzMybatisConstant.MAPPER_PARAM_RET) Class<T> clazz,
                               @Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                               @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 根据sql查询数据并返回对象列表
     */
    @MethodName(SELECT_OBJECT_BY_SQL_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.SELECT_BY_SQL_METHOD)
    <T> List<T> selectObjectBySql(@Param(EzMybatisConstant.MAPPER_PARAM_RET) Class<T> clazz,
                                  @Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                                  @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    @MethodName(QUERY_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    <Rt> List<Rt> query(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    @MethodName(QUERY_ONE_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_METHOD)
    <Rt> Rt queryOne(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<Rt> query);

    /**
     * 根据ezQuery查询count
     */
    @MethodName(QUERY_COUNT_METHOD)
    @SelectProvider(type = EzSelectProvider.class, method = EzSelectProvider.QUERY_COUNT_METHOD)
    int queryCount(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzQuery<?> query);

    /**
     * 根据更新参数更新
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_EZ_UPDATE_METHOD)
    int ezUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzUpdate update);

    /**
     * 根据更新参数更新
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_BY_EZ_UPDATE_METHOD)
    void ezBatchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) Collection<EzUpdate> updates);

    /**
     * 根据sql更新
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_SQL_METHOD)
    Integer updateBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 批量删除
     */
    @MethodName(EZ_DELETE_METHOD)
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_EZ_DELETE_METHOD)
    int ezDelete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) EzDelete delete);

    /**
     * 批量删除
     */
    @MethodName(EZ_BATCH_DELETE_METHOD)
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_EZ_DELETE_METHOD)
    void ezBatchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_EZPARAM) Collection<EzDelete> deletes);

    /**
     * 根据sql删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_SQL_METHOD)
    Integer deleteBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 插入, 注意, 该接口仅能插入单条实体数据, 不能传入map或collection或array
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_METHOD)
    int insert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 插入,指定表, 注意, 该接口仅能插入单条实体数据, 不能传入map或collection或array
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_BY_TABLE_METHOD)
    int insertByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 批量插入
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_METHOD)
    int batchInsert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 批量插入, 指定表
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.BATCH_INSERT_BY_TABLE_METHOD)
    int batchInsertByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 根据sql插入记录
     */
    @InsertProvider(type = EzInsertProvider.class, method = EzInsertProvider.INSERT_BY_SQL_METHOD)
    Integer insertBySql(@Param(EzMybatisConstant.MAPPER_PARAM_SQL) String sql,
                        @Param(EzMybatisConstant.MAPPER_PARAM_SQLPARAM) Map<String, Object> param);

    /**
     * 更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_METHOD)
    int update(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.UPDATE_BY_TABLE_METHOD)
    int updateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 批量更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_METHOD)
    int batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 批量更新, 只更新非空字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_UPDATE_BY_TABLE_METHOD)
    int batchUpdateByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD)
    int replace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.REPLACE_METHOD_BY_TABLE)
    int replaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                       @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 批量更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_METHOD)
    int batchReplace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 批量更新, 更新所有字段
     */
    @UpdateProvider(type = EzUpdateProvider.class, method = EzUpdateProvider.BATCH_REPLACE_BY_TABLE_METHOD)
    int batchReplaceByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_METHOD)
    int delete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_METHOD)
    int deleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                      @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Object entity);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_METHOD)
    int batchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_METHOD)
    int batchDeleteByTable(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                           @Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) Collection<?> entitys);

    /**
     * 根据主键删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_ID_METHOD)
    <T extends Serializable> int deleteById(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
                                            @Param(EzMybatisConstant.MAPPER_PARAM_ID) T id);

    /**
     * 根据主键删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.DELETE_BY_TABLE_AND_ID_METHOD)
    <T extends Serializable> int deleteByTableAndId(@Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
                                                    @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
                                                    @Param(EzMybatisConstant.MAPPER_PARAM_ID) T id);

    /**
     * 根据主键批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_ID_METHOD)
    <T extends Serializable> int batchDeleteById(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
                                                 @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<T> ids);

    /**
     * 根据主键批量删除
     */
    @DeleteProvider(type = EzDeleteProvider.class, method = EzDeleteProvider.BATCH_DELETE_BY_TABLE_AND_ID_METHOD)
    <T extends Serializable> int batchDeleteByTableAndId(
            @Param(EzMybatisConstant.MAPPER_PARAM_TABLE) Table table,
            @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> etType,
            @Param(EzMybatisConstant.MAPPER_PARAM_IDS) Collection<T> ids);
}
