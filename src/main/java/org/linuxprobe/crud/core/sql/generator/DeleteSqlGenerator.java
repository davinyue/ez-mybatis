package org.linuxprobe.crud.core.sql.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.linuxprobe.crud.core.sql.field.ColumnField;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.exception.ParameterException;
import org.linuxprobe.crud.utils.EntityUtils;

public class DeleteSqlGenerator {
	public static String toDeleteSql(Object entity) {
		if (entity == null) {
			throw new OperationNotSupportedException("没有需要被删除的实体");
		}
		ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
		String table = EntityUtils.getTable(entity.getClass());
		String sql = "delete from " + table + " where " + primaryKey.getColumn() + " = " + primaryKey.getValue();
		return sql;
	}

	public static String toDeleteSqlByPrimaryKey(String id, Class<?> type) {
		if (id == null) {
			throw new OperationNotSupportedException("没有需要被删除的实体");
		}
		Object entity = null;
		try {
			entity = type.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ParameterException(type.getName() + "没有无参构造函数", e);
		}
		ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
		String table = EntityUtils.getTable(type);
		String sql = "delete from " + table + " where " + primaryKey.getColumn() + " = '" + id + "'";
		return sql;
	}

	public static String toBatchDeleteSql(List<?> entitys) {
		if (entitys == null || entitys.isEmpty()) {
			throw new OperationNotSupportedException("没有需要被删除的实体");
		}
		StringBuffer sqlBuffer = new StringBuffer("delete from ");
		for (int i = 0; i < entitys.size(); i++) {
			Object entity = entitys.get(i);
			ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
			if (i == 0) {
				String table = EntityUtils.getTable(entity.getClass());
				sqlBuffer.append(table + " where " + primaryKey.getColumn() + " in(");
			}
			sqlBuffer.append(primaryKey.getValue() + ", ");
		}
		if (sqlBuffer.lastIndexOf(", ") != -1) {
			sqlBuffer.replace(sqlBuffer.length() - 2, sqlBuffer.length(), "");
		}
		sqlBuffer.append(")");
		return sqlBuffer.toString();
	}

	public static String toBatchDeleteSqlByPrimaryKey(List<String> ids, Class<?> type) {
		if (ids == null || ids.isEmpty()) {
			throw new OperationNotSupportedException("没有需要被删除的实体");
		}
		Object entity = null;
		try {
			entity = type.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ParameterException(type.getName() + "没有无参构造函数", e);
		}
		ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
		String table = EntityUtils.getTable(type);
		StringBuffer sqlBuffer = new StringBuffer(
				"delete from " + table + " where " + primaryKey.getColumn() + " in (");
		for (String id : ids) {
			sqlBuffer.append("'" + id + "', ");
		}
		if (sqlBuffer.lastIndexOf(", ") != -1) {
			sqlBuffer.replace(sqlBuffer.length() - 2, sqlBuffer.length(), "");
		}
		sqlBuffer.append(")");
		return sqlBuffer.toString();
	}
}
