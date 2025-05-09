package org.rdlinux.ezmybatis.service.impl;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.dao.EzDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.dto.DcDTO;
import org.rdlinux.ezmybatis.service.EzService;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础service
 *
 * @param <MdType> 实体类型
 * @param <PkType> 主键类型
 */
public abstract class EzServiceImpl<MdType, PkType extends Serializable> implements EzService<MdType, PkType> {
    /**
     * 实体类型
     */
    private final Class<?> modelClass;
    @Resource
    protected EzMapper ezMapper;
    @Resource
    protected EzDao ezDao;

    public EzServiceImpl() {
        this.modelClass = ReflectionUtils.getGenericSuperclass(this.getClass(), 0);
    }


    @Override
    public List<MdType> query(EzQuery<MdType> param) {
        Assert.notNull(param, "param can not be null");
        return this.ezDao.query(param);
    }

    @Override
    public int queryCount(EzQuery<MdType> param) {
        Assert.notNull(param, "param can not be null");
        return this.ezDao.queryCount(param);
    }

    @Override
    public <RetType> DcDTO<RetType> queryDataAndCount(EzQuery<RetType> param) {
        return this.ezDao.queryDataAndCount(param);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public MdType getById(PkType id) {
        Assert.notNull(id, "id can not be null");
        return (MdType) this.ezDao.selectById(this.modelClass, id);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByIds(Collection<PkType> ids) {
        Assert.notEmpty(ids, "ids can not be null");
        return (List<MdType>) this.ezDao.selectByIds(this.modelClass, ids);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByField(String field, Object value) {
        return (List<MdType>) this.ezDao.selectByField(this.modelClass, EntityTable.of(this.modelClass), field, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public MdType getOneByField(String field, Object value) {
        return (MdType) this.ezDao.selectOneByField(this.modelClass, EntityTable.of(this.modelClass), field, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByColumn(String column, Object value) {
        return (List<MdType>) this.ezDao.selectByColumn(this.modelClass, EntityTable.of(this.modelClass), column, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public MdType getOneByColumn(String column, Object value) {
        return (MdType) this.ezDao.selectOneByColumn(this.modelClass, EntityTable.of(this.modelClass), column, value);
    }

    @Override
    public int update(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezDao.update(model);
    }

    @Override
    public int batchUpdate(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezDao.batchUpdate(models);
    }

    @Override
    public int replace(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezDao.replace(model);
    }

    @Override
    public int batchReplace(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezDao.batchReplace(models);
    }

    @Override
    public int deleteById(PkType id) {
        Assert.notNull(id, "id can not be null");
        return this.ezDao.deleteById(this.modelClass, id);
    }

    @Override
    public int deleteByIds(Collection<PkType> ids) {
        Assert.notEmpty(ids, "ids can not be null");
        return this.ezDao.batchDeleteById(this.modelClass, ids);
    }

    @Override
    public int deleteByField(String field, Object value) {
        return this.ezDao.deleteByField(EntityTable.of(this.modelClass), field, value);
    }

    @Override
    public int deleteByColumn(String column, Object value) {
        return this.ezDao.deleteByColumn(EntityTable.of(this.modelClass), column, value);
    }

    @Override
    public int delete(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezDao.delete(model);
    }

    @Override
    public int batchDelete(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezDao.batchDelete(models);
    }

    @Override
    public int save(MdType model) {
        Assert.notNull(model, "model can not be empty");
        return this.ezDao.insert(model);
    }

    @Override
    public int batchSave(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezDao.batchInsert(models);
    }
}
