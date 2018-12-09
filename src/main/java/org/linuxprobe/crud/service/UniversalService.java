package org.linuxprobe.crud.service;

import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.query.BaseQuery;

public interface UniversalService {
	/** 添加 */
	public <T> T save(T record);

	/**
	 * 批量添加
	 * 
	 * @throws Exception
	 */
	public <T> List<T> batchSave(List<T> records);

	public int remove(Object record);

	public long batchRemove(List<?> records);

	public int removeByPrimaryKey(String id, Class<?> type);

	public long batchRemoveByPrimaryKey(List<String> ids, Class<?> type);

	/** 通用查询,不支持关联加载，如果传入的对象的所属类没有继承BaseQuery,则不支持排序和分页 */
	public <T> List<T> universalSelect(BaseQuery param, Class<T> type);

	public long selectCount(BaseQuery param);

	/** 根据sql查询数据 */
	public List<Map<String, Object>> selectBySql(String sql);

	/** 根据sql查询唯一数据 */
	public Map<String, Object> selectUniqueResultBySql(String sql);

	/** 根据sql查询数据 */
	public <T> List<T> selectBySql(String sql, Class<T> type);

	/** 根据sql查询唯一数据 */
	public <T> T selectUniqueResultBySql(String sql, Class<T> type);

	/** 增量更新 */
	public int localUpdate(Object record);

	/** 全量更新 */
	public int globalUpdate(Object record);
}
