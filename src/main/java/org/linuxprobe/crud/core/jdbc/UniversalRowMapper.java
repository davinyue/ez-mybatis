package org.linuxprobe.crud.core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.springframework.jdbc.core.RowMapper;

public class UniversalRowMapper<T> implements RowMapper<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Class<?> type = ReflectionUtils.getGenericSuperclass(this.getClass(), 0);
		return (T) ResultSetConvert.convert(resultSet, type);
	}

}
