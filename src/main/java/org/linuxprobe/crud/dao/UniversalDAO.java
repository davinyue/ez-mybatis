package org.linuxprobe.crud.dao;

import java.util.List;

public interface UniversalDAO {
	/** 插入 */
	int insert(Object record);

	/** 批量插入 */
	int batchInsert(List<Object> records);

	/** 删除 */
	int delete(Object record);

	/** 批量删除 */
	long batchDelete(List<Object> records);

	/** 根据主键删除 */
	public int deleteByPrimaryKey(String id, Class<?> type);

	/** 根据主键批量删除 */
	public long batchDeleteByPrimaryKey(List<String> ids, Class<?> type);

	/** 通用查询,不支持关联加载，如果传入的对象的所属类没有继承BaseQuery,则不支持排序和分页 */
	public <T> List<T> universalSelect(Object param, Class<T> type);

	/** 查询数量 */
	public long selectCount(Object param);

	/** 增量更新 */
	int localUpdate(Object record);

	/** 全量更新 */
	int globalUpdate(Object record);
}
