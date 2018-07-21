package org.linuxprobe.crud.dao.impl;

import java.util.List;
import java.util.Map;
import org.linuxprobe.crud.dao.GeneralDAO;
import org.linuxprobe.crud.mapper.GeneralMapper;
import org.linuxprobe.crud.model.BaseModel;
import org.linuxprobe.crud.query.BaseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** 通用dao */
@Repository
public class GeneralDAOImpl implements GeneralDAO {
	@Autowired
	private GeneralMapper mapper;

	@Override
	public int insert(BaseModel record) {
		return this.mapper.insert(record);
	}

	@Override
	public int batchInsert(String sql) {
		return this.mapper.batchInsert(sql);
	}

	@Override
	public int deleteByPrimaryKey(String sql) {
		return this.mapper.deleteByPrimaryKey(sql);
	}

	@Override
	public long batchDeleteByPrimaryKey(String sql) {
		return this.mapper.batchDeleteByPrimaryKey(sql);
	}

	@Override
	public List<Map<String, Object>> select(BaseQuery param) {
		return this.mapper.select(param);
	}

	@Override
	public long selectCount(BaseQuery param) {
		return this.mapper.selectCount(param);
	}

	@Override
	public int localUpdate(BaseModel record) {
		return this.mapper.localUpdate(record);
	}

	@Override
	public int globalUpdate(BaseModel record) {
		return this.mapper.globalUpdate(record);
	}
}
