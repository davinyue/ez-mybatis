package org.linuxprobe.crud.mapper;

import java.util.List;

import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.model.BaseModel;

/**
 * @param <Model>
 *            模型
 */
public interface BaseMapper<Model extends BaseModel> {
	/**
	 * 根据查询对象查询
	 * 
	 * @param param
	 *            param类型必须继承QueryDTO
	 */
	<QueryDTO extends BaseQuery> List<Model> select(QueryDTO param);

	/** 根据主键查询 */
	Model selectByPrimaryKey(String id);
}