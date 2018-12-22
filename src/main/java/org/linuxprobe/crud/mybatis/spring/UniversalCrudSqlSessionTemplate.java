package org.linuxprobe.crud.mybatis.spring;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSession;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSessionFactory;
import org.linuxprobe.crud.mybatis.session.defaults.UniversalCrudDefaultSqlSessionExtend;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.PersistenceExceptionTranslator;

public class UniversalCrudSqlSessionTemplate extends SqlSessionTemplate implements UniversalCrudSqlSession {
	// private SqlSession sqlSession;
	private final UniversalCrudDefaultSqlSessionExtend sqlSessionExtend;

	public UniversalCrudSqlSessionTemplate(UniversalCrudSqlSessionFactory sqlSessionFactory, ExecutorType executorType,
			PersistenceExceptionTranslator exceptionTranslator) {
		super(sqlSessionFactory, executorType, exceptionTranslator);
		// initSqlSession();
		this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
	}

	public UniversalCrudSqlSessionTemplate(UniversalCrudSqlSessionFactory sqlSessionFactory,
			ExecutorType executorType) {
		super(sqlSessionFactory, executorType);
		// initSqlSession();
		this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
	}

	public UniversalCrudSqlSessionTemplate(UniversalCrudSqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
		// initSqlSession();
		this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
	}

//	private void initSqlSession() {
//		try {
//			Field sqlSessionProxyFiels = SqlSessionTemplate.class.getDeclaredField("sqlSessionProxy");
//			sqlSessionProxyFiels.setAccessible(true);
//			sqlSession = (SqlSession) sqlSessionProxyFiels.get(this);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	@Override
	public <T> T insert(T record) {
		return sqlSessionExtend.insert(record);
	}

	@Override
	public <T> List<T> batchInsert(Collection<T> records) {
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
	public <T> T globalUpdate(T record) {
		return sqlSessionExtend.globalUpdate(record);
	}

	@Override
	public <T> T localUpdate(T record) {
		return sqlSessionExtend.localUpdate(record);
	}

	@Override
	public <T> T selectByPrimaryKey(Serializable id, Class<T> type) {
		return sqlSessionExtend.selectByPrimaryKey(id, type);
	}

	@Override
	public <T> List<T> selectByColumn(String column, Serializable columnValue, Class<T> type) {
		return sqlSessionExtend.selectByColumn(column, columnValue, type);
	}

	@Override
	public <T> List<T> selectByField(String fieldName, Serializable fieldValue, Class<T> type) {
		return sqlSessionExtend.selectByField(fieldName, fieldValue, type);
	}

	@Override
	public <T> T selectOneByColumn(String column, Serializable columnValue, Class<T> type) {
		return this.sqlSessionExtend.selectOneByColumn(column, columnValue, type);
	}

	@Override
	public <T> T selectOneByField(String fieldName, Serializable fieldValue, Class<T> type) {
		return this.sqlSessionExtend.selectOneByField(fieldName, fieldValue, type);
	}
}
