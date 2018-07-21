package org.linuxprobe.crud.mapper;

import java.util.List;
import java.util.Map;
import org.linuxprobe.crud.model.BaseModel;
import org.linuxprobe.crud.query.BaseQuery;

/** 通用mapper */
public interface GeneralMapper {
	/** 插入 */
	int insert(BaseModel record);

	/** 批量插入 */
	int batchInsert(String sql);

	/** 删除 */
	int deleteByPrimaryKey(String sql);

	/** 批量删除 */
	long batchDeleteByPrimaryKey(String sql);

	/** 查询 */
	public List<Map<String, Object>> select(BaseQuery param);

	/** 查询数量 */
	public long selectCount(BaseQuery param);

	/** 增量更新 */
	int localUpdate(BaseModel record);

	/** 全量更新 */
	int globalUpdate(BaseModel record);

}
