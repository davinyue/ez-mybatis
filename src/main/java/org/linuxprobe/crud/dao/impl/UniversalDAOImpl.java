package org.linuxprobe.crud.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.dao.UniversalDAO;
import org.linuxprobe.crud.mapper.UniversalMapper;
import org.linuxprobe.crud.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UniversalDAOImpl implements UniversalDAO {
	@Autowired
	private UniversalMapper mapper;

	@Override
	public int insert(Object record) {
		return 0;
	}

	@Override
	public int batchInsert(List<?> records) {
		return 0;
	}

	@Override
	public int delete(Object record) {
		return this.mapper.delete(UniversalCrudContent.getDeleteSqlGenerator().toDeleteSql(record));
	}

	@Override
	public long batchDelete(List<?> records) {
		return 1;
	}

	@Override
	public int deleteByPrimaryKey(String id, Class<?> type) {
		return this.mapper.delete(UniversalCrudContent.getDeleteSqlGenerator().toDeleteSqlByPrimaryKey(id, type));
	}

	@Override
	public long batchDeleteByPrimaryKey(List<String> ids, Class<?> type) {
		return 1;
	}

	@Override
	public <T> List<T> universalSelect(BaseQuery param, Class<T> type) {
		SelectSqlGenerator selectSqlGenerator = UniversalCrudContent.getSelectSqlGenerator();
		List<T> records = new LinkedList<>();
		List<Map<String, Object>> mapperResults = this.mapper.universalSelect(selectSqlGenerator.toSelectSql(param));
		for (Map<String, Object> mapperResult : mapperResults) {
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
	public long selectCount(BaseQuery param) {
		SelectSqlGenerator selectSqlGenerator = UniversalCrudContent.getSelectSqlGenerator();
		return this.mapper.selectCount(selectSqlGenerator.toSelectCountSql(param));
	}

	@Override
	public int localUpdate(Object record) {
		return 0;
	}

	@Override
	public int globalUpdate(Object record) {
		return 0;
	}

	@Override
	public List<Map<String, Object>> selectBySql(String sql) {
		List<Map<String, Object>> result = this.mapper.universalSelect(sql);
		return result;
	}
}
