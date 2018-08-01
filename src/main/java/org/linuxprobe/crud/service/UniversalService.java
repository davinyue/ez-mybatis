package org.linuxprobe.crud.service;

import java.util.List;

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

	public long batchRemove(List<Object> records);

	public int removeByPrimaryKey(String id, Class<?> type);

	public long batchRemoveByPrimaryKey(List<String> ids, Class<?> type);

	/** 通用查询,不支持关联加载，如果传入的对象的所属类没有继承BaseQuery,则不支持排序和分页 */
	public <T> List<T> universalSelect(Object param, Class<T> type);

	public long selectCount(Object param);

	/** 增量更新 */
	public int localUpdate(Object record);

	/** 全量更新 */
	public int globalUpdate(Object record);
}
