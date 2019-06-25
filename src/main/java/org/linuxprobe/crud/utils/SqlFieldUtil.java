package org.linuxprobe.crud.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.linuxprobe.luava.string.StringUtils;
import org.springframework.util.StreamUtils;

public class SqlFieldUtil {
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
		result.add(Date.class);
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

	/** 获取所有的sql支持的类型成员 */
	public static List<Field> getAllSqlSupportFields(Class<?> objClass) {
		List<Field> fields = ReflectionUtils.getAllFields(objClass);
		List<Field> result = new LinkedList<>();
		List<Class<?>> sqlSuperClasss = getSqlSuperClasss();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPrivate(modifiers) && Modifier.isFinal(modifiers)
					&& field.getName().equals("serialVersionUID")) {
				continue;
			}
			for (Class<?> superClass : sqlSuperClasss) {
				if (superClass.isAssignableFrom(field.getType())) {
					result.add(field);
					break;
				}
			}
		}
		return result;
	}

	/** 是否是字符串系列类型 */
	public static boolean isFacultyOfString(Class<?> type) {
		if (String.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	/** 是否是时间系列类型 */
	public static boolean isFacultyOfDate(Class<?> type) {
		if (Date.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	/** 是否是数字系列类型 */
	public static boolean isFacultyOfNumber(Class<?> type) {
		if (Number.class.isAssignableFrom(type)) {
			return true;
		}
		if (byte.class.isAssignableFrom(type)) {
			return true;
		}
		if (short.class.isAssignableFrom(type)) {
			return true;
		}
		if (int.class.isAssignableFrom(type)) {
			return true;
		}
		if (long.class.isAssignableFrom(type)) {
			return true;
		}
		if (float.class.isAssignableFrom(type)) {
			return true;
		}
		if (double.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	/** 是否是二进制系列类型 */
	public static boolean isFacultyOfBlob(Class<?> type) {
		if (Blob.class.isAssignableFrom(type)) {
			return true;
		} else if (byte[].class.isAssignableFrom(type)) {
			return true;
		} else if (Byte[].class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	/** 是否是枚举系列类型 */
	public static boolean isFacultyOfEnum(Class<?> type) {
		if (Enum.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	/** 是否是布尔系列类型 */
	public static boolean isFacultyOfBoolean(Class<?> type) {
		if (Boolean.class.isAssignableFrom(type)) {
			return true;
		} else if (boolean.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	/** 是否是字符系列类型 */
	public static boolean isFacultyOfChar(Class<?> type) {
		if (char.class.isAssignableFrom(type)) {
			return true;
		} else if (Character.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	/** 设置实体的属性值 */
	@SuppressWarnings("unchecked")
	public static void setFieldValue(Object object, Field field, Object value) {
		/** 如果是字符串 */
		if (isFacultyOfString(field.getType())) {
			ReflectionUtils.setFieldValue(object, field, value.toString(), true);
		}
		/** 如果是时间 */
		else if (isFacultyOfDate(field.getType())) {
			long timestamp = 0;
			if (isFacultyOfDate(value.getClass())) {
				timestamp = ((Date) value).getTime();
			} else if (isFacultyOfNumber(value.getClass())) {
				timestamp = ((Number) value).longValue();
			} else {
				throw new ClassCastException(
						value.getClass().getName() + " can't cast to " + field.getType().getName());
			}
			if (java.sql.Date.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, new java.sql.Date(timestamp), true);
			} else if (Timestamp.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, new Timestamp(timestamp), true);
			} else if (Time.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, new Time(timestamp), true);
			} else if (Date.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, new Date(timestamp), true);
			}
		}
		/** 如果是数值 */
		else if (isFacultyOfNumber(field.getType())) {
			Number number = (Number) value;
			if (BigDecimal.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, new BigDecimal(number.toString()), true);
			} else if (byte.class.isAssignableFrom(field.getType()) || Byte.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, number.byteValue(), true);
			} else if (short.class.isAssignableFrom(field.getType()) || Short.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, number.shortValue(), true);
			} else if (int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, number.intValue(), true);
			} else if (long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, number.longValue(), true);
			} else if (float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, number.floatValue(), true);
			} else if (double.class.isAssignableFrom(field.getType())
					|| Double.class.isAssignableFrom(field.getType())) {
				ReflectionUtils.setFieldValue(object, field, number.doubleValue(), true);
			}
		}
		/** 如果是blob */
		else if (isFacultyOfBlob(field.getType())) {
			field.setAccessible(true);
			if (Blob.class.isAssignableFrom(value.getClass())) {
				Blob blob = (Blob) value;
				if (Blob.class.isAssignableFrom(field.getType())) {
					ReflectionUtils.setFieldValue(object, field, value, true);
				} else if (Byte[].class.isAssignableFrom(field.getType())) {
					try {
						byte[] byteb = StreamUtils.copyToByteArray(blob.getBinaryStream());
						Byte[] bin = new Byte[byteb.length];
						for (int i = 0; i < bin.length; i++) {
							bin[i] = byteb[i];
						}
						ReflectionUtils.setFieldValue(object, field, bin, true);
					} catch (Exception e) {
						throw new IllegalArgumentException(e);
					}
				} else if (byte[].class.isAssignableFrom(field.getType())) {
					try {
						ReflectionUtils.setFieldValue(object, field,
								StreamUtils.copyToByteArray(blob.getBinaryStream()), true);
					} catch (IOException | SQLException e) {
						throw new IllegalArgumentException(e);
					}
				}
			} else if (Byte[].class.isAssignableFrom(value.getClass())) {
				if (Byte[].class.isAssignableFrom(field.getType())) {
					ReflectionUtils.setFieldValue(object, field, value, true);
				} else if (byte[].class.isAssignableFrom(field.getType())) {
					Byte[] byteb = (Byte[]) value;
					byte[] bin = new byte[byteb.length];
					for (int i = 0; i < bin.length; i++) {
						bin[i] = byteb[i];
					}
					ReflectionUtils.setFieldValue(object, field, bin, true);
				} else if (SerialBlob.class.isAssignableFrom(field.getType())) {
					Byte[] byteb = (Byte[]) value;
					byte[] bin = new byte[byteb.length];
					for (int i = 0; i < bin.length; i++) {
						bin[i] = byteb[i];
					}
					try {
						SerialBlob serialBlob = new SerialBlob(bin);
						ReflectionUtils.setFieldValue(object, field, serialBlob, true);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			} else if (byte[].class.isAssignableFrom(value.getClass())) {
				if (byte[].class.isAssignableFrom(field.getType())) {
					ReflectionUtils.setFieldValue(object, field, value, true);
				} else if (Byte[].class.isAssignableFrom(field.getType())) {
					byte[] byteb = (byte[]) value;
					Byte[] bin = new Byte[byteb.length];
					for (int i = 0; i < bin.length; i++) {
						bin[i] = byteb[i];
					}
					ReflectionUtils.setFieldValue(object, field, bin, true);
				} else if (SerialBlob.class.isAssignableFrom(field.getType())) {
					try {
						SerialBlob serialBlob = new SerialBlob((byte[]) value);
						ReflectionUtils.setFieldValue(object, field, serialBlob, true);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		/** 如果是枚举 */
		else if (isFacultyOfEnum(field.getType())) {
			@SuppressWarnings({ "rawtypes" })
			Class<Enum> enumType = (Class<Enum>) field.getType();
			if (value instanceof String && !((String) value).matches("^[0-9]+$")) {
				ReflectionUtils.setFieldValue(object, field, Enum.valueOf(enumType, (String) value), true);
			} else if (isFacultyOfNumber(value.getClass()) && ((String) value).matches("^[0-9]+$")) {
				int ordinal = 0;
				if (((String) value).matches("^[0-9]+$")) {
					ordinal = Integer.valueOf((String) value);
				} else {
					ordinal = ((Number) value).intValue();
				}
				@SuppressWarnings("rawtypes")
				Enum[] enums = enumType.getEnumConstants();
				for (@SuppressWarnings("rawtypes")
				Enum tempEnum : enums) {
					if (tempEnum.ordinal() == ordinal) {
						ReflectionUtils.setFieldValue(object, field, tempEnum, true);
						break;
					}
				}
			}
		}
		/** 如果是布尔 */
		else if (isFacultyOfBoolean(field.getType())) {
			if (value instanceof Boolean) {
				ReflectionUtils.setFieldValue(object, field, value, true);
			} else if (boolean.class.isAssignableFrom(value.getClass())) {
				ReflectionUtils.setFieldValue(object, field, value, true);
			} else if (value instanceof String) {
				String strValue = ((String) value).toLowerCase();
				if (!strValue.equals("yes") && !strValue.equals("no") && !strValue.equals("true")
						&& !strValue.equals("false")) {
					throw new ClassCastException("can't cast " + strValue + " to boolean");
				} else if (strValue.equals("yes") || strValue.equals("true")) {
					ReflectionUtils.setFieldValue(object, field, true, true);
				} else if (strValue.equals("no") || strValue.equals("false")) {
					ReflectionUtils.setFieldValue(object, field, false, true);
				}
			} else if (isFacultyOfNumber(value.getClass())) {
				int intValue = ((Number) value).intValue();
				if (intValue != 0) {
					ReflectionUtils.setFieldValue(object, field, true, true);
				} else {
					ReflectionUtils.setFieldValue(object, field, false, true);
				}
			}
		}
		/** 如果是char */
		else if (isFacultyOfChar(field.getType())) {
			if (value instanceof String) {
				ReflectionUtils.setFieldValue(object, field, ((String) value).charAt(0), true);
			} else if (isFacultyOfNumber(value.getClass())) {
				int intValue = ((Number) value).intValue();
				ReflectionUtils.setFieldValue(object, field, (char) intValue, true);
			}
		}
	}

	/**
	 * 把数据库列与值的map复制到其它对象
	 * 
	 * @param columnValue 数据库列与列值map
	 * @param object      要设置的对象
	 */
	public static void copyColumnValueToObject(Map<String, Object> columnValue, Object object) {
		List<Field> fields = getAllSqlSupportFields(object.getClass());
		for (Field field : fields) {
			String column = null;
			if (field.isAnnotationPresent(Column.class)) {
				column = field.getAnnotation(Column.class).value();
			} else {
				column = field.getName();
				if (!column.contains(column)) {
					column = StringUtils.humpToLine(column);
				}
			}
			Object value = columnValue.get(column);
			setFieldValue(object, field, value);
		}
	}
}
