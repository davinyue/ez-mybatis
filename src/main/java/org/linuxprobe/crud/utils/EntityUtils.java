package org.linuxprobe.crud.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.Table;
import org.linuxprobe.crud.core.annoatation.Column.EnumHandler;
import org.linuxprobe.crud.core.sql.field.ColumnField;

public class EntityUtils {
	/**
	 * 获取实体类型与数据库表列名对应的field
	 * 
	 * @param entityClass
	 *            被查找的实体类类型
	 * @param columnName
	 *            列名
	 */
	public static Field getFieldByColumnName(Class<?> entityClass, String columnName) {
		if (entityClass == null || columnName == null) {
			return null;
		}
		List<Field> fields = FieldUtils.getAllFields(entityClass);
		/** 注解匹配 */
		Field columnAnnotationMatch = null;
		/** 完全匹配 */
		Field nameMatch = null;
		/** 下划线转驼峰匹配 */
		Field lineToHumpMatch = null;
		for (Field field : fields) {
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
			Method methodOfSet = FieldUtils.getMethodOfFieldSet(entity.getClass(), field);
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

	/** 获取实体类型对应的表名 */
	public static String getTable(Class<?> entityType) {
		boolean classHasTableAnno = entityType.isAnnotationPresent(Table.class);
		if (classHasTableAnno) {
			Table annotation = entityType.getAnnotation(Table.class);
			String table = annotation.value();
			if (table.trim().isEmpty()) {
				return StringHumpTool.humpToLine2(entityType.getSimpleName(), "_");
			} else {
				return table.trim();
			}
		} else {
			return StringHumpTool.humpToLine2(entityType.getSimpleName(), "_");
		}
	}

	/** 获取实体主键 */
	public static ColumnField getPrimaryKey(Object entity) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Field> fields = FieldUtils.getAllFields(entity.getClass());
		ColumnField result = null;
		Field primaryKey = null;
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				primaryKey = field;
				break;
			}
		}
		if (primaryKey != null) {
			result = new ColumnField();
			String fieldName = primaryKey.getName();
			result.setName(fieldName);
			/** 获取本次参数的方法 */
			String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getCurrnetClounm = null;
			try {
				getCurrnetClounm = entity.getClass().getMethod("get" + funSuffix);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalArgumentException(entity.getClass().getName() + "主键成员没有get方法", e);
			}
			String value = null;
			Object fieldValue = null;
			try {
				/** 得到本次参数 */
				fieldValue = getCurrnetClounm.invoke(entity);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalArgumentException(entity.getClass().getName() + "主键成员的get方法调用失败", e);
			}
			if (String.class.isAssignableFrom(primaryKey.getType())) {
				String clounmValue = (String) fieldValue;
				if (clounmValue != null) {
					clounmValue = SqlEscapeUtil.escape(clounmValue);
					value = "'" + clounmValue + "'";
				} else {
					value = null;
				}
			} else if (Number.class.isAssignableFrom(primaryKey.getType())) {
				Number clounmValue = (Number) fieldValue;
				if (clounmValue != null) {
					value = clounmValue.toString();
				} else {
					value = null;
				}
			} else if (Boolean.class.isAssignableFrom(primaryKey.getType())) {
				Boolean clounmValue = (Boolean) fieldValue;
				if (clounmValue != null) {
					if (clounmValue) {
						value = "1";
					} else {
						value = "0";
					}
				} else {
					value = null;
				}
			} else if (Date.class.isAssignableFrom(primaryKey.getType())) {
				Date clounmValue = (Date) fieldValue;
				if (clounmValue != null) {
					value = "'" + dateFormat.format(clounmValue) + "'";
				} else {
					value = null;
				}
			} else if (Enum.class.isAssignableFrom(primaryKey.getType())) {
				Enum<?> clounmValue = (Enum<?>) fieldValue;
				if (clounmValue != null) {
					value = clounmValue.ordinal() + "";
					/** 如果成员变量有Column注解 */
					if (primaryKey.isAnnotationPresent(Column.class)) {
						Column column = primaryKey.getAnnotation(Column.class);
						if (column.enumHandler().equals(EnumHandler.Name)) {
							value = "'" + clounmValue.toString() + "'";
						}
					}
				} else {
					value = null;
				}
			} else {
				throw new IllegalArgumentException(entity.getClass().getName() + "主键成员不是被支持的类型");
			}
			result.setValue(value);
			/** 设置数据库列是成员名称转下划线 */
			result.setColumn(StringHumpTool.humpToLine2(fieldName, "_"));
			/** 如果成员变量有Column注解 */
			if (primaryKey.isAnnotationPresent(Column.class)) {
				Column column = primaryKey.getAnnotation(Column.class);
				String strColumn = column.value();
				if (strColumn != null && !strColumn.trim().isEmpty()) {
					result.setColumn(strColumn);
				}
			}
		} else {
			throw new IllegalArgumentException(entity.getClass().getName() + "所有成员变量均未标注@PrimaryKey注解");
		}
		return result;
	}
}
