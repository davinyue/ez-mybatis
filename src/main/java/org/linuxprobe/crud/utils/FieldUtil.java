package org.linuxprobe.crud.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** 类的成员工具类 */
public class FieldUtil {
	/** 获取sql支持的类型 */
	public static List<Class<?>> getSqlSuperClasss() {
		List<Class<?>> result = new LinkedList<>();
		result.add(Byte.class);
		result.add(Character.class);
		result.add(Short.class);
		result.add(Boolean.class);
		result.add(Integer.class);
		result.add(Long.class);
		result.add(Float.class);
		result.add(Double.class);
		result.add(BigDecimal.class);
		result.add(Number.class);
		result.add(String.class);
		result.add(Enum.class);
		result.add(Blob.class);
		result.add(Serializable.class);
		result.add(Byte[].class);
		result.add(byte.class);
		result.add(char.class);
		result.add(short.class);
		result.add(boolean.class);
		result.add(int.class);
		result.add(long.class);
		result.add(float.class);
		result.add(double.class);
		result.add(byte[].class);
		return result;
	}

	/** 获取该类型的所有成员，包括它的超类的成员 */
	public static List<Field> getAllFields(Class<?> objClass) {
		objClass = getRealCalssOfProxyClass(objClass);
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
		List<Class<?>> sqlSuperClasss = getSqlSuperClasss();
		for (Field field : fields) {
			for (Class<?> superClass : sqlSuperClasss) {
				if (superClass.getName().equals(field.getType().getName())) {
					result.add(field);
					break;
				}
			}
		}
		return result;
	}

	/** 获取类的field设置方法 */
	public static Method getMethodOfFieldSet(Class<?> objClass, Field field) {
		if (objClass == null || field == null) {
			return null;
		}
		objClass = getRealCalssOfProxyClass(objClass);
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
		objClass = getRealCalssOfProxyClass(objClass);
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
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Method methodOfSet = getMethodOfFieldSet(objClass, field);
		if (methodOfSet != null) {
			methodOfSet.invoke(obj, arg);
		}
	}

	public static Object getFieldValue(Object obj, Field field) {
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Method getMethod = getMethodOfFieldGet(objClass, field);
		getMethod.setAccessible(true);
		try {
			Object value = getMethod.invoke(obj);
			return value;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Object getFieldValueByFieldName(Object obj, String fieldName) {
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Field field = null;
		try {
			field = objClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e1) {
			throw new RuntimeException(e1);
		}
		Method getMethod = getMethodOfFieldGet(objClass, field);
		getMethod.setAccessible(true);
		try {
			Object value = getMethod.invoke(obj);
			return value;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String getFieldNameByMethod(Method method) {
		String fieldName = method.getName().substring(3, method.getName().length());
		fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1, fieldName.length());
		return fieldName;
	}

	public static Field getFieldByMethod(Class<?> type, Method method) {
		type = getRealCalssOfProxyClass(type);
		String fieldName = getFieldNameByMethod(method);
		return getDeclaredField(type, fieldName);
	}

	public static Class<?> getRealCalssOfProxyClass(Class<?> type) {
		while (type.getSimpleName().indexOf("$$EnhancerByCGLIB$$") != -1) {
			type = type.getSuperclass();
		}
		return type;
	}

	public static Field getDeclaredField(Class<?> type, String fieldName) {
		List<Field> fields = getAllFields(type);
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	/** 获取类的父类泛型类型参数 */
	public static Class<?> getGenericSuperclass(Class<?> clazz, int order) {
		Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[order];
		Class<?> genericsCalss = null;
		try {
			genericsCalss = Class.forName(type.getTypeName());
			return genericsCalss;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/** 获取类的父类泛型类型参数 */
	public static Type getGenericSuperType(Class<?> clazz, int order) {
		Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[order];
		return type;
	}

	/** 获取field的泛型类型参数,eg List<T>, 获取T的类型 */
	public static Class<?> getFiledGenericclass(Field field, int order) {
		ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
		if (parameterizedType == null) {
			throw new IllegalArgumentException("必须指定泛型类型");
		}
		Type type = parameterizedType.getActualTypeArguments()[0];
		Class<?> genericsCalss = null;
		try {
			genericsCalss = Class.forName(type.getTypeName());
			return genericsCalss;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/** 判断一个类是否是代理类 */
	public static boolean isProxyClass(Class<?> clazz) {
		if (clazz.getSimpleName().indexOf("$$EnhancerByCGLIB$$") != -1) {
			return true;
		} else {
			return false;
		}
	}
}
