package org.linuxprobe.crud.service;

import java.io.IOException;
import java.util.List;
import org.linuxprobe.crud.model.BaseModel;
import org.linuxprobe.crud.query.BaseQuery;

/**
 * @param <Model>
 *            模型
 */
public interface BaseService<Model extends BaseModel, QueryDTO extends BaseQuery> {
	/** 添加 */
	public Model add(Model model);

	/** 批量添加 
	 * @throws Exception */
	public List<Model> batchAdd(List<Model> models) throws Exception;

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public int removeByPrimaryKey(String id) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @throws Exception
	 */
	public long batchRemoveByPrimaryKey(List<String> ids) throws Exception;

	/** 根据查询对象获取实体数量 */
	public long getCountByQueryParam(QueryDTO param);
	
	/** 通用查询，根据查询对象获取实体list，不能处理关联加载 
	 * @throws IOException */
	public List<Model> generalGetByQueryParam(QueryDTO param) throws IOException;

	/** 增量更新 */
	public int localUpdate(Model model);

	/** 全量更新 */
	public int globalUpdate(Model model);
}
