package org.linuxprobe.crud.service.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.linuxprobe.crud.dao.GeneralDAO;
import org.linuxprobe.crud.model.BaseModel;
import org.linuxprobe.crud.query.BaseQuery;
import org.linuxprobe.crud.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/** 通用service */
@Service
public class GeneralServiceImpl implements GeneralService {
	@Autowired
	private GeneralDAO dao;

	@Override
	public BaseModel add(BaseModel model) {
		this.dao.insert(model);
		return model;
	}

	@Override
	public List<BaseModel> batchAdd(List<BaseModel> models) throws Exception {
		if (models == null || models.isEmpty()) {
			return null;
		} else {
			StringBuffer sqlBuffer = new StringBuffer();
			for (int i = 0; i < models.size(); i++) {
				BaseModel model = models.get(i);
				model.setId(UUID.randomUUID().toString());
				if (i == 0) {
					sqlBuffer.append(model.getSqlr().toInsertSql());
				} else {
					String sql = model.getSqlr().toInsertSql();
					String sqlValue = sql.substring(sql.indexOf("values") + 6);
					sqlBuffer.append(", " + sqlValue);
				}
			}
			this.dao.batchInsert(sqlBuffer.toString());
			return models;
		}
	}

	@Override
	public int localUpdate(BaseModel model) {
		return this.dao.localUpdate(model);
	}

	@Override
	public int globalUpdate(BaseModel model) {
		return this.dao.globalUpdate(model);
	}

	@Override
	public <Model extends BaseModel> int remove(String id, Class<Model> modelClass) throws Exception {
		if (id == null || modelClass == null) {
			return 0;
		}
		BaseModel model = modelClass.getConstructor().newInstance();
		String table = model.getTable().getName();
		String sql = "delete from " + table + " where id = '" + id + "'";
		return this.dao.deleteByPrimaryKey(sql);
	}

	@Override
	public <Model extends BaseModel> long batchRemove(List<String> ids, Class<Model> modelClass) throws Exception {
		if (ids == null || modelClass == null) {
			return 0;
		}
		StringBuffer idsBuffer = new StringBuffer("(");
		for (int i = 0; i < ids.size(); i++) {
			String id = ids.get(i);
			if (i + 1 != ids.size()) {
				idsBuffer.append("'" + id + "', ");
			} else {
				idsBuffer.append("'" + id + "') ");
			}
		}
		BaseModel model = modelClass.getConstructor().newInstance();
		String table = model.getTable().getName();
		String sql = "delete from " + table + " where id in" + idsBuffer.toString();
		return this.dao.batchDeleteByPrimaryKey(sql);
	}

	@Override
	public long selectCount(BaseQuery param) {
		return this.dao.selectCount(param);
	}

	@Override
	public <T> List<T> select(BaseQuery param, Class<T> type) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		List<Map<String, Object>> mapperResults = this.dao.select(param);
		List<T> result = new LinkedList<>();
		for (int i = 0; i < mapperResults.size(); i++) {
			Map<String, Object> mapperResult = mapperResults.get(i);
			String content = mapper.writeValueAsString(mapperResult);
			T record = mapper.readValue(content, type);
			result.add(record);
		}
		return result;
	}
}
