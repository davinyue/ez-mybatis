package org.linuxprobe.crud.core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UniversalResultSetExtractor<T> implements ResultSetExtractor<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T extractData(ResultSet resultSet) throws SQLException, DataAccessException {
		Class<?> type = ReflectionUtils.getGenericSuperclass(this.getClass(), 0);
		return (T) ResultSetConvert.convert(resultSet, type);
	}

}
