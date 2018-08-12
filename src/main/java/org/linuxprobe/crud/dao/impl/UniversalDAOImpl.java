package org.linuxprobe.crud.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.InsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.UpdateSqlGenerator;
import org.linuxprobe.crud.dao.UniversalDAO;
import org.linuxprobe.crud.exception.ParameterException;
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
		return this.mapper.insert(InsertSqlGenerator.toInsertSql(record));
	}

	@Override
	public int batchInsert(List<?> records) {
		return this.mapper.batchInsert(InsertSqlGenerator.toBatchInsertSql(records));
	}

	@Override
	public int delete(Object record) {
		return this.mapper.delete(DeleteSqlGenerator.toDeleteSql(record));
	}

	@Override
	public long batchDelete(List<?> records) {
		return this.mapper.batchDelete(DeleteSqlGenerator.toBatchDeleteSql(records));
	}

	@Override
	public int deleteByPrimaryKey(String id, Class<?> type) {
		return this.mapper.delete(DeleteSqlGenerator.toDeleteSqlByPrimaryKey(id, type));
	}

	@Override
	public long batchDeleteByPrimaryKey(List<String> ids, Class<?> type) {
		return this.mapper.batchDelete(DeleteSqlGenerator.toBatchDeleteSqlByPrimaryKey(ids, type));
	}

	@Override
	public <T> List<T> universalSelect(Object param, Class<T> type) {
		List<T> records = new LinkedList<>();
		List<Map<String, Object>> mapperResults = this.mapper.universalSelect(SelectSqlGenerator.toSelectSql(param));
		for (Map<String, Object> mapperResult : mapperResults) {
			T model = null;
			try {
				model = type.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ParameterException(type.getName() + "没有无参构造函数", e);
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
	public long selectCount(Object param) {
		return this.mapper.selectCount(SelectSqlGenerator.toSelectCountSql(param));
	}

	@Override
	public int localUpdate(Object record) {
		return this.mapper.update(UpdateSqlGenerator.toLocalUpdateSql(record));
	}

	@Override
	public int globalUpdate(Object record) {
		return this.mapper.update(UpdateSqlGenerator.toGlobalUpdateSql(record));
	}
}
