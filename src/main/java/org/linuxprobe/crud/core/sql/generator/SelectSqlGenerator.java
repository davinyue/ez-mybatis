package org.linuxprobe.crud.core.sql.generator;

import java.io.Serializable;

import org.linuxprobe.crud.core.query.BaseQuery;

public interface SelectSqlGenerator {
	/** 转换为查询sql */
	public String toSelectSql(BaseQuery searcher);
	
	/** 转换为查询sql
	 * @param id 主键
	 * @param modelType model类型 */
	public String toSelectSql(Serializable id, Class<?> modelType);

	/** 转换为查询数量的sql */
	public String toSelectCountSql(BaseQuery searcher, String clounm);

	/** 转换为查询主键数量的sql */
	public String toSelectCountSql(BaseQuery searcher);
}
