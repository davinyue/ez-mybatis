package org.linuxprobe.crud.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
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
		List<Field> fields = FieldUtil.getAllFields(objClass);
		List<Field> result = new LinkedList<>();
		List<Class<?>> sqlSuperClasss = getSqlSuperClasss();
		for (Field field : fields) {
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

	/** 通过列名设置实体的field值 */
	@SuppressWarnings("unchecked")
	public static void setFieldValue(String column, Object entity, Object value) {
		if (value == null) {
			return;
		}
		FieldInfo fieldInfo = UniversalCrudContent.getEntityInfo(entity.getClass()).getColumnMapFieldInfo().get(column);
		if (fieldInfo != null) {
			Field field = fieldInfo.getField();
			/** 如果是字符串 */
			if (isFacultyOfString(field.getType())) {
				FieldUtil.setField(entity, field, value.toString());
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
							value.getClass().getName() + " can't cas to " + field.getType().getName());
				}
				if (java.sql.Date.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, new java.sql.Date(timestamp));
				} else if (Timestamp.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, new Timestamp(timestamp));
				} else if (Time.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, new Time(timestamp));
				} else if (Date.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, new Date(timestamp));
				}
			}
			/** 如果是数值 */
			else if (isFacultyOfNumber(field.getType())) {
				Number number = (Number) value;
				if (BigDecimal.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, new BigDecimal(number.toString()));
				} else if (byte.class.isAssignableFrom(field.getType())
						|| Byte.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, number.byteValue());
				} else if (short.class.isAssignableFrom(field.getType())
						|| Short.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, number.shortValue());
				} else if (int.class.isAssignableFrom(field.getType())
						|| Integer.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, number.intValue());
				} else if (long.class.isAssignableFrom(field.getType())
						|| Long.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, number.longValue());
				} else if (float.class.isAssignableFrom(field.getType())
						|| Float.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, number.floatValue());
				} else if (double.class.isAssignableFrom(field.getType())
						|| Double.class.isAssignableFrom(field.getType())) {
					FieldUtil.setField(entity, field, number.doubleValue());
				}
			}
			/** 如果是blob */
			else if (isFacultyOfBlob(field.getType())) {
				field.setAccessible(true);
				if (Blob.class.isAssignableFrom(value.getClass())) {
					Blob blob = (Blob) value;
					if (Blob.class.isAssignableFrom(field.getType())) {
						FieldUtil.setField(entity, field, value);
					} else if (Byte[].class.isAssignableFrom(field.getType())) {
						try {
							byte[] byteb = StreamUtils.copyToByteArray(blob.getBinaryStream());
							Byte[] bin = new Byte[byteb.length];
							for (int i = 0; i < bin.length; i++) {
								bin[i] = byteb[i];
							}
							FieldUtil.setField(entity, field, (Object) bin);
						} catch (Exception e) {
							throw new IllegalArgumentException(e);
						}
					} else if (byte[].class.isAssignableFrom(field.getType())) {
						try {
							FieldUtil.setField(entity, field, StreamUtils.copyToByteArray(blob.getBinaryStream()));
						} catch (IOException | SQLException e) {
							throw new IllegalArgumentException(e);
						}
					}
				} else if (Byte[].class.isAssignableFrom(value.getClass())) {
					if (Byte[].class.isAssignableFrom(field.getType())) {
						FieldUtil.setField(entity, field, value);
					} else if (byte[].class.isAssignableFrom(field.getType())) {
						Byte[] byteb = (Byte[]) value;
						byte[] bin = new byte[byteb.length];
						for (int i = 0; i < bin.length; i++) {
							bin[i] = byteb[i];
						}
						FieldUtil.setField(entity, field, bin);
					}
				} else if (byte[].class.isAssignableFrom(value.getClass())) {
					if (byte[].class.isAssignableFrom(field.getType())) {
						FieldUtil.setField(entity, field, value);
					} else if (Byte[].class.isAssignableFrom(field.getType())) {
						byte[] byteb = (byte[]) value;
						Byte[] bin = new Byte[byteb.length];
						for (int i = 0; i < bin.length; i++) {
							bin[i] = byteb[i];
						}
						FieldUtil.setField(entity, field, (Object) bin);
					}
				}
			}
			/** 如果是枚举 */
			else if (isFacultyOfEnum(field.getType())) {
				@SuppressWarnings({ "rawtypes" })
				Class<Enum> enumType = (Class<Enum>) field.getType();
				if (value instanceof String) {
					FieldUtil.setField(entity, field, Enum.valueOf(enumType, (String) value));
				} else if (isFacultyOfNumber(value.getClass())) {
					int ordinal = ((Number) value).intValue();
					@SuppressWarnings("rawtypes")
					Enum[] enums = enumType.getEnumConstants();
					for (@SuppressWarnings("rawtypes")
					Enum tempEnum : enums) {
						if (tempEnum.ordinal() == ordinal) {
							FieldUtil.setField(entity, field, tempEnum);
							break;
						}
					}
				}
			}
		}

	}
}
