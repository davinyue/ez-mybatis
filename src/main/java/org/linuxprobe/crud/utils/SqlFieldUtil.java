package org.linuxprobe.crud.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.Column.EnumHandler;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;

public class SqlFieldUtil {
	/** 获取实体主键值 */
	public static Serializable getPrimaryKeyValue(Object entity) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Serializable result = null;
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
		Field primaryKey = entityInfo.getPrimaryKey().getField();
		String fieldName = primaryKey.getName();
		/** 获取本次参数的方法 */
		String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method getCurrnetClounm = null;
		try {
			getCurrnetClounm = entity.getClass().getMethod("get" + funSuffix);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		Object fieldValue = null;
		try {
			/** 得到本次参数 */
			fieldValue = getCurrnetClounm.invoke(entity);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
		if (String.class.isAssignableFrom(primaryKey.getType())) {
			String clounmValue = (String) fieldValue;
			if (clounmValue != null) {
				clounmValue = SqlEscapeUtil.escape(clounmValue);
				result = clounmValue;
			}
		} else if (Number.class.isAssignableFrom(primaryKey.getType())) {
			Number clounmValue = (Number) fieldValue;
			if (clounmValue != null) {
				result = clounmValue;
			}
		} else if (Boolean.class.isAssignableFrom(primaryKey.getType())) {
			Boolean clounmValue = (Boolean) fieldValue;
			if (clounmValue != null) {
				if (clounmValue) {
					result = 1;
				} else {
					result = 0;
				}
			}
		} else if (Date.class.isAssignableFrom(primaryKey.getType())) {
			Date clounmValue = (Date) fieldValue;
			if (clounmValue != null) {
				result = dateFormat.format(clounmValue);
			}
		} else if (Enum.class.isAssignableFrom(primaryKey.getType())) {
			Enum<?> clounmValue = (Enum<?>) fieldValue;
			if (clounmValue != null) {
				result = clounmValue.ordinal();
				/** 如果成员变量有Column注解 */
				if (primaryKey.isAnnotationPresent(Column.class)) {
					Column column = primaryKey.getAnnotation(Column.class);
					if (column.enumHandler().equals(EnumHandler.Name)) {
						result = clounmValue.toString();
					}
				}
			}
		} else {
			throw new IllegalArgumentException(entity.getClass().getName() + "主键成员不是被支持的类型");
		}
		return result;
	}
}
