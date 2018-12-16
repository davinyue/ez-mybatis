package org.linuxprobe.crud.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;
import org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionTemplate;
import org.linuxprobe.crud.service.UniversalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @param <Model> 模型
 */
public class UniversalServiceImpl<Model, Query extends BaseQuery> implements UniversalService<Model, Query> {
	@Autowired
	private UniversalCrudSqlSessionTemplate sqlSessionTemplate;

	private Class<?> getModeCalss() {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> modelClass = null;
		try {
			modelClass = Class.forName(type.getTypeName());
		} catch (ClassNotFoundException e) {
		}
		return modelClass;
	}

	@SuppressWarnings("unused")
	private Class<?> getQueryCalss() {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		Class<?> querylClass = null;
		try {
			querylClass = Class.forName(type.getTypeName());
		} catch (ClassNotFoundException e) {
		}
		return querylClass;
	}

	@Override
	@Transactional
	public Model save(Model model) {
		this.sqlSessionTemplate.insert(model);
		return model;
	}

	@Override
	@Transactional
	public List<Model> batchSave(List<Model> models) {
		this.sqlSessionTemplate.batchInsert(models);
		return models;
	}

	@Override
	@Transactional
	public <T extends Serializable> int removeByPrimaryKey(T id) {
		Class<?> modelClass = getModeCalss();
		return this.sqlSessionTemplate.deleteByPrimaryKey(id, modelClass);
	}

	@Override
	@Transactional
	public <T extends Serializable> long batchRemoveByPrimaryKey(List<T> ids) throws Exception {
		Class<?> modelClass = getModeCalss();
		return this.sqlSessionTemplate.batchDeleteByPrimaryKey(ids, modelClass);
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
	public int globalUpdate(Model model) {
		return this.sqlSessionTemplate.globalUpdate(model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> Model getByPrimaryKey(T id) {
		Class<?> modelClass = getModeCalss();
		return (Model) this.sqlSessionTemplate.selectByPrimaryKey(id, modelClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Model> getByQueryParam(Query param) {
		Class<?> modelClass = getModeCalss();
		return (List<Model>) sqlSessionTemplate.universalSelect(param, modelClass);
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
		result.setData(this.getByQueryParam(param));
		result.setTotal(this.getCountByQueryParam(param));
		return result;
	}

	@Override
	public List<Map<String, Object>> getBySql(String sql) {
		return this.sqlSessionTemplate.selectBySql(sql);
	}

	@Override
	public Map<String, Object> getUniqueResultBySql(String sql) {
		return this.sqlSessionTemplate.selectOneBySql(sql);
	}

	@Override
	public <T> List<T> getBySql(String sql, Class<T> type) {
		return this.sqlSessionTemplate.selectBySql(sql, type);
	}

	@Override
	public <T> T getUniqueResultBySql(String sql, Class<T> type) {
		return this.sqlSessionTemplate.selectOneBySql(sql, type);
	}

	@Override
	@Transactional
	public int localUpdate(Model model) {
		return this.sqlSessionTemplate.localUpdate(model);
	}
}
