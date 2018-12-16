package org.linuxprobe.crud.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;

/**
 * @param <Model> 模型
 */
public interface UniversalService<Model, Query extends BaseQuery> {
	/** 添加 */
	public Model save(Model model);

	/**
	 * 批量添加
	 * 
	 * @throws Exception
	 */
	public List<Model> batchSave(List<Model> models);

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public <T extends Serializable> int removeByPrimaryKey(T id);

	/**
	 * 批量删除
	 * 
	 * @throws Exception
	 */
	public <T extends Serializable> long batchRemoveByPrimaryKey(List<T> ids) throws Exception;

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public int remove(Model record);

	/**
	 * 批量删除
	 * 
	 * @throws Exception
	 */
	public int batchRemove(List<Model> records);

	/** 根据主键查询 */
	public <T extends Serializable> Model getByPrimaryKey(T id);

	/** 根据查询对象获取实体list */
	public List<Model> getByQueryParam(Query param);

	/** 根据查询对象获取实体数量 */
	public long getCountByQueryParam(Query param);

	/** 根据查询对象获取实体分页数据 */
	public Page<Model> getPageInfo(Query param);

	/** 根据sql获取数据 */
	public List<Map<String, Object>> getBySql(String sql);

	/** 根据sql获取唯一数据 */
	public Map<String, Object> getUniqueResultBySql(String sql);

	/** 根据sql获取数据 */
	public <T> List<T> getBySql(String sql, Class<T> type);

	/** 根据sql获取唯一数据 */
	public <T> T getUniqueResultBySql(String sql, Class<T> type);

	/** 增量更新 */
	public int localUpdate(Model model);

	/** 全量更新 */
	public int globalUpdate(Model model);
}
