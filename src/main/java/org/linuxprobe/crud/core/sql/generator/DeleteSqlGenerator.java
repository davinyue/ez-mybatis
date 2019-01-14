package org.linuxprobe.crud.core.sql.generator;

import java.io.Serializable;
import java.util.Collection;

public interface DeleteSqlGenerator {
	/**
	 * 生成删除sql
	 * 
	 * @param record 需要删除的记录
	 */
	public String generateDeleteSql(Object record);

	/**
	 * 生成删除sql
	 * 
	 * @param id   主键
	 * @param type 需要删除的记录的类型
	 */
	public String generateDeleteSqlByPrimaryKey(Serializable id, Class<?> type);

	/**
	 * 生成删除sql
	 * 
	 * @param records 需要删除的记录
	 */
	public String generateBatchDeleteSql(Collection<?> records);

	/**
	 * 生成删除sql
	 * 
	 * @param ids  主键
	 * @param type model类型
	 */
	public <T extends Serializable> String generateBatchDeleteSqlByPrimaryKey(Collection<T> ids, Class<?> type);

	/**
	 * 根据列名生成删除sql
	 * 
	 * @param columnName  列名
	 * @param columnValue 列值
	 * @param modelType   model类型
	 */
	public String generateDeleteSqlByColumnName(String columnName, Serializable columnValue, Class<?> modelType);

	/**
	 * 根据列名生成删除sql，列名称与列值请一一对象
	 * 
	 * @param columnNames  列名
	 * @param columnValues 列值
	 * @param modelType    model类型
	 */
	public String generateDeleteSqlByColumnNames(String[] columnNames, Serializable[] columnValues, Class<?> modelType);

	/**
	 * 根据类的成员名称生成删除sql
	 * 
	 * @param fieldName  属性名称
	 * @param fieldValue 属性值
	 * @param modelType  model类型
	 */
	public String generateDeleteSqlByFieldName(String fieldName, Serializable fieldValue, Class<?> modelType);

	/**
	 * 根据属性名称生成删除sql，属性名称与属性值请一一对应
	 * 
	 * @param fieldNames  属性名称
	 * @param fieldValues 属性值
	 * @param modelType   model类型
	 */
	public String generateDeleteSqlByFieldNames(String[] fieldNames, Serializable[] fieldValues, Class<?> modelType);
}
