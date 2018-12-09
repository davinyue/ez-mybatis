package org.linuxprobe.crud.mybatis.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.linuxprobe.crud.core.query.BaseQuery;

public interface UniversalCrudSqlSession extends SqlSession {
	public List<Map<String, Object>> universalSelect(BaseQuery param);

	public <T> List<T> universalSelect(BaseQuery param, Class<T> type);
	
	public long selectCount(BaseQuery param);

	public int insert(Object record);

	public <T> int batchInsert(Collection<T> records);

	public int deleteByPrimaryKey(Serializable id, Class<?> entityType);

	public int batchDeleteByPrimaryKey(Collection<Serializable> ids, Class<?> entityType);

	public int delete(Object record);

	public int batchDelete(Collection<?> records);
	
	/** 根据sql查询数据 */
	public List<Map<String, Object>> selectBySql(String sql);

	/** 根据sql查询唯一数据 */
	public Map<String, Object> selectOneBySql(String sql);

	/** 根据sql查询数据 */
	public <T> List<T> selectBySql(String sql, Class<T> type);

	/** 根据sql查询唯一数据 */
	public <T> T selectOneBySql(String sql, Class<T> type);
}
