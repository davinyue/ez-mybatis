package org.rdlinux.ezmybatis.service;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.dto.DcDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础service
 *
 * @param <MdType> 实体类型
 * @param <PkType> 主键类型
 */
public interface EzService<MdType, PkType extends Serializable> {
    /**
     * 根据条件查询数据
     *
     * @param param 查询参数
     */
    List<MdType> query(EzQuery<MdType> param);

    /**
     * 根据条件查询总数
     *
     * @param param 查询参数
     */
    int queryCount(EzQuery<MdType> param);

    /**
     * 根据条件数据和总数
     *
     * @param param 查询参数
     */
    <RetType> DcDTO<RetType> queryDataAndCount(EzQuery<RetType> param);

    /**
     * 根据id查询
     *
     * @param id 主键
     */
    MdType getById(PkType id);
    
    /**
     * 根据多个id查询
     *
     * @param ids 主键集合
     */
    List<MdType> getByIds(Collection<PkType> ids);

    /**
     * 根据实体属性查询
     *
     * @param field 实体属性
     * @param value 属性值
     */
    List<MdType> getByField(String field, Object value);

    /**
     * 根据实体属性查询一条记录
     *
     * @param field 实体属性
     * @param value 属性值
     */
    MdType getOneByField(String field, Object value);

    /**
     * 根据列明查询
     *
     * @param column 表列
     * @param value  列值
     */
    List<MdType> getByColumn(String column, Object value);

    /**
     * 根据列明查询一条记录
     *
     * @param column 表列
     * @param value  列值
     */
    MdType getOneByColumn(String column, Object value);

    /**
     * 更新, 只会更新非空字段
     */
    int update(MdType model);

    /**
     * 批量更新, 只会更新非空字段
     */
    int batchUpdate(Collection<MdType> models);

    /**
     * 替换, 更新全部字段
     */
    int replace(MdType model);

    /**
     * 批量替换, 更新全部字段
     */
    int batchReplace(Collection<MdType> models);

    /**
     * 根据id删除
     */
    int deleteById(PkType id);

    /**
     * 根据id批量删除
     */
    int deleteByIds(Collection<PkType> ids);

    /**
     * 根据属性删除
     *
     * @param field 实体属性
     * @param value 属性值
     */
    int deleteByField(String field, Object value);

    /**
     * 根据列删除
     *
     * @param column 表列
     * @param value  列值
     */
    int deleteByColumn(String column, Object value);

    /**
     * 删除
     *
     * @param model 要删除的实体
     */
    int delete(MdType model);

    /**
     * 批量删除
     *
     * @param models 要删除的实体集合
     */
    int batchDelete(Collection<MdType> models);

    /**
     * 保存
     *
     * @param model 要保存的实体
     */
    int save(MdType model);

    /**
     * 批量保存
     *
     * @param models 要保存的实体集合
     */
    int batchSave(Collection<MdType> models);
}
