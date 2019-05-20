package org.linuxprobe.crud.core.sql.generator;

/** 更新sql语句生成器 */
public interface UpdateSqlGenerator extends Escape {
	/** 生成字段全更新sql */
	public String toGlobalUpdateSql(Object entity);

	/** 生成字段选择更新sql */
	public String toLocalUpdateSql(Object entity);
}
