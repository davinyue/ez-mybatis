package org.linuxprobe.crud.service;

import java.util.List;
import org.linuxprobe.crud.query.BaseQuery;

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

	public <T> List<T> universalSelect(BaseQuery param, Class<T> type);

	public long selectCount(BaseQuery param);

	/** 增量更新 */
	public int localUpdate(Object record);

	/** 全量更新 */
	public int globalUpdate(Object record);
}
