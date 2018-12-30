package org.linuxprobe.crud.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.Page;

/**
 * @param <Model> 模型类型
 * @param <IdType> 主键类型
 * @param <Query> 查询类型
 */
public interface UniversalService<Model, IdType extends Serializable, Query extends BaseQuery> {
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
	public int removeByPrimaryKey(IdType id);

	/**
	 * 批量删除
	 * 
	 * @throws Exception
	 */
	public long batchRemoveByPrimaryKey(List<IdType> ids) throws Exception;

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

	/**
	 * 根据列名生成删除sql
	 * 
	 * @param columnName  列名
	 * @param columnValue 列值
	 */
	public int removeByColumnName(String columnName, Serializable columnValue);

	/**
	 * 根据列名生成删除sql，列名称与列值请一一对象
	 * 
	 * @param columnNames  列名
	 * @param columnValues 列值
	 */
	public int removeByColumnNames(String[] columnNames, Serializable[] columnValues);

	/**
	 * 根据类的成员名称生成删除sql
	 * 
	 * @param fieldName  属性名称
	 * @param fieldValue 属性值
	 */
	public int removeyByFieldName(String fieldName, Serializable fieldValue);

	/**
	 * 根据属性删除，属性名称与属性值请一一对应
	 * 
	 * @param fieldNames  属性名称
	 * @param fieldValues 属性值
	 */
	public int removeByFieldNames(String[] fieldNames, Serializable[] fieldValues);

	/** 根据主键查询 */
	public Model getByPrimaryKey(IdType id);

	/** 根据查询对象获取实体list */
	public List<Model> getByQueryParam(Query param);

	/** 根据查询对象获取实体数量 */
	public long getCountByQueryParam(Query param);

	/** 根据列作为条件查询数据 */
	public List<Model> getByColumn(String column, Serializable columnValue);

	/** 根据实体成员名称作为条件查询数据 */
	public List<Model> getByFiled(String fieldName, Serializable fieldValue);

	/** 根据列作为条件查询数据 */
	public Model getOneByColumn(String column, Serializable columnValue);

	/** 根据实体成员名称作为条件查询数据 */
	public Model getOneByFiled(String fieldName, Serializable fieldValue);

	/** 根据查询对象获取实体分页数据 */
	public Page<Model> getPageInfo(Query param);

	/** 根据sql获取数据 */
	public List<Map<String, Object>> getBySql(String sql);

	/** 根据sql获取唯一数据 */
	public Map<String, Object> getOneBySql(String sql);

	/** 根据sql获取数据 */
	public <T> List<T> getBySql(String sql, Class<T> type);

	/** 根据sql获取唯一数据 */
	public <T> T getOneBySql(String sql, Class<T> type);

	/** 增量更新 */
	public Model localUpdate(Model model);

	/** 全量更新 */
	public Model globalUpdate(Model model);
}
