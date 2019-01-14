package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;
import org.linuxprobe.crud.utils.SqlEscapeUtil;

public class MysqlDeleteSqlGenerator implements DeleteSqlGenerator {
	@Override
	public String generateDeleteSql(Object record) {
		if (record == null) {
			throw new NullPointerException("entity can't be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(record.getClass());
		String idValue = MysqlFieldValueConversion.deleteModelConversion(record, entityInfo.getPrimaryKey().getField());
		String sql = "DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
				+ entityInfo.getPrimaryKey().getColumnName() + "` = " + idValue;
		return sql;
	}

	@Override
	public String generateDeleteSqlByPrimaryKey(Serializable id, Class<?> type) {
		if (id == null) {
			throw new NullPointerException("id can't be null");
		}
		if (type == null) {
			throw new NullPointerException("type can't be null");
		}
		if (String.class.isAssignableFrom(id.getClass())) {
			id = "'" + id + "'";
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(type);
		String sql = "DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
				+ entityInfo.getPrimaryKey().getColumnName() + "` = " + id;
		return sql;
	}

	@Override
	public String generateBatchDeleteSql(Collection<?> entitys) {
		if (entitys == null || entitys.isEmpty()) {
			throw new IllegalArgumentException("entitys can't be empty");
		}
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM `");
		Iterator<?> entityIterator = entitys.iterator();
		String table = null;
		EntityInfo entityInfo = null;
		while (entityIterator.hasNext()) {
			Object entity = entityIterator.next();
			if (table == null) {
				entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
				table = entityInfo.getTableName();
				sqlBuilder.append(table + "` WHERE `" + entityInfo.getPrimaryKey().getColumnName() + "` IN(");
			}
			String idValue = MysqlFieldValueConversion.deleteModelConversion(entity,
					entityInfo.getPrimaryKey().getField());
			if (idValue == null) {
				throw new NullPointerException(entity.toString() + " id can't be null");
			}
			if (String.class.isAssignableFrom(idValue.getClass())) {
				idValue = "'" + idValue + "'";
			}
			sqlBuilder.append(idValue + ", ");
		}
		if (sqlBuilder.lastIndexOf(", ") != -1) {
			sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), "");
		}
		sqlBuilder.append(")");
		return sqlBuilder.toString();
	}

	@Override
	public <T extends Serializable> String generateBatchDeleteSqlByPrimaryKey(Collection<T> ids, Class<?> type) {
		if (type == null) {
			throw new NullPointerException("type can't be null");
		}
		if (ids == null || ids.isEmpty()) {
			throw new IllegalArgumentException("ids can't be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(type);
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
				+ entityInfo.getPrimaryKey().getColumnName() + "` IN (");
		Iterator<T> idIterator = ids.iterator();
		while (idIterator.hasNext()) {
			Serializable idValue = idIterator.next();
			if (String.class.isAssignableFrom(idValue.getClass())) {
				idValue = "'" + idValue + "', ";
			}
			sqlBuilder.append(idValue + ", ");
		}
		if (sqlBuilder.lastIndexOf(", ") != -1) {
			sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), "");
		}
		sqlBuilder.append(")");
		return sqlBuilder.toString();
	}

	@Override
	public String generateDeleteSqlByColumnName(String columnName, Serializable columnValue, Class<?> modelType) {
		if (columnName == null) {
			throw new IllegalArgumentException("column cannot be null");
		}
		if (columnValue instanceof String) {
			if ("".equals(columnValue)) {
				throw new IllegalArgumentException("columnValue cannot be empty");
			}
		}
		if (modelType == null) {
			throw new IllegalArgumentException("modelType cannot be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
		String table = entityInfo.getTableName();
		if (String.class.isAssignableFrom(columnValue.getClass())) {
			columnValue = SqlEscapeUtil.mysqlEscape((String) columnValue);
			columnValue = "'" + columnValue + "'";
		}
		String sql = "DElETE  FROM `" + table + "` WHERE `" + columnName + "` = " + columnValue;
		return sql;
	}

	@Override
	public String generateDeleteSqlByFieldName(String fieldName, Serializable fieldValue, Class<?> modelType) {
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
		List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
		String columnName = null;
		for (FieldInfo fieldInfo : fieldInfos) {
			if (fieldInfo.getField().getName().equals(fieldName)) {
				columnName = fieldInfo.getColumnName();
				break;
			}
		}
		if (columnName == null) {
			throw new IllegalArgumentException(fieldName + " is not " + modelType.getClass().getName() + " field");
		}
		return this.generateDeleteSqlByColumnName(columnName, fieldValue, modelType);
	}

	@Override
	public String generateDeleteSqlByColumnNames(String[] columnNames, Serializable[] columnValues,
			Class<?> modelType) {
		if (columnNames == null || columnNames.length == 0) {
			throw new IllegalArgumentException("column cannot be null");
		}
		if (columnValues == null || columnValues.length == 0) {
			throw new IllegalArgumentException("columnValues cannot be null");
		}
		if (columnNames.length != columnValues.length) {
			throw new IllegalArgumentException("columnNames length uneq columnValues length");
		}
		if (modelType == null) {
			throw new IllegalArgumentException("modelType cannot be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
		String table = entityInfo.getTableName();
		StringBuilder sqlBuilder = new StringBuilder("DElETE  FROM `" + table + "` WHERE ");
		for (int i = 0; i < columnNames.length; i++) {
			String columnName = columnNames[i];
			Serializable columnValue = columnValues[i];
			if (String.class.isAssignableFrom(columnValue.getClass())) {
				columnValue = SqlEscapeUtil.mysqlEscape((String) columnValue);
				columnValue = "'" + columnValue + "'";
			}
			if (i + 1 < columnNames.length) {
				sqlBuilder.append("`" + columnName + "` = " + columnValue + " AND ");
			} else {
				sqlBuilder.append("`" + columnName + "` = " + columnValue);
			}
		}
		return sqlBuilder.toString();
	}

	@Override
	public String generateDeleteSqlByFieldNames(String[] fieldNames, Serializable[] fieldValues, Class<?> modelType) {
		if (fieldNames == null || fieldNames.length == 0) {
			throw new IllegalArgumentException("fieldNames cannot be null");
		}
		if (fieldValues == null || fieldValues.length == 0) {
			throw new IllegalArgumentException("fieldValues cannot be null");
		}
		if (fieldValues.length != fieldValues.length) {
			throw new IllegalArgumentException("fieldNames length uneq fieldValues length");
		}
		if (modelType == null) {
			throw new IllegalArgumentException("modelType cannot be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
		List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
		String[] columnNames = new String[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			String fieldName = fieldNames[i];
			String columnName = null;
			for (FieldInfo fieldInfo : fieldInfos) {
				if (fieldInfo.getField().getName().equals(fieldName)) {
					columnName = fieldInfo.getColumnName();
					break;
				}
			}
			if (columnName == null) {
				throw new IllegalArgumentException("No column corresponding to " + fieldName + " was found");
			} else {
				columnNames[i] = columnName;
			}
		}
		return this.generateDeleteSqlByColumnNames(columnNames, fieldValues, modelType);
	}
}
