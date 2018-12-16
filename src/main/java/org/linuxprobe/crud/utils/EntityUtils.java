package org.linuxprobe.crud.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;

public class EntityUtils {
	/**
	 * 获取实体类型与数据库表列名对应的field
	 * 
	 * @param entityClass 被查找的实体类类型
	 * @param columnName  列名
	 */
	public static Field getFieldByColumnName(Class<?> entityClass, String columnName) {
		if (entityClass == null || columnName == null) {
			return null;
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(entityClass);
		List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();

		/** 注解匹配 */
		Field columnAnnotationMatch = null;
		/** 完全匹配 */
		Field nameMatch = null;
		/** 下划线转驼峰匹配 */
		Field lineToHumpMatch = null;
		for (FieldInfo fieldInfo : fieldInfos) {
			Field field = fieldInfo.getField();
			if (field.getName().equals(columnName)) {
				nameMatch = field;
			}
			if (StringHumpTool.humpToLine2(field.getName(), "_").equals(columnName)) {
				lineToHumpMatch = field;
			}
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (column.value() != null && !column.value().trim().isEmpty()) {
					if (column.value().trim().equals(columnName)) {
						columnAnnotationMatch = field;
					}
				}
			}
		}
		if (columnAnnotationMatch != null) {
			return columnAnnotationMatch;
		} else if (nameMatch != null) {
			return nameMatch;
		} else if (lineToHumpMatch != null) {
			return lineToHumpMatch;
		} else {
			return null;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setField(Object entity, String column, Object arg)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Field field = getFieldByColumnName(entity.getClass(), column);
		if (field != null) {
			Method methodOfSet = FieldUtil.getMethodOfFieldSet(entity.getClass(), field);
			if (methodOfSet != null) {
				Class<?> argType = methodOfSet.getParameterTypes()[0];
				if (arg == null || argType.isAssignableFrom(arg.getClass())) {
					methodOfSet.invoke(entity, arg);
				} else if (Boolean.class.isAssignableFrom(argType)) {
					String strArg = arg.toString();
					if (strArg.equals("0") || strArg.trim().isEmpty()) {
						methodOfSet.invoke(entity, false);
					} else {
						methodOfSet.invoke(entity, true);
					}
				} else if (Enum.class.isAssignableFrom(argType)) {
					Class<Enum> enumType = (Class<Enum>) field.getType();
					if (Number.class.isAssignableFrom(arg.getClass())) {
						int ordinal = ((Number) arg).intValue();
						Enum[] enums = enumType.getEnumConstants();
						for (Enum tempEnum : enums) {
							if (tempEnum.ordinal() == ordinal) {
								methodOfSet.invoke(entity, tempEnum);
								break;
							}
						}

					} else if (String.class.isAssignableFrom(arg.getClass())) {
						methodOfSet.invoke(entity, Enum.valueOf(enumType, (String) arg));
					}
				}
			}
		}

	}
}
