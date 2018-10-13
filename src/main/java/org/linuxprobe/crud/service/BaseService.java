package org.linuxprobe.crud.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;

/**
 * @param <Model>
 *            模型
 */
public interface BaseService<Model, QueryDTO extends BaseQuery> {
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
	public int removeByPrimaryKey(String id);

	/**
	 * 批量删除
	 * 
	 * @throws Exception
	 */
	public long batchRemoveByPrimaryKey(List<String> ids) throws Exception;

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
	public long batchRemove(List<Model> records);

	/** 根据主键查询 */
	public Model getByPrimaryKey(String id);

	/** 根据查询对象获取实体list */
	public List<Model> getByQueryParam(QueryDTO param);

	/** 根据查询对象获取实体数量 */
	public long getCountByQueryParam(QueryDTO param);

	/** 根据查询对象获取实体分页数据 */
	public Page<Model> getPageInfo(QueryDTO param);
	
	/** 根据sql获取数据 */
	public List<Map<String, Object>> getBySql(String sql);
	
	/** 根据sql获取唯一数据 */
	public Map<String, Object> getUniqueResultBySql(String sql);

	/** 根据sql获取数据 */
	public <T> List<T> getBySql(String sql, Class<T> type);
	
	/** 根据sql获取唯一数据 */
	public <T> T getUniqueResultBySql(String sql, Class<T> type);

	/**
	 * 通用查询，根据查询对象获取实体list，不能处理关联加载
	 * 
	 * @throws IOException
	 */
	public List<Model> universalGetByQueryParam(QueryDTO param);

	/** 增量更新 */
	public int localUpdate(Model model);

	/** 全量更新 */
	public int globalUpdate(Model model);
}
