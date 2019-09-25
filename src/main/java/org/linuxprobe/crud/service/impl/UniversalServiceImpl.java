package org.linuxprobe.crud.service.impl;

import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;
import org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionTemplate;
import org.linuxprobe.crud.service.UniversalService;
import org.linuxprobe.luava.reflection.ReflectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @param <Model>  模型类型
 * @param <IdType> 主键类型
 * @param <Query>  查询类型
 */
public class UniversalServiceImpl<Model, IdType extends Serializable, Query extends BaseQuery>
        implements UniversalService<Model, IdType, Query> {
    public UniversalServiceImpl(UniversalCrudSqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public UniversalCrudSqlSessionTemplate sqlSessionTemplate;

    private UniversalService<Model, IdType, Query> proxy;

    private Class<?> getModelCalss() {
        return UniversalCrudContent.getEntityInfo(ReflectionUtils.getGenericSuperclass(this.getClass(), 0).getName()).getEntityType();
    }

    @Override
    public Model save(Model model) {
        return this.sqlSessionTemplate.insert(model);
    }

    @Override
    public List<Model> batchSave(List<Model> models) {
        return this.sqlSessionTemplate.batchInsert(models);
    }

    @Override
    public int removeByPrimaryKey(IdType id) {
        Class<?> modelClass = this.getModelCalss();
        return this.sqlSessionTemplate.deleteByPrimaryKey(id, modelClass);
    }

    @Override
    public long batchRemoveByPrimaryKey(List<IdType> ids) throws Exception {
        Class<?> modelClass = this.getModelCalss();
        return this.sqlSessionTemplate.batchDeleteByPrimaryKey((Collection<Serializable>) ids, modelClass);
    }

    @Override
    public int remove(Model record) {
        return this.sqlSessionTemplate.delete(record);
    }

    @Override
    public int batchRemove(List<Model> records) {
        return this.sqlSessionTemplate.batchDelete(records);
    }

    @Override
    public int removeByColumnName(String columnName, Serializable columnValue) {
        return this.sqlSessionTemplate.deleteByColumnName(columnName, columnValue, this.getModelCalss());
    }

    @Override
    public int removeByColumnNames(String[] columnNames, Serializable[] columnValues) {
        return this.sqlSessionTemplate.deleteByColumnNames(columnNames, columnValues, this.getModelCalss());
    }

    @Override
    public int removeyByFieldName(String fieldName, Serializable fieldValue) {
        return this.sqlSessionTemplate.deleteByFieldName(fieldName, fieldValue, this.getModelCalss());
    }

    @Override
    public int removeByFieldNames(String[] fieldNames, Serializable[] fieldValues) {
        return this.sqlSessionTemplate.deleteByFieldNames(fieldNames, fieldValues, this.getModelCalss());
    }

    @Override
    public Model getByPrimaryKey(IdType id) {
        Class<?> modelClass = this.getModelCalss();
        Model model = (Model) this.sqlSessionTemplate.selectByPrimaryKey(id, modelClass);
        return model;
    }

    @Override
    public List<Model> getByQueryParam(Query param) {
        return (List<Model>) this.sqlSessionTemplate.universalSelect(param);
    }

    @Override
    public long getCountByQueryParam(Query param) {
        return this.sqlSessionTemplate.selectCount(param);
    }

    @Override
    public Page<Model> getPageInfo(Query param) {
        Page<Model> result = new Page<>();
        if (param.getLimit() != null) {
            result.setCurrentPage(param.getLimit().getCurrentPage());
            result.setPageSize(param.getLimit().getPageSize());
        }
        result.setData(this.getByQueryParam(param));
        result.setTotal(this.getCountByQueryParam(param));
        return result;
    }

    @Override
    public List<Map<String, Object>> getBySql(String sql) {
        return this.sqlSessionTemplate.selectBySql(sql);
    }

    @Override
    public Map<String, Object> getOneBySql(String sql) {
        return this.sqlSessionTemplate.selectOneBySql(sql);
    }

    @Override
    public <T> List<T> getBySql(String sql, Class<T> type) {
        return this.sqlSessionTemplate.selectBySql(sql, type);
    }

    @Override
    public <T> T getOneBySql(String sql, Class<T> type) {
        return this.sqlSessionTemplate.selectOneBySql(sql, type);
    }

    @Override
    public List<Model> getByColumn(String column, Serializable columnValue) {
        return (List<Model>) this.sqlSessionTemplate.selectByColumn(column, columnValue, this.getModelCalss());
    }

    @Override
    public List<Model> getByFiled(String fieldName, Serializable fieldValue) {
        return (List<Model>) this.sqlSessionTemplate.selectByField(fieldName, fieldValue, this.getModelCalss());
    }

    @Override
    public Model getOneByColumn(String column, Serializable columnValue) {
        return (Model) this.sqlSessionTemplate.selectOneByColumn(column, columnValue, this.getModelCalss());
    }

    @Override
    public Model getOneByFiled(String fieldName, Serializable fieldValue) {
        return (Model) this.sqlSessionTemplate.selectOneByField(fieldName, fieldValue, this.getModelCalss());
    }

    @Override
    public Model globalUpdate(Model model) {
        return this.sqlSessionTemplate.globalUpdate(model);
    }

    @Override
    public Model localUpdate(Model model) {
        return this.sqlSessionTemplate.localUpdate(model);
    }
}
