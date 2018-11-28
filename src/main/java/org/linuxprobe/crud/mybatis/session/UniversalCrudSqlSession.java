package org.linuxprobe.crud.mybatis.session;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.linuxprobe.crud.core.query.BaseQuery;

public interface UniversalCrudSqlSession extends SqlSession {
	public List<Map<String, Object>> universalSelect(BaseQuery param);

	public <T> List<T> universalSelect(BaseQuery param, Class<T> type);
	
	public int insert(Object record);
}
