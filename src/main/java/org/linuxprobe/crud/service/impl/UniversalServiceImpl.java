package org.linuxprobe.crud.service.impl;

import java.util.List;
import org.linuxprobe.crud.dao.UniversalDAO;
import org.linuxprobe.crud.query.BaseQuery;
import org.linuxprobe.crud.service.UniversalService;
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> batchSave(List<T> records) {
		this.dao.batchInsert((List<Object>) records);
		return records;
	}

	@Override
	public int remove(Object record) {
		return this.dao.delete(record);
	}

	@Override
	public long batchRemove(List<Object> records) {
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
	public <T> List<T> universalSelect(BaseQuery param, Class<T> type) {
		return this.dao.universalSelect(param, type);
	}

	@Override
	public long selectCount(BaseQuery param) {
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
}
