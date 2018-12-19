package org.linuxprobe.crud.mybatis.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.query.BaseQuery;

public interface SqlSessionExtend {
	/** 根据查询对象查询符合条件的数据 */
	public <T> List<T> universalSelect(BaseQuery param);

	/** 查询符合条件的记录数 */
	public long selectCount(BaseQuery param);

	/** 根据主键查询数据 */
	public <T> T selectByPrimaryKey(Serializable id, Class<T> type);

	/** 插入 */
	public int insert(Object record);

	/** 批量插入 */
	public <T> int batchInsert(Collection<T> records);

	/** 根据主键删除 */
	public int deleteByPrimaryKey(Serializable id, Class<?> entityType);

	/** 根据主键批量删除 */
	public int batchDeleteByPrimaryKey(Collection<Serializable> ids, Class<?> entityType);

	/** 删除 */
	public int delete(Object record);

	/** 批量删除 */
	public int batchDelete(Collection<?> records);

	/** 根据sql查询数据 */
	public List<Map<String, Object>> selectBySql(String sql);

	/** 根据sql查询唯一数据 */
	public Map<String, Object> selectOneBySql(String sql);

	/** 根据sql查询数据 */
	public <T> List<T> selectBySql(String sql, Class<T> type);

	/** 根据sql查询唯一数据 */
	public <T> T selectOneBySql(String sql, Class<T> type);

	/** 全字段更新 */
	public int globalUpdate(Object record);

	/** 非空字段更新 */
	public int localUpdate(Object record);
}
