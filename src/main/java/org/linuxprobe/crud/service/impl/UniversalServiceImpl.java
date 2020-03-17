package org.linuxprobe.crud.service.impl;

import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;
import org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionTemplate;
import org.linuxprobe.crud.service.UniversalService;
import org.linuxprobe.crud.service.UniversalServiceEventListener;
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
    public UniversalCrudSqlSessionTemplate sqlSessionTemplate;
    private UniversalServiceEventListener eventListener;
    private ClassLoader classLoader;

    public UniversalServiceImpl(UniversalCrudSqlSessionTemplate sqlSessionTemplate) {
        this(sqlSessionTemplate, null);
    }

    public UniversalServiceImpl(UniversalCrudSqlSessionTemplate sqlSessionTemplate, UniversalServiceEventListener eventListener) {
//        获取当前执行方法
//        Method thisMethod = new Object() {
//        }.getClass().getEnclosingMethod();
        if (eventListener == null) {
            eventListener = new UniversalServiceEventListener() {
            };
        }
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.eventListener = eventListener;
        this.classLoader = this.getClass().getClassLoader();
    }

    private Class<?> getModelCalss() {
        Class<?> entityType = UniversalCrudContent.getEntityInfo(ReflectionUtils.getGenericSuperclass(this.getClass(), 0).getName()).getEntityType();
        try {
            entityType = Class.forName(entityType.getName(), true, this.classLoader);
        } catch (ClassNotFoundException ignored) {
        }
        return entityType;
    }

    @Override
    public Model save(Model model) {
        Model result = null;
        if (this.eventListener.beforeSave(this, model)) {
            result = this.sqlSessionTemplate.insert(model, this.classLoader);
            result = this.eventListener.afterSave(this, model, result);
        }
        return result;
    }

    @Override
    public List<Model> batchSave(List<Model> models, boolean loop) {
        List<Model> result = null;
        if (this.eventListener.beforeBatchSave(this, models, loop)) {
            result = this.sqlSessionTemplate.batchInsert(models, loop, this.classLoader);
            result = this.eventListener.afterBatchSave(this, models, result, loop);
        }
        return result;
    }

    @Override
    public int removeByPrimaryKey(IdType id) {
        int result = 0;
        Class<?> modelClass = this.getModelCalss();
        if (this.eventListener.beforeRemoveByPrimaryKey(this, modelClass, id)) {
            result = this.sqlSessionTemplate.deleteByPrimaryKey(id, modelClass);
            result = this.eventListener.afterRemoveByPrimaryKey(this, modelClass, id, result);
        }
        return result;
    }

    @Override
    public long batchRemoveByPrimaryKey(List<IdType> ids) throws Exception {
        long result = 0;
        Class<?> modelClass = this.getModelCalss();
        if (this.eventListener.beforeBatchRemoveByPrimaryKey(this, modelClass, ids)) {
            result = this.sqlSessionTemplate.batchDeleteByPrimaryKey((Collection<Serializable>) ids, modelClass);
            result = this.eventListener.afterBatchRemoveByPrimaryKey(this, modelClass, ids, result);
        }
        return result;
    }

    @Override
    public int remove(Model record) {
        int result = 0;
        if (this.eventListener.beforeRemove(this, record)) {
            result = this.sqlSessionTemplate.delete(record);
            result = this.eventListener.afterRemove(this, record, result);
        }
        return result;
    }

    @Override
    public int batchRemove(List<Model> records) {
        int result = 0;
        if (this.eventListener.beforeBatchRemove(this, records)) {
            result = this.sqlSessionTemplate.batchDelete(records);
            result = this.eventListener.afterBatchRemove(this, records, result);
        }
        return result;
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
        Model model = null;
        Class<?> modelClass = this.getModelCalss();
        if (this.eventListener.beforeGetByPrimaryKey(this, modelClass, id)) {
            model = (Model) this.sqlSessionTemplate.selectByPrimaryKey(id, modelClass, this.classLoader);
            model = this.eventListener.afterGetByPrimaryKey(this, modelClass, id, model);
        }
        return model;
    }

    @Override
    public List<Model> getByQueryParam(Query param) {
        List<Model> result = null;
        if (this.eventListener.beforeGetByQueryParam(this, param)) {
            result = this.sqlSessionTemplate.universalSelect(param, this.classLoader);
            result = this.eventListener.afterGetByQueryParam(this, param, result);
        }
        return result;
    }

    @Override
    public long getCountByQueryParam(Query param) {
        long result = 0;
        if (this.eventListener.beforeGetCountByQueryParam(this, param)) {
            result = this.sqlSessionTemplate.selectCount(param);
            result = this.eventListener.afterGetCountByQueryParam(this, param, result);
        }
        return result;
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
        return this.sqlSessionTemplate.selectBySql(sql, type, this.classLoader);
    }

    @Override
    public <T> T getOneBySql(String sql, Class<T> type) {
        return this.sqlSessionTemplate.selectOneBySql(sql, type, this.classLoader);
    }

    @Override
    public List<Model> getByColumn(String column, Serializable columnValue) {
        return (List<Model>) this.sqlSessionTemplate.selectByColumn(column, columnValue, this.getModelCalss(), this.classLoader);
    }

    @Override
    public List<Model> getByFiled(String fieldName, Serializable fieldValue) {
        return (List<Model>) this.sqlSessionTemplate.selectByField(fieldName, fieldValue, this.getModelCalss(), this.classLoader);
    }

    @Override
    public Model getOneByColumn(String column, Serializable columnValue) {
        return (Model) this.sqlSessionTemplate.selectOneByColumn(column, columnValue, this.getModelCalss(), this.classLoader);
    }

    @Override
    public Model getOneByFiled(String fieldName, Serializable fieldValue) {
        return (Model) this.sqlSessionTemplate.selectOneByField(fieldName, fieldValue, this.getModelCalss(), this.classLoader);
    }

    @Override
    public Model globalUpdate(Model model) {
        Model result = null;
        if (this.eventListener.beforeGlobalUpdate(this, model)) {
            result = this.sqlSessionTemplate.globalUpdate(model, this.classLoader);
            result = this.eventListener.afterGlobalUpdate(this, model, result);
        }
        return result;
    }

    @Override
    public Model localUpdate(Model model) {
        Model result = null;
        if (this.eventListener.beforeLocalUpdate(this, model)) {
            result = this.sqlSessionTemplate.localUpdate(model, this.classLoader);
            result = this.eventListener.afterLocalUpdate(this, model, result);
        }
        return result;
    }
}
