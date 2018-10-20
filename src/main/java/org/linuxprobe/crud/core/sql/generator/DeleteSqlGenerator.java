package org.linuxprobe.crud.core.sql.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.linuxprobe.crud.core.sql.field.ColumnField;
import org.linuxprobe.crud.utils.EntityUtils;

public class DeleteSqlGenerator extends SqlGenerator {
	public static String toDeleteSql(Object entity) {
		if (entity == null) {
			throw new IllegalArgumentException("没有需要被删除的实体");
		}
		ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
		String table = EntityUtils.getTable(entity.getClass());
		String sql = "DELETE FROM " + getEscapeCharacter() + table + getEscapeCharacter() + " WHERE "
				+ getEscapeCharacter() + primaryKey.getColumn() + getEscapeCharacter() + " = " + primaryKey.getValue();
		return sql;
	}

	public static String toDeleteSqlByPrimaryKey(String id, Class<?> type) {
		if (id == null) {
			throw new IllegalArgumentException("没有需要被删除的实体");
		}
		Object entity = null;
		try {
			entity = type.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(type.getName() + "没有无参构造函数", e);
		}
		ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
		String table = EntityUtils.getTable(type);
		String sql = "DELETE FROM " + getEscapeCharacter() + table + getEscapeCharacter() + " WHERE "
				+ getEscapeCharacter() + primaryKey.getColumn() + getEscapeCharacter() + " = '" + id + "'";
		return sql;
	}

	public static String toBatchDeleteSql(List<?> entitys) {
		if (entitys == null || entitys.isEmpty()) {
			throw new IllegalArgumentException("没有需要被删除的实体");
		}
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + getEscapeCharacter());
		for (int i = 0; i < entitys.size(); i++) {
			Object entity = entitys.get(i);
			ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
			if (i == 0) {
				String table = EntityUtils.getTable(entity.getClass());
				sqlBuilder.append(table + getEscapeCharacter() + " WHERE " + getEscapeCharacter()
						+ primaryKey.getColumn() + getEscapeCharacter() + " IN(");
			}
			sqlBuilder.append(primaryKey.getValue() + ", ");
		}
		if (sqlBuilder.lastIndexOf(", ") != -1) {
			sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), "");
		}
		sqlBuilder.append(")");
		return sqlBuilder.toString();
	}

	public static String toBatchDeleteSqlByPrimaryKey(List<String> ids, Class<?> type) {
		if (ids == null || ids.isEmpty()) {
			throw new IllegalArgumentException("没有需要被删除的实体");
		}
		Object entity = null;
		try {
			entity = type.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(type.getName() + "没有无参构造函数", e);
		}
		ColumnField primaryKey = EntityUtils.getPrimaryKey(entity);
		String table = EntityUtils.getTable(type);
		StringBuilder sqlBuilder = new StringBuilder(
				"DELETE FROM " + getEscapeCharacter() + table + getEscapeCharacter() + " WHERE " + getEscapeCharacter()
						+ primaryKey.getColumn() + getEscapeCharacter() + " IN (");
		for (String id : ids) {
			sqlBuilder.append("'" + id + "', ");
		}
		if (sqlBuilder.lastIndexOf(", ") != -1) {
			sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), "");
		}
		sqlBuilder.append(")");
		return sqlBuilder.toString();
	}
}
