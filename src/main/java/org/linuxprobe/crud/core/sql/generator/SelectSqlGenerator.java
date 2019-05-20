package org.linuxprobe.crud.core.sql.generator;

import java.io.Serializable;

import org.linuxprobe.crud.core.query.BaseQuery;

public interface SelectSqlGenerator extends Escape {
	/** 转换为查询sql */
	public String toSelectSql(BaseQuery searcher);

	/**
	 * 转换为查询sql
	 * 
	 * @param id        主键
	 * @param modelType model类型
	 */
	public String toSelectSql(Serializable id, Class<?> modelType);

	/**
	 * 转换为查询sql
	 * 
	 * @param column      列名
	 * @param columnValue 列值
	 * @param modelType   model类型
	 */
	public String toSelectSql(String column, Serializable columnValue, Class<?> modelType);

	/**
	 * 根据类成员名称转换为查询sql
	 * 
	 * @param field      属性名称
	 * @param fieldValue 列值
	 * @param modelType  model类型
	 */
	public String toSelectSqlByFieldName(String field, Serializable fieldValue, Class<?> modelType);

	/** 转换为查询数量的sql */
	public String toSelectCountSql(BaseQuery searcher, String clounm);

	/** 转换为查询主键数量的sql */
	public String toSelectCountSql(BaseQuery searcher);

	/**
	 * 生成多对多查询sql
	 * 
	 * @param middleTable          中间表
	 * @param joinColumn           中间表用于join的字段
	 * @param conditionColumn      中间表的条件过滤字段
	 * @param conditionColumnValue 中间表的条件过滤字段的值
	 * @param modelType            需要查询的实体类型
	 */
	public String generateManyToManySelectSql(String middleTable, String joinColumn, String conditionColumn,
			Serializable conditionColumnValue, Class<?> modelType);
}
