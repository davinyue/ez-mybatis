package org.linuxprobe.crud.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.linuxprobe.crud.dao.UniversalDAO;
import org.linuxprobe.crud.exception.ParameterException;
import org.linuxprobe.crud.mapper.UniversalMapper;
import org.linuxprobe.crud.persistence.Sqlr;
import org.linuxprobe.crud.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UniversalDAOImpl implements UniversalDAO {
	@Autowired
	private UniversalMapper mapper;
	private static Sqlr sqlr = Sqlr.getInstance();

	@Override
	public int insert(Object record) {
		return this.mapper.insert(record, sqlr);
	}

	@Override
	public int batchInsert(List<Object> records) {
		return this.mapper.batchInsert(records, sqlr);
	}

	@Override
	public int delete(Object record) {
		return this.mapper.delete(record, sqlr);
	}

	@Override
	public long batchDelete(List<Object> records) {
		return this.mapper.batchDelete(records, sqlr);
	}

	@Override
	public int deleteByPrimaryKey(String id, Class<?> type) {
		try {
			return this.mapper.deleteByPrimaryKey(Sqlr.toDeleteSqlByPrimaryKey(id, type));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public long batchDeleteByPrimaryKey(List<String> ids, Class<?> type) {
		try {
			return this.mapper.batchDeleteByPrimaryKey(Sqlr.toBatchDeleteSqlByPrimaryKey(ids, type));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public <T> List<T> universalSelect(Object param, Class<T> type) {
		List<T> records = new LinkedList<>();
		List<Map<String, Object>> mapperResults = this.mapper.universalSelect(param);
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
		return this.mapper.selectCount(param);
	}

	@Override
	public int localUpdate(Object record) {
		return this.mapper.localUpdate(record, sqlr);
	}

	@Override
	public int globalUpdate(Object record) {
		return this.mapper.globalUpdate(record, sqlr);
	}
}
