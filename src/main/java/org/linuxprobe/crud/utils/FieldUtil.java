package org.linuxprobe.crud.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/** 类的成员工具类 */
public class FieldUtil {
	/** 获取该类型的所有成员，包括它的超类的成员 */
	public static List<Field> getAllFields(Class<?> objClass) {
		List<Field> fields = Arrays.asList(objClass.getDeclaredFields());
		fields = new ArrayList<Field>(fields);
		Class<?> superClass = objClass.getSuperclass();
		if (superClass != null) {
			for (;;) {
				if (!superClass.equals(Object.class)) {
					fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
					superClass = superClass.getSuperclass();
				} else {
					break;
				}
			}
		}
		return fields;
	}

	/** 获取所有的sql支持的类型成员 */
	public static List<Field> getAllSqlSupportFields(Class<?> objClass) {
		List<Field> fields = getAllFields(objClass);
		List<Field> result = new LinkedList<>();
		for (Field field : fields) {
			Class<?> fieldType = field.getType();
			if (String.class.isAssignableFrom(fieldType)) {
				result.add(field);
			} else if (Number.class.isAssignableFrom(fieldType)) {
				result.add(field);
			} else if (Boolean.class.isAssignableFrom(fieldType)) {
				result.add(field);
			} else if (Date.class.isAssignableFrom(fieldType)) {
				result.add(field);
			} else if (Enum.class.isAssignableFrom(fieldType)) {
				result.add(field);
			}
		}
		return result;
	}

	/** 获取类的field设置方法 */
	public static Method getMethodOfFieldSet(Class<?> objClass, Field field) {
		if (objClass == null || field == null) {
			return null;
		}
		String fieldName = field.getName();
		String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method methodOfSet = null;
		try {
			methodOfSet = objClass.getMethod("set" + funSuffix, field.getType());
		} catch (NoSuchMethodException | SecurityException e) {
		}
		return methodOfSet;
	}

	/** 获取类的field get方法 */
	public static Method getMethodOfFieldGet(Class<?> objClass, Field field) {
		if (objClass == null || field == null) {
			return null;
		}
		String fieldName = field.getName();
		String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method methodOfGet = null;
		try {
			methodOfGet = objClass.getMethod("get" + funSuffix);
		} catch (NoSuchMethodException | SecurityException e) {
		}
		return methodOfGet;
	}

	public static void setField(Object obj, Field field, Object... arg)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodOfSet = getMethodOfFieldSet(obj.getClass(), field);
		if (methodOfSet != null) {
			methodOfSet.invoke(obj, arg);
		}
	}

	public static Object getFieldValue(Object obj, Field field) {
		Method getMethod = getMethodOfFieldGet(obj.getClass(), field);
		try {
			Object value = getMethod.invoke(obj);
			return value;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
