package org.linuxprobe.crud.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;
import org.linuxprobe.crud.mapper.BaseMapper;
import org.linuxprobe.crud.service.BaseService;
import org.linuxprobe.crud.service.UniversalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @param <Model>
 *            模型
 */
public class BaseServiceImpl<Model, QueryDTO extends BaseQuery> implements BaseService<Model, QueryDTO> {
	@Autowired
	private UniversalService service;
	@Autowired
	private BaseMapper<Model> mapper;

	@Override
	@Transactional
	public Model save(Model model) {
		this.service.save(model);
		return model;
	}

	@Override
	@Transactional
	public List<Model> batchSave(List<Model> models) {
		return this.service.batchSave(models);
	}

	@Override
	@Transactional
	public int removeByPrimaryKey(String id) {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> modelClass = null;
		try {
			modelClass = Class.forName(type.getTypeName());
		} catch (ClassNotFoundException e) {
		}
		return this.service.removeByPrimaryKey(id, modelClass);
	}

	@Override
	@Transactional
	public long batchRemoveByPrimaryKey(List<String> ids) throws Exception {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> modelClass = Class.forName(type.getTypeName());
		return this.service.batchRemoveByPrimaryKey(ids, modelClass);
	}

	@Override
	@Transactional
	public int remove(Model record) {
		return this.service.remove(record);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public long batchRemove(List<Model> records) {
		return this.service.batchRemove((List<Object>) (List<?>) records);
	}

	@Override
	@Transactional
	public int globalUpdate(Model model) {
		return this.service.globalUpdate(model);
	}

	@Override
	public Model getByPrimaryKey(String id) {
		return this.mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Model> getByQueryParam(QueryDTO param) {
		return this.mapper.select(param);
	}

	@Override
	public long getCountByQueryParam(QueryDTO param) {
		return this.service.selectCount(param);
	}

	@Override
	public Page<Model> getPageInfo(QueryDTO param) {
		Page<Model> result = new Page<Model>();
		result.setCurrentPage(param.getLimit().getCurrentPage());
		result.setPageSize(param.getLimit().getPageSize());
		result.setData(this.getByQueryParam(param));
		result.setTotal(this.getCountByQueryParam(param));
		return result;
	}

	@Override
	public List<Map<String, Object>> getBySql(String sql) {
		return this.service.selectBySql(sql);
	}

	@Override
	public Map<String, Object> getUniqueResultBySql(String sql) {
		return this.service.selectUniqueResultBySql(sql);
	}

	@Override
	public <T> List<T> getBySql(String sql, Class<T> type) {
		return this.service.selectBySql(sql, type);
	}

	@Override
	public <T> T getUniqueResultBySql(String sql, Class<T> type) {
		return this.service.selectUniqueResultBySql(sql, type);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Model> universalGetByQueryParam(QueryDTO param) {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> modelClass = null;
		try {
			modelClass = Class.forName(type.getTypeName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		List<?> result = this.service.universalSelect(param, modelClass);
		return (List<Model>) result;
	}

	@Override
	@Transactional
	public int localUpdate(Model model) {
		return this.service.localUpdate(model);
	}
}
