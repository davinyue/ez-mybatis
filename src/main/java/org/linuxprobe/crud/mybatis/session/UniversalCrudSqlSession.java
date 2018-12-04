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

	public int batchDelete(Collection<Object> records);
}
