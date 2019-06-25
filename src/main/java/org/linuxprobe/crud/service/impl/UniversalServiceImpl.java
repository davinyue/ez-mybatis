package org.linuxprobe.crud.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;
import org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionTemplate;
import org.linuxprobe.crud.service.UniversalService;
import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

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
		return ReflectionUtils.getGenericSuperclass(this.getClass(), 0);
	}

	/** 获取当前对象的代理对象,用代理对象调用代替this调用,可解决内部调用切面无效问题 */
	@SuppressWarnings("unchecked")
	public UniversalService<Model, IdType, Query> getProxy() {
		if (proxy == null) {
			proxy = (UniversalService<Model, IdType, Query>) AopContext.currentProxy();
		}
		return proxy;
	}

	/** 获取当前对象的代理对象,用代理对象调用代替this调用,可解决内部调用切面无效问题 */
	@SuppressWarnings("unchecked")
	public <T> T getProxy2() {
		if (proxy == null) {
			proxy = (UniversalService<Model, IdType, Query>) AopContext.currentProxy();
		}
		return (T) proxy;
	}

	@Override
	@Transactional
	public Model save(Model model) {
		return this.sqlSessionTemplate.insert(model);
	}

	@Override
	@Transactional
	public List<Model> batchSave(List<Model> models) {
		return this.sqlSessionTemplate.batchInsert(models);
	}

	@Override
	@Transactional
	public int removeByPrimaryKey(IdType id) {
		Class<?> modelClass = getModelCalss();
		return this.sqlSessionTemplate.deleteByPrimaryKey(id, modelClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public long batchRemoveByPrimaryKey(List<IdType> ids) throws Exception {
		Class<?> modelClass = getModelCalss();
		return this.sqlSessionTemplate.batchDeleteByPrimaryKey((Collection<Serializable>) ids, modelClass);
	}

	@Override
	@Transactional
	public int remove(Model record) {
		return this.sqlSessionTemplate.delete(record);
	}

	@Override
	@Transactional
	public int batchRemove(List<Model> records) {
		return this.sqlSessionTemplate.batchDelete(records);
	}

	@Override
	@Transactional
	public int removeByColumnName(String columnName, Serializable columnValue) {
		return this.sqlSessionTemplate.deleteByColumnName(columnName, columnValue, this.getModelCalss());
	}

	@Override
	@Transactional
	public int removeByColumnNames(String[] columnNames, Serializable[] columnValues) {
		return this.sqlSessionTemplate.deleteByColumnNames(columnNames, columnValues, this.getModelCalss());
	}

	@Override
	@Transactional
	public int removeyByFieldName(String fieldName, Serializable fieldValue) {
		return this.sqlSessionTemplate.deleteByFieldName(fieldName, fieldValue, this.getModelCalss());
	}

	@Override
	@Transactional
	public int removeByFieldNames(String[] fieldNames, Serializable[] fieldValues) {
		return this.sqlSessionTemplate.deleteByFieldNames(fieldNames, fieldValues, this.getModelCalss());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Model getByPrimaryKey(IdType id) {
		Class<?> modelClass = getModelCalss();
		Model model = (Model) this.sqlSessionTemplate.selectByPrimaryKey(id, modelClass);
		return model;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Model> getByQueryParam(Query param) {
		return (List<Model>) sqlSessionTemplate.universalSelect(param);
	}

	@Override
	public long getCountByQueryParam(Query param) {
		return this.sqlSessionTemplate.selectCount(param);
	}

	@Override
	public Page<Model> getPageInfo(Query param) {
		Page<Model> result = new Page<Model>();
		result.setCurrentPage(param.getLimit().getCurrentPage());
		result.setPageSize(param.getLimit().getPageSize());
		result.setData(this.getProxy().getByQueryParam(param));
		result.setTotal(this.getProxy().getCountByQueryParam(param));
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Model> getByColumn(String column, Serializable columnValue) {
		return (List<Model>) this.sqlSessionTemplate.selectByColumn(column, columnValue, this.getModelCalss());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Model> getByFiled(String fieldName, Serializable fieldValue) {
		return (List<Model>) this.sqlSessionTemplate.selectByField(fieldName, fieldValue, this.getModelCalss());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Model getOneByColumn(String column, Serializable columnValue) {
		return (Model) this.sqlSessionTemplate.selectOneByColumn(column, columnValue, this.getModelCalss());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Model getOneByFiled(String fieldName, Serializable fieldValue) {
		return (Model) this.sqlSessionTemplate.selectOneByField(fieldName, fieldValue, this.getModelCalss());
	}

	@Override
	@Transactional
	public Model globalUpdate(Model model) {
		return this.sqlSessionTemplate.globalUpdate(model);
	}

	@Override
	@Transactional
	public Model localUpdate(Model model) {
		return this.sqlSessionTemplate.localUpdate(model);
	}
}
