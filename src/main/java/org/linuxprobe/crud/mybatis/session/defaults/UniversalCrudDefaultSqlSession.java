package org.linuxprobe.crud.mybatis.session.defaults;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSession;

public class UniversalCrudDefaultSqlSession extends DefaultSqlSession implements UniversalCrudSqlSession {
	private final UniversalCrudDefaultSqlSessionExtend sqlSessionExtend;

	public UniversalCrudDefaultSqlSession(Configuration configuration, Executor executor) {
		super(configuration, executor);
		this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
	}

	public UniversalCrudDefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
		super(configuration, executor, autoCommit);
		this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
	}

	@Override
	public int insert(Object record) {
		return sqlSessionExtend.insert(record);
	}

	@Override
	public <T> int batchInsert(Collection<T> records) {
		return sqlSessionExtend.batchInsert(records);
	}

	@Override
	public int deleteByPrimaryKey(Serializable id, Class<?> entityType) {
		return sqlSessionExtend.deleteByPrimaryKey(id, entityType);
	}

	@Override
	public int batchDeleteByPrimaryKey(Collection<Serializable> ids, Class<?> entityType) {
		return sqlSessionExtend.batchDeleteByPrimaryKey(ids, entityType);
	}

	@Override
	public int delete(Object record) {
		return sqlSessionExtend.delete(record);
	}

	@Override
	public int batchDelete(Collection<?> records) {
		return sqlSessionExtend.batchDelete(records);
	}

	@Override
	public <T> List<T> universalSelect(BaseQuery param) {
		return sqlSessionExtend.universalSelect(param);
	}

	@Override
	public long selectCount(BaseQuery param) {
		return sqlSessionExtend.selectCount(param);
	}

	@Override
	public List<Map<String, Object>> selectBySql(String sql) {
		return sqlSessionExtend.selectBySql(sql);
	}

	@Override
	public Map<String, Object> selectOneBySql(String sql) {
		return sqlSessionExtend.selectOneBySql(sql);
	}

	@Override
	public <T> List<T> selectBySql(String sql, Class<T> type) {
		return sqlSessionExtend.selectBySql(sql, type);
	}

	@Override
	public <T> T selectOneBySql(String sql, Class<T> type) {
		return sqlSessionExtend.selectOneBySql(sql, type);
	}

	@Override
	public int globalUpdate(Object record) {
		return sqlSessionExtend.globalUpdate(record);
	}

	@Override
	public int localUpdate(Object record) {
		return sqlSessionExtend.localUpdate(record);
	}

	@Override
	public <T> T selectByPrimaryKey(Serializable id, Class<T> type) {
		return sqlSessionExtend.selectByPrimaryKey(id, type);
	}
}
