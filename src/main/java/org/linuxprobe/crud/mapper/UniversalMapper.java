package org.linuxprobe.crud.mapper;

import java.util.List;
import java.util.Map;

public interface UniversalMapper {
	/** 插入 */
	int insert(String sql);

	/** 批量插入 */
	int batchInsert(String sql);

	/** 删除 */
	int delete(String sql);

	/** 批量删除 */
	long batchDelete(String sql);

	/** 通用查询 */
	public List<Map<String, Object>> universalSelect(String sql);

	/** 查询数量 */
	public long selectCount(String sql);

	/** 更新 */
	int update(String sql);
}
