package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.*;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;

import java.io.Serializable;
import java.util.List;

/**
 * 基础mapper
 *
 * @param <Nt> 实体类型
 * @param <Pt> 主键类型
 */
public interface EzBaseMapper<Nt, Pt extends Serializable> {
    /**
     * 插入
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int insert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量插入
     */
    @InsertProvider(type = EzEntityBatchInsertProvider.class, method = "generate")
    int batchInsert(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) List<Nt> entitys);

    /**
     * 更新, 只更新非空字段
     */
    @UpdateProvider(type = EzEntityInsertProvider.class, method = "generate")
    int update(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量更新, 只更新非空字段
     */
    @DeleteProvider(type = EzEntityInsertProvider.class, method = "generate")
    int batchUpdate(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) List<Nt> entitys);

    /**
     * 更新, 更新所有字段
     */
    @UpdateProvider(type = EzEntityInsertProvider.class, method = "generate")
    int replace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量更新, 更新所有字段
     */
    @DeleteProvider(type = EzEntityInsertProvider.class, method = "generate")
    int batchReplace(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) List<Nt> entitys);

    /**
     * 删除
     */
    @DeleteProvider(type = EzEntityInsertProvider.class, method = "generate")
    int delete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITY) Nt entity);

    /**
     * 批量删除
     */
    @DeleteProvider(type = EzEntityInsertProvider.class, method = "generate")
    int batchDelete(@Param(EzMybatisConstant.MAPPER_PARAM_ENTITYS) List<Nt> entitys);

    /**
     * 根据主键删除
     */
    @DeleteProvider(type = EzEntityInsertProvider.class, method = "generate")
    int deleteByPrimaryKey(@Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * 根据主键批量删除
     */
    @DeleteProvider(type = EzEntityInsertProvider.class, method = "generate")
    int deleteByPrimaryKeys(@Param(EzMybatisConstant.MAPPER_PARAM_IDS) List<Pt> ids);

    /**
     * 根据主键查询
     */
    @SelectProvider(type = EzEntitySelectProvider.class, method = "selectById")
    Nt selectById(@Param(EzMybatisConstant.MAPPER_PARAM_ID) Pt id);

    /**
     * 根据主键批量查询
     */
    @SelectProvider(type = EzEntitySelectProvider.class, method = "selectByIds")
    List<Nt> selectByIds(@Param(EzMybatisConstant.MAPPER_PARAM_IDS) List<Pt> ids);
}
