package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.Column.EnumHandler;
import org.linuxprobe.crud.core.annoatation.Column.LengthHandler;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.SqlEscapeUtil;

public class MysqlFieldValueConversion {
	/** 更新模式，不检测id和不生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号 */
	public static String updateConversion(Object entity, Field field) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = null;
		Object fieldValue = FieldUtil.getFieldValue(entity, field);
		if (String.class.isAssignableFrom(field.getType())) {
			String clounmValue = (String) fieldValue;
			if (clounmValue != null) {
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					if (column.length() > 0) {
						if (clounmValue.length() > column.length()) {
							if (column.lengthHandler().equals(LengthHandler.Sub)) {
								clounmValue = clounmValue.substring(0, column.length());
							} else {
								throw new IllegalArgumentException(field.getName() + "字段的赋值超出规定长度" + column.length());
							}
						}
					}
				}
				clounmValue = SqlEscapeUtil.mysqlEscape(clounmValue);
				result = "'" + clounmValue + "'";
			}
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
		} else if (Date.class.isAssignableFrom(field.getType())) {
			Date clounmValue = (Date) fieldValue;
			if (clounmValue != null) {
				result = "'" + dateFormat.format(clounmValue) + "'";
			}
		} else if (Enum.class.isAssignableFrom(field.getType())) {
			Enum<?> clounmValue = (Enum<?>) fieldValue;
			if (clounmValue != null) {
				result = clounmValue.ordinal() + "";
				/** 如果成员变量有Column注解 */
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					if (column.enumHandler().equals(EnumHandler.Name)) {
						result = "'" + clounmValue.toString() + "'";
					}
				}
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
