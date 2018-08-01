package org.linuxprobe.crud.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.linuxprobe.crud.persistence.Sqlr;

public interface UniversalMapper {
	/** 插入 */
	int insert(@Param("entity") Object entity, @Param("sqlr") Sqlr sqlr);

	/** 批量插入 */
	int batchInsert(@Param("entitys") List<Object> entitys, @Param("sqlr") Sqlr sqlr);

	/** 删除 */
	int delete(@Param("entity") Object entity, @Param("sqlr") Sqlr sqlr);

	/** 批量删除 */
	long batchDelete(@Param("entitys") List<Object> entitys, @Param("sqlr") Sqlr sqlr);

	/** 删除 */
	int deleteByPrimaryKey(String sql);

	/** 批量删除 */
	long batchDeleteByPrimaryKey(String sql);

	/** 通用查询 */
	public List<Map<String, Object>> universalSelect(Object param);

	/** 查询数量 */
	public long selectCount(Object param);

	/** 增量更新 */
	int localUpdate(@Param("entity") Object record, @Param("sqlr") Sqlr sqlr);

	/** 全量更新 */
	int globalUpdate(@Param("entity") Object record, @Param("sqlr") Sqlr sqlr);
}
