package org.linuxprobe.crud.mybatis.session.defaults;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.linuxprobe.crud.core.annoatation.PrimaryKey.Strategy;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.InsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSession;
import org.linuxprobe.crud.utils.EntityUtils;
import org.linuxprobe.crud.utils.FieldUtil;

public class UniversalCrudDefaultSqlSession extends DefaultSqlSession implements UniversalCrudSqlSession {
	private static final String selectStatement = "org.linuxprobe.crud.mapper.UniversalMapper.universalSelect";
	private static final String selectOneStatement = "org.linuxprobe.crud.mapper.UniversalMapper.universalSelectOne";
	private static final String selectCountStatement = "org.linuxprobe.crud.mapper.UniversalMapper.selectCount";
	private static final String insertStatement = "org.linuxprobe.crud.mapper.UniversalMapper.insert";
	private static final String deleteStatement = "org.linuxprobe.crud.mapper.UniversalMapper.delete";
	private static final String updateStatement = "org.linuxprobe.crud.mapper.UniversalMapper.update";

	public UniversalCrudDefaultSqlSession(Configuration configuration, Executor executor) {
		super(configuration, executor);
	}

	public UniversalCrudDefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
		super(configuration, executor, autoCommit);
	}

	@Override
	public int insert(Object record) {
		InsertSqlGenerator insertSqlGenerator = UniversalCrudContent.getInsertSqlGenerator();
		int result = super.insert(insertStatement, insertSqlGenerator.toInsertSql(record));
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(record.getClass());
		if (entityInfo.getPrimaryKey().getPrimaryKey().value().equals(Strategy.NATIVE)) {
			Object idValue = FieldUtil.getFieldValue(record, entityInfo.getPrimaryKey().getField());
			if (idValue == null) {
				Map<String, Object> idMap = super.selectOne(selectOneStatement, "SELECT LAST_INSERT_ID() as id");
				try {
					Number id = (Number) idMap.get("id");
					if (entityInfo.getPrimaryKey().getField().getType().equals(Long.class)) {
						id = id.longValue();
					} else if (entityInfo.getPrimaryKey().getField().getType().equals(Integer.class)) {
						id = id.intValue();
					} else if (entityInfo.getPrimaryKey().getField().getType().equals(Short.class)) {
						id = id.shortValue();
					}
					FieldUtil.setField(record, entityInfo.getPrimaryKey().getField(), id);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		return result;
	}

	@Override
	public <T> int batchInsert(Collection<T> records) {
		if (records == null || records.isEmpty()) {
			return 0;
		} else {
			int result = 0;
			for (T record : records) {
				this.insert(record);
				result++;
			}
			return result;
		}
	}

	@Override
	public int deleteByPrimaryKey(Serializable id, Class<?> entityType) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return super.delete(deleteStatement, deleteSqlGenerator.toDeleteSqlByPrimaryKey(id, entityType));
	}

	@Override
	public int batchDeleteByPrimaryKey(Collection<Serializable> ids, Class<?> entityType) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return super.delete(deleteStatement, deleteSqlGenerator.toBatchDeleteSqlByPrimaryKey(ids, entityType));
	}

	@Override
	public int delete(Object record) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return super.delete(deleteStatement, deleteSqlGenerator.toDeleteSql(record));
	}

	@Override
	public int batchDelete(Collection<?> records) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return super.delete(deleteStatement, deleteSqlGenerator.toBatchDeleteSql(records));
	}

	@Override
	public List<Map<String, Object>> universalSelect(BaseQuery param) {
		String sql = UniversalCrudContent.getSelectSqlGenerator().toSelectSql(param);
		List<Map<String, Object>> reslut = super.selectList(selectStatement, sql);
		return reslut;
	}

	@Override
	public <T> List<T> universalSelect(BaseQuery param, Class<T> type) {
		List<Map<String, Object>> mapperResults = this.universalSelect(param);
		List<T> records = new LinkedList<>();
		for (Map<String, Object> mapperResult : mapperResults) {
			T model = null;
			try {
				model = type.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
			Set<String> columns = mapperResult.keySet();
			for (String column : columns) {
				try {
					EntityUtils.setField(model, column, mapperResult.get(column));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			records.add(model);
		}
		return records;
	}

	@Override
	public long selectCount(BaseQuery param) {
		SelectSqlGenerator selectSqlGenerator = UniversalCrudContent.getSelectSqlGenerator();
		return super.selectOne(selectCountStatement, selectSqlGenerator.toSelectCountSql(param));
	}

	@Override
	public List<Map<String, Object>> selectBySql(String sql) {
		List<Map<String, Object>> reslut = super.selectList(selectStatement, sql);
		return reslut;
	}

	@Override
	public Map<String, Object> selectOneBySql(String sql) {
		List<Map<String, Object>> mapResult = this.selectBySql(sql);
		if (mapResult != null && !mapResult.isEmpty()) {
			return mapResult.get(0);
		} else
			return null;
	}

	@Override
	public <T> List<T> selectBySql(String sql, Class<T> type) {
		List<Map<String, Object>> mapResult = this.selectBySql(sql);
		List<T> records = new LinkedList<>();
		for (Map<String, Object> mapperResult : mapResult) {
			T model = null;
			try {
				model = type.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException(type.getName() + "没有无参构造函数", e);
			}
			Set<String> columns = mapperResult.keySet();
			for (String column : columns) {
				try {
					EntityUtils.setField(model, column, mapperResult.get(column));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			records.add(model);
		}
		return records;
	}

	@Override
	public <T> T selectOneBySql(String sql, Class<T> type) {
		List<T> records = this.selectBySql(sql, type);
		if (records != null && !records.isEmpty()) {
			return records.get(0);
		} else
			return null;
	}

	@Override
	public int globalUpdate(Object record) {
		return super.update(updateStatement, UniversalCrudContent.getUpdateSqlGenerator().toGlobalUpdateSql(record));
	}

	@Override
	public int localUpdate(Object record) {
		return super.update(updateStatement, UniversalCrudContent.getUpdateSqlGenerator().toLocalUpdateSql(record));
	}
}
