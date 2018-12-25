package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.DateHandler;
import org.linuxprobe.crud.core.annoatation.DateHandler.DateCustomerType;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.StringHandler;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.SqlEscapeUtil;
import org.linuxprobe.crud.utils.SqlFieldUtil;

public class MysqlFieldValueConversion {
	/**
	 * 获取字符串值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getStringValue(Object record, Field field, boolean enalbeCheckRule) {
		String fieldValue = (String) FieldUtil.getFieldValue(record, field);
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (fieldValue != null) {
			if (enalbeCheckRule && field.isAnnotationPresent(StringHandler.class)) {
				StringHandler stringHandler = field.getAnnotation(StringHandler.class);
				if (stringHandler.minLeng() > 0) {
					if (fieldValue.length() < stringHandler.minLeng()) {
						throw new IllegalArgumentException("in " + record.getClass().getName() + "," + field.getName()
								+ " minLeng is " + stringHandler.minLeng());
					}
				}
				if (stringHandler.maxLeng() > 0) {
					if (fieldValue.length() > stringHandler.maxLeng()) {
						throw new IllegalArgumentException("in " + record.getClass().getName() + "," + field.getName()
								+ " maxLeng is " + stringHandler.maxLeng());
					}
				}
				if (!stringHandler.regex().isEmpty()) {
					if (!fieldValue.matches(stringHandler.regex())) {
						throw new IllegalArgumentException(
								"in " + record.getClass().getName() + "," + "the value " + fieldValue + " of "
										+ field.getName() + " cannot match the regex " + stringHandler.regex());
					}
				}
			}
		}
		fieldValue = SqlEscapeUtil.mysqlEscape(fieldValue);
		fieldValue = "'" + fieldValue + "'";
		return fieldValue;
	}

	/**
	 * 获取时间值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getDateValue(Object record, Field field, boolean enalbeCheckRule) {
		Date fieldValue = (Date) FieldUtil.getFieldValue(record, field);
		String result = null;
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (fieldValue != null) {
			if (field.isAnnotationPresent(DateHandler.class)) {
				DateHandler dateHandler = field.getAnnotation(DateHandler.class);
				if (dateHandler.customerType().equals(DateCustomerType.String)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(dateHandler.pattern());
					result = "'" + dateFormat.format(fieldValue) + "'";
				} else {
					result = fieldValue.getTime() + "";
				}
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				result = "'" + dateFormat.format(fieldValue) + "'";
			}
		}
		return result;
	}
	
	/**
	 * 获取时间值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getEnumValue(Object record, Field field, boolean enalbeCheckRule) {
		Enum<?> fieldValue = (Enum) FieldUtil.getFieldValue(record, field);
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		return null;
	}

	/** 更新模式，不检测id和不生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号 */
	public static String updateConversion(Object entity, Field field) {
		String result = null;
		Object fieldValue = FieldUtil.getFieldValue(entity, field);
		if (SqlFieldUtil.isFacultyOfString(field.getType())) {
			result = getStringValue(entity, field, true);
		} else if (Number.class.isAssignableFrom(field.getType())) {
			Number clounmValue = (Number) fieldValue;
			if (clounmValue != null) {
				result = clounmValue.toString();
			}
		} else if (Boolean.class.isAssignableFrom(field.getType())) {
			Boolean clounmValue = (Boolean) fieldValue;
			if (clounmValue != null) {
				if (clounmValue) {
					result = "1";
				} else {
					result = "0";
				}
			}
		} else if (SqlFieldUtil.isFacultyOfDate(field.getType())) {
			result = getDateValue(entity, field, true);
		} else if (Enum.class.isAssignableFrom(field.getType())) {
			Enum<?> clounmValue = (Enum<?>) fieldValue;
			if (clounmValue != null) {
				result = clounmValue.ordinal() + "";
			}
		}
		return result;
	}

	/** 插入模式，检测id和生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号 */
	public static String insertConversion(Object entity, Field field) {
		String result = updateConversion(entity, field);
		/** 如果是主键 */
		if (field.isAnnotationPresent(PrimaryKey.class)) {
			if (result == null) {
				PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
				/** 如果指定主键生成模式是uuid */
				if (primaryKey.value().equals(PrimaryKey.Strategy.UUID)) {
					try {
						String uuid = UUID.randomUUID().toString().replaceAll("-", "");
						FieldUtil.setField(entity, field, uuid);
						result = "'" + uuid + "'";
					} catch (Exception e) {
						throw new OperationNotSupportedException("未找到主键的set方法", e);
					}
				}
				/** 如果指定主键生成模式是程序指定 */
				else if (primaryKey.value().equals(PrimaryKey.Strategy.ASSIGNED)) {
					throw new NullPointerException("primaryKey can't not be null");
				}
			}
		}
		return result;
	}
}
