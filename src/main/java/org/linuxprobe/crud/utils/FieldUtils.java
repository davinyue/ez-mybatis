package org.linuxprobe.crud.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 类的成员工具类 */
public class FieldUtils {
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

	public static void setField(Object obj, Field field, Object arg)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodOfSet = getMethodOfFieldSet(obj.getClass(), field);
		if (methodOfSet != null) {
			methodOfSet.invoke(obj, arg);
		}
	}
}
