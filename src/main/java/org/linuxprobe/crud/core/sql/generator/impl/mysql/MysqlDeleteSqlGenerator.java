package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;

public class MysqlDeleteSqlGenerator implements DeleteSqlGenerator {
	@Override
	public String toDeleteSql(Object entity) {
		if (entity == null) {
			throw new NullPointerException("entity can't be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
		String idValue = MysqlFieldValueConversion.updateConversion(entity, entityInfo.getPrimaryKey().getField());
		String sql = "DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
				+ entityInfo.getPrimaryKey().getFiledColumn() + "` = " + idValue;
		return sql;
	}

	@Override
	public String toDeleteSqlByPrimaryKey(Serializable id, Class<?> type) {
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
				+ entityInfo.getPrimaryKey().getFiledColumn() + "` = " + id;
		return sql;
	}

	@Override
	public String toBatchDeleteSql(Collection<?> entitys) {
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
				sqlBuilder.append(table + "` WHERE `" + entityInfo.getPrimaryKey().getFiledColumn() + "` IN(");
			}
			String idValue = MysqlFieldValueConversion.updateConversion(entity, entityInfo.getPrimaryKey().getField());
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
	public <T extends Serializable> String toBatchDeleteSqlByPrimaryKey(Collection<T> ids, Class<?> type) {
		if (type == null) {
			throw new NullPointerException("type can't be null");
		}
		if (ids == null || ids.isEmpty()) {
			throw new IllegalArgumentException("ids can't be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(type);
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
				+ entityInfo.getPrimaryKey().getFiledColumn() + "` IN (");
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
}
