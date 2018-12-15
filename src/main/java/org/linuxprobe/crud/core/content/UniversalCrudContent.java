package org.linuxprobe.crud.core.content;

import java.util.HashMap;
import java.util.Map;

import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.InsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.UpdateSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlDeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlInsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlSelectSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlUpdateSqlGenerator;

public class UniversalCrudContent {
	private static Map<Class<?>, EntityInfo> entityInfos = new HashMap<>();
	private static InsertSqlGenerator insertSqlGenerator = new MysqlInsertSqlGenerator();
	private static DeleteSqlGenerator deleteSqlGenerator = new MysqlDeleteSqlGenerator();
	private static SelectSqlGenerator selectSqlGenerator = new MysqlSelectSqlGenerator();
	private static UpdateSqlGenerator updateSqlGenerator = new MysqlUpdateSqlGenerator();

	public static InsertSqlGenerator getInsertSqlGenerator() {
		return insertSqlGenerator;
	}

	public static DeleteSqlGenerator getDeleteSqlGenerator() {
		return deleteSqlGenerator;
	}

	public static SelectSqlGenerator getSelectSqlGenerator() {
		return selectSqlGenerator;
	}

	public static UpdateSqlGenerator getUpdateSqlGenerator() {
		return updateSqlGenerator;
	}

	public static void addEntityInfo(Class<?> entityType) {
		entityInfos.put(entityType, new EntityInfo(entityType));
	}

	public static EntityInfo getEntityInfo(Class<?> entityType) {
		return entityInfos.get(entityType);
	}
}
