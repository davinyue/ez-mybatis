package org.rdlinux.ezmybatis.service.impl;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
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
    @Resource
    protected EzMapper ezMapper;
    /**
     * 实体类型
     */
    private Class<?> modelClass;

    public EzServiceImpl() {
        this.modelClass = ReflectionUtils.getGenericSuperclass(this.getClass(), 0);
    }


    @Override
    public List<MdType> query(EzQuery<MdType> param) {
        Assert.notNull(param, "param can not be null");
        return this.ezMapper.query(param);
    }

    @Override
    public int queryCount(EzQuery<MdType> param) {
        Assert.notNull(param, "param can not be null");
        return this.ezMapper.queryCount(param);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public MdType getById(PkType id) {
        Assert.notNull(id, "id can not be null");
        return (MdType) this.ezMapper.selectById(this.modelClass, id);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByIds(Collection<PkType> ids) {
        Assert.notEmpty(ids, "ids can not be null");
        return (List<MdType>) this.ezMapper.selectByIds(this.modelClass, ids);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByField(String field, Object value) {
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(this.modelClass).from(EntityTable.of(this.modelClass))
                .select().addAll().done()
                .where()
                .addFieldCondition(field, value).done().build();
        return (List<MdType>) this.ezMapper.query(query);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public MdType getOneByField(String field, Object value) {
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        Class<?> modelClass = this.modelClass;
        EzQuery<?> query = EzQuery.builder(modelClass).from(EntityTable.of(modelClass))
                .select().addAll().done()
                .where()
                .addFieldCondition(field, value).done()
                .page(1, 1).build();
        return (MdType) this.ezMapper.queryOne(query);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByColumn(String column, Object value) {
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(this.modelClass).from(EntityTable.of(this.modelClass))
                .select().addAll().done()
                .where()
                .addColumnCondition(column, value).done().build();
        return (List<MdType>) this.ezMapper.queryOne(query);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public MdType getOneByColumn(String column, Object value) {
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(this.modelClass).from(EntityTable.of(this.modelClass))
                .select().addAll().done()
                .where()
                .addColumnCondition(column, value).done()
                .page(1, 1).build();
        return (MdType) this.ezMapper.queryOne(query);
    }

    @Override
    public int update(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.update(model);
    }

    @Override
    public int batchUpdate(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchUpdate(models);
    }

    @Override
    public int replace(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.replace(model);
    }

    @Override
    public int replace(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchReplace(models);
    }

    @Override
    public int deleteById(PkType id) {
        Assert.notNull(id, "id can not be null");
        return this.ezMapper.deleteById(this.modelClass, id);
    }

    @Override
    public int deleteByIds(Collection<PkType> ids) {
        Assert.notEmpty(ids, "ids can not be null");
        return this.ezMapper.batchDeleteById(this.modelClass, ids);
    }

    @Override
    public int deleteByField(String field, Object value) {
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        EzDelete delete = EzDelete.delete(EntityTable.of(this.modelClass))
                .where().addFieldCondition(field, value).done().build();
        return this.ezMapper.ezDelete(delete);
    }

    @Override
    public int deleteByColumn(String column, Object value) {
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzDelete delete = EzDelete.delete(EntityTable.of(this.modelClass))
                .where().addColumnCondition(column, value).done().build();
        return this.ezMapper.ezDelete(delete);
    }

    @Override
    public int delete(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.delete(model);
    }

    @Override
    public int batchDelete(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchDelete(models);
    }

    @Override
    public int save(MdType model) {
        Assert.notNull(model, "model can not be empty");
        return this.ezMapper.insert(model);
    }

    @Override
    public int batchSave(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchInsert(models);
    }
}
