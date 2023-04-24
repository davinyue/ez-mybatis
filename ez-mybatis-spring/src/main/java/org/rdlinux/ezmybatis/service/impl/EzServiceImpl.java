package org.rdlinux.ezmybatis.service.impl;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.dao.JdbcBatchInsertDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
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
    @Resource
    protected JdbcBatchInsertDao jdbcBatchInsertDao;
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

    @SuppressWarnings({"unchecked"})
    @Override
    public MdType getById(Table table, PkType id) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(id, "id can not be null");
        return (MdType) this.ezMapper.selectByTableAndId(table, this.modelClass, id);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByIds(Collection<PkType> ids) {
        Assert.notEmpty(ids, "ids can not be null");
        return (List<MdType>) this.ezMapper.selectByIds(this.modelClass, ids);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByIds(Table table, Collection<PkType> ids) {
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(ids, "ids can not be null");
        return (List<MdType>) this.ezMapper.selectByTableAndIds(table, this.modelClass, ids);
    }

    @Override
    public List<MdType> getByField(String field, Object value) {
        return this.getByField(EntityTable.of(this.modelClass), field, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByField(Table table, String field, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(this.modelClass).from(table)
                .select().addAll().done()
                .where()
                .addFieldCondition(field, value).done().build();
        return (List<MdType>) this.ezMapper.query(query);
    }

    @Override
    public MdType getOneByField(String field, Object value) {
        return this.getOneByField(EntityTable.of(this.modelClass), field, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public MdType getOneByField(Table table, String field, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        Class<?> modelClass = this.modelClass;
        EzQuery<?> query = EzQuery.builder(modelClass).from(table)
                .select().addAll().done()
                .where()
                .addFieldCondition(field, value).done()
                .page(1, 1).build();
        return (MdType) this.ezMapper.queryOne(query);
    }

    @Override
    public List<MdType> getByColumn(String column, Object value) {
        return this.getByColumn(EntityTable.of(this.modelClass), column, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<MdType> getByColumn(Table table, String column, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(this.modelClass).from(table)
                .select().addAll().done()
                .where()
                .addColumnCondition(column, value).done().build();
        return (List<MdType>) this.ezMapper.queryOne(query);
    }

    @Override
    public MdType getOneByColumn(String column, Object value) {
        return this.getOneByColumn(EntityTable.of(this.modelClass), column, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public MdType getOneByColumn(Table table, String column, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzQuery<?> query = EzQuery.builder(this.modelClass).from(table)
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
    public int update(Table table, MdType model) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.updateByTable(table, model);
    }

    @Override
    public int batchUpdate(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchUpdate(models);
    }

    @Override
    public int batchUpdate(Table table, Collection<MdType> models) {
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchUpdateByTable(table, models);
    }

    @Override
    public int replace(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.replace(model);
    }

    @Override
    public int replace(Table table, MdType model) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.replaceByTable(table, model);
    }

    @Override
    public int batchReplace(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchReplace(models);
    }

    @Override
    public int batchReplace(Table table, Collection<MdType> models) {
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchReplaceByTable(table, models);
    }

    @Override
    public int deleteById(PkType id) {
        Assert.notNull(id, "id can not be null");
        return this.ezMapper.deleteById(this.modelClass, id);
    }

    @Override
    public int deleteById(Table table, PkType id) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(id, "id can not be null");
        return this.ezMapper.deleteByTableAndId(table, this.modelClass, id);
    }

    @Override
    public int deleteByIds(Collection<PkType> ids) {
        Assert.notEmpty(ids, "ids can not be null");
        return this.ezMapper.batchDeleteById(this.modelClass, ids);
    }

    @Override
    public int deleteByIds(Table table, Collection<PkType> ids) {
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(ids, "ids can not be null");
        return this.ezMapper.batchDeleteByTableAndId(table, this.modelClass, ids);
    }

    @Override
    public int deleteByField(String field, Object value) {
        return this.deleteByField(EntityTable.of(this.modelClass), field, value);
    }

    @Override
    public int deleteByField(Table table, String field, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        Assert.notNull(value, "value can not be null");
        EzDelete delete = EzDelete.delete(table)
                .where().addFieldCondition(field, value).done().build();
        return this.ezMapper.ezDelete(delete);
    }

    @Override
    public int deleteByColumn(String column, Object value) {
        return this.deleteByColumn(EntityTable.of(this.modelClass), column, value);
    }

    @Override
    public int deleteByColumn(Table table, String column, Object value) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        Assert.notNull(value, "value can not be null");
        EzDelete delete = EzDelete.delete(table)
                .where().addColumnCondition(column, value).done().build();
        return this.ezMapper.ezDelete(delete);
    }

    @Override
    public int delete(MdType model) {
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.delete(model);
    }

    @Override
    public int delete(Table table, MdType model) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(model, "model can not be null");
        return this.ezMapper.deleteByTable(table, model);
    }

    @Override
    public int batchDelete(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchDelete(models);
    }

    @Override
    public int batchDelete(Table table, Collection<MdType> models) {
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchDeleteByTable(table, models);
    }

    @Override
    public int save(MdType model) {
        Assert.notNull(model, "model can not be empty");
        return this.ezMapper.insert(model);
    }

    @Override
    public int save(Table table, MdType model) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(model, "model can not be empty");
        return this.ezMapper.insertByTable(table, model);
    }

    @Override
    public int batchSave(Collection<MdType> models) {
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchInsert(models);
    }

    @Override
    public int batchSave(Table table, Collection<MdType> models) {
        Assert.notNull(table, "table can not be null");
        Assert.notEmpty(models, "models can not be empty");
        return this.ezMapper.batchInsertByTable(table, models);
    }
}
