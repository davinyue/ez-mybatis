package org.linuxprobe.crud.mybatis.session.defaults;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.sql.generator.InsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSession;
import org.linuxprobe.crud.utils.EntityUtils;

public class UniversalCrudDefaultSqlSession extends DefaultSqlSession implements UniversalCrudSqlSession {
	private static final String selectStatement = "org.linuxprobe.crud.mapper.UniversalMapper.universalSelect";
	private static final String selectOneStatement = "org.linuxprobe.crud.mapper.UniversalMapper.universalSelectOne";
	private static final String insertStatement = "org.linuxprobe.crud.mapper.UniversalMapper.insert";

	public UniversalCrudDefaultSqlSession(Configuration configuration, Executor executor) {
		super(configuration, executor);
	}

	public UniversalCrudDefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
		super(configuration, executor, autoCommit);
	}

	@Override
	public List<Map<String, Object>> universalSelect(BaseQuery param) {

		String sql = SelectSqlGenerator.toSelectSql(param);
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
	public int insert(Object record) {
		int result = super.insert(insertStatement, InsertSqlGenerator.toInsertSql(record));
		Object i= super.selectOne(selectOneStatement, "SELECT LAST_INSERT_ID() as id");
		System.out.println(i.getClass()+"   "+i);
		return result;
	}
}
