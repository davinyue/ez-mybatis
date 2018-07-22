package org.linuxprobe.crud.service.impl;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.linuxprobe.crud.mapper.BaseMapper;
import org.linuxprobe.crud.model.BaseModel;
import org.linuxprobe.crud.query.BaseQuery;
import org.linuxprobe.crud.service.BaseService;
import org.linuxprobe.crud.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @param <Model>
 *            模型
 */
public class BaseServiceImpl<Model extends BaseModel, QueryDTO extends BaseQuery>
		implements BaseService<Model, QueryDTO> {
	@Autowired
	private GeneralService service;
	@Autowired
	private BaseMapper<Model> mapper;

	@Override
	@Transactional
	public Model add(Model model) {
		this.service.add(model);
		return model;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Model> batchAdd(List<Model> models) throws Exception {
		return (List<Model>) this.service.batchAdd((List<BaseModel>) models);
	}

	@Override
	@Transactional
	public int removeByPrimaryKey(String id) throws Exception {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		@SuppressWarnings("unchecked")
		Class<BaseModel> modelClass = (Class<BaseModel>) Class.forName(type.getTypeName());
		return this.service.remove(id, modelClass);
	}

	@Override
	@Transactional
	public long batchRemoveByPrimaryKey(List<String> ids) throws Exception {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		@SuppressWarnings("unchecked")
		Class<BaseModel> modelClass = (Class<BaseModel>) Class.forName(type.getTypeName());
		return this.service.batchRemove(ids, modelClass);
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
	@SuppressWarnings("unchecked")
	public List<Model> generalGetByQueryParam(QueryDTO param) throws IOException {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<BaseModel> modelClass = null;
		try {
			modelClass = (Class<BaseModel>) Class.forName(type.getTypeName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<BaseModel> result = this.service.select(param, modelClass);
		return (List<Model>) result;
	}

	@Override
	@Transactional
	public int localUpdate(Model model) {
		return this.service.localUpdate(model);
	}
}
