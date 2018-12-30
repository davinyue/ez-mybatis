package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.util.List;

import org.linuxprobe.crud.core.annoatation.UpdateIgnore;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.sql.generator.UpdateSqlGenerator;
import org.linuxprobe.crud.exception.OperationNotSupportedException;

public class MysqlUpdateSqlGenerator implements UpdateSqlGenerator {
	/** 生成字段全更新sql */
	@Override
	public String toGlobalUpdateSql(Object entity) {
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
		if (entityInfo.getPrimaryKey() == null) {
			throw new OperationNotSupportedException(entity.getClass()
					+ " cannot be without primary key, please use org.linuxprobe.crud.core.annoatation.PrimaryKey Mark the primary key");
		}
		String table = entityInfo.getTableName();
		/** 获取主键值 */
		String primaryKeyValue = MysqlFieldValueConversion.updateModelConversion(entity,
				entityInfo.getPrimaryKey().getField());
		if (primaryKeyValue == null) {
			throw new IllegalArgumentException("The primary key cannot be null");
		}
		if (String.class.isAssignableFrom(primaryKeyValue.getClass())) {
			if (primaryKeyValue.isEmpty()) {
				throw new IllegalArgumentException("The primary key cannot be empty");
			}
		}
		StringBuilder sqlBuilder = new StringBuilder("UPDATE `" + table + "` SET ");
		List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
		for (FieldInfo fieldInfo : fieldInfos) {
			/** 如果指定忽略更新，跳过 */
			if (fieldInfo.getField().isAnnotationPresent(UpdateIgnore.class)) {
				continue;
			}
			String value = MysqlFieldValueConversion.updateModelConversion(entity, fieldInfo.getField());
			sqlBuilder.append("`" + fieldInfo.getColumnName() + "` = " + value + ", ");
		}
		if (sqlBuilder.indexOf(",") != -1)
			sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), " ");
		sqlBuilder.append("WHERE `" + entityInfo.getPrimaryKey().getColumnName() + "` = " + primaryKeyValue);
		return sqlBuilder.toString();
	}

	/** 生成字段选择更新sql */
	@Override
	public String toLocalUpdateSql(Object entity) {
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
		if (entityInfo.getPrimaryKey() == null) {
			throw new OperationNotSupportedException(entity.getClass()
					+ " cannot be without primary key, please use org.linuxprobe.crud.core.annoatation.PrimaryKey Mark the primary key");
		}
		String table = entityInfo.getTableName();
		/** 获取主键值 */
		String primaryKeyValue = MysqlFieldValueConversion.updateModelConversion(entity,
				entityInfo.getPrimaryKey().getField());
		if (primaryKeyValue == null) {
			throw new IllegalArgumentException("The primary key cannot be null");
		}
		if (String.class.isAssignableFrom(primaryKeyValue.getClass())) {
			if (primaryKeyValue.isEmpty()) {
				throw new IllegalArgumentException("The primary key cannot be empty");
			}
		}

		StringBuilder sqlBuilder = new StringBuilder("UPDATE `" + table + "` SET ");
		List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
		for (FieldInfo fieldInfo : fieldInfos) {
			/** 如果指定忽略更新，跳过 */
			if (fieldInfo.getField().isAnnotationPresent(UpdateIgnore.class)) {
				continue;
			}
			String value = MysqlFieldValueConversion.updateModelConversion(entity, fieldInfo.getField());
			if (value == null) {
				continue;
			} else {
				sqlBuilder.append("`" + fieldInfo.getColumnName() + "` = " + value + ", ");
			}
		}
		if (sqlBuilder.indexOf(",") != -1)
			sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), " ");
		sqlBuilder.append("WHERE `" + entityInfo.getPrimaryKey().getColumnName() + "` = " + primaryKeyValue);
		return sqlBuilder.toString();
	}
}
