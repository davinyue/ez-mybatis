package org.linuxprobe.crud.dao.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.linuxprobe.crud.dao.UniversalDAO;
import org.linuxprobe.crud.mapper.UniversalMapper;
import org.linuxprobe.crud.persistence.Sqlr;
import org.linuxprobe.crud.query.BaseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

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
		return this.mapper.deleteByPrimaryKey(record, sqlr);
	}

	@Override
	public long batchDelete(List<Object> records) {
		return this.mapper.batchDeleteByPrimaryKey(records, sqlr);
	}

	@Override
	public <T> List<T> universalSelect(BaseQuery param, Class<T> type) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		List<Map<String, Object>> mapperResults = this.mapper.universalSelect(param);
		List<T> result = new LinkedList<>();
		for (int i = 0; i < mapperResults.size(); i++) {
			Map<String, Object> mapperResult = mapperResults.get(i);
			String content;
			try {
				content = mapper.writeValueAsString(mapperResult);
				T record = mapper.readValue(content, type);
				result.add(record);
			} catch (IOException e) {
				continue;
			}
		}
		return result;
	}

	@Override
	public long selectCount(BaseQuery param) {
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
