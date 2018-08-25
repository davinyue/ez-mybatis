package org.linuxprobe.crud.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.linuxprobe.crud.dao.UniversalDAO;
import org.linuxprobe.crud.service.UniversalService;
import org.linuxprobe.crud.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversalServiceImpl implements UniversalService {
	@Autowired
	private UniversalDAO dao;

	@Override
	public <T> T save(T record) {
		this.dao.insert(record);
		return record;
	}

	@Override
	public <T> List<T> batchSave(List<T> records) {
		this.dao.batchInsert(records);
		return records;
	}

	@Override
	public int remove(Object record) {
		return this.dao.delete(record);
	}

	@Override
	public long batchRemove(List<?> records) {
		return this.dao.batchDelete(records);
	}

	@Override
	public int removeByPrimaryKey(String id, Class<?> type) {
		return this.dao.deleteByPrimaryKey(id, type);
	}

	@Override
	public long batchRemoveByPrimaryKey(List<String> ids, Class<?> type) {
		return this.dao.batchDeleteByPrimaryKey(ids, type);
	}

	@Override
	public <T> List<T> universalSelect(Object param, Class<T> type) {
		return this.dao.universalSelect(param, type);
	}

	@Override
	public long selectCount(Object param) {
		return this.dao.selectCount(param);
	}

	@Override
	public int localUpdate(Object record) {
		return this.dao.localUpdate(record);
	}

	@Override
	public int globalUpdate(Object record) {
		return this.dao.globalUpdate(record);
	}

	@Override
	public List<Map<String, Object>> selectBySql(String sql) {
		return this.dao.selectBySql(sql);
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
	public Map<String, Object> selectUniqueResultBySql(String sql) {
		List<Map<String, Object>> mapResult = this.selectBySql(sql);
		if (mapResult != null && !mapResult.isEmpty()) {
			return mapResult.get(0);
		} else
			return null;
	}

	@Override
	public <T> T selectUniqueResultBySql(String sql, Class<T> type) {
		List<T> records = this.selectBySql(sql, type);
		if (records != null && !records.isEmpty()) {
			return records.get(0);
		} else
			return null;
	}
}
