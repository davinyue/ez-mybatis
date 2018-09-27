package org.linuxprobe.crud.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface UniversalMapper {
	/** 插入 */
	@InsertProvider(type = SqlGenerator.class, method = "getSql")
	int insert(String sql);

	/** 批量插入 */
	@InsertProvider(type = SqlGenerator.class, method = "getSql")
	int batchInsert(String sql);

	/** 删除 */
	@DeleteProvider(type = SqlGenerator.class, method = "getSql")
	int delete(String sql);

	/** 批量删除 */
	@DeleteProvider(type = SqlGenerator.class, method = "getSql")
	long batchDelete(String sql);

	/** 通用查询 */
	@SelectProvider(type = SqlGenerator.class, method = "getSql")
	public List<Map<String, Object>> universalSelect(String sql);

	/** 查询数量 */
	@SelectProvider(type = SqlGenerator.class, method = "getSql")
	public long selectCount(String sql);

	/** 更新 */
	@UpdateProvider(type = SqlGenerator.class, method = "getSql")
	int update(String sql);

	default String getSql(String sql) {
		return sql;
	}
}
