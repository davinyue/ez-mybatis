package org.linuxprobe.crud.core.sql.generator.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import org.linuxprobe.crud.core.annoatation.BlobHandler;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.NumberHandler;
import org.linuxprobe.crud.core.annoatation.StringHandler;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.SqlFieldUtil;
import org.springframework.util.StreamUtils;

public class AnnoatationRuleCheck {
	/**
	 * 字符串检查
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void stringCheck(Object record, Field field) {
		String fieldValue = (String) FieldUtil.getFieldValue(record, field);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (fieldValue != null) {
			if (field.isAnnotationPresent(StringHandler.class)) {
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
	}

	/**
	 * 时间检查
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void dateCheck(Object record, Field field) {
		Date fieldValue = (Date) FieldUtil.getFieldValue(record, field);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
	}

	/**
	 * 枚举检查
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void enumCheck(Object record, Field field) {
		Enum<?> fieldValue = (Enum<?>) FieldUtil.getFieldValue(record, field);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
	}

	/**
	 * 数字值检查
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void numberCheck(Object record, Field field) {
		Number fieldValue = (Number) FieldUtil.getFieldValue(record, field);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (fieldValue != null) {
			if (field.isAnnotationPresent(NumberHandler.class)) {
				NumberHandler numberHandler = field.getAnnotation(NumberHandler.class);
				BigDecimal bigDecimalValue = new BigDecimal(fieldValue + "");
				if (!numberHandler.minValue().isEmpty()) {
					BigDecimal bigDecimalMinValue = new BigDecimal(numberHandler.minValue());
					if (bigDecimalMinValue.compareTo(bigDecimalValue) > 0) {
						throw new IllegalArgumentException("in " + record.getClass().getName() + "," + field.getName()
								+ " minValue is " + numberHandler.minValue());
					}
				}
				if (!numberHandler.maxValue().isEmpty()) {
					BigDecimal bigDecimalMaxValue = new BigDecimal(numberHandler.maxValue());
					if (bigDecimalMaxValue.compareTo(bigDecimalValue) < 0) {
						throw new IllegalArgumentException("in " + record.getClass().getName() + "," + field.getName()
								+ " maxValue is " + numberHandler.maxValue());
					}
				}
			}
		}
	}

	/**
	 * 获取数字值
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void booleanCehck(Object record, Field field) {
		Boolean fieldValue = (Boolean) FieldUtil.getFieldValue(record, field);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
	}

	/**
	 * 字符检查
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void charCheck(Object record, Field field) {
		Character fieldValue = (Character) FieldUtil.getFieldValue(record, field);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
	}

	/**
	 * blob检查
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void blobCheck(Object record, Field field) {
		Object fieldValue = FieldUtil.getFieldValue(record, field);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (fieldValue != null) {
			byte[] bin = null;
			if (Blob.class.isAssignableFrom(field.getType())) {
				Blob blob = (Blob) fieldValue;
				try {
					bin = StreamUtils.copyToByteArray(blob.getBinaryStream());
				} catch (IOException | SQLException e) {
					throw new IllegalArgumentException(e);
				}
			} else if (byte[].class.isAssignableFrom(field.getType())) {
				bin = (byte[]) fieldValue;
			} else if (Byte[].class.isAssignableFrom(field.getType())) {
				Byte[] binB = (Byte[]) fieldValue;
				bin = new byte[binB.length];
				for (int i = 0; i < binB.length; i++) {
					bin[i] = binB[i];
				}
			}
			if (field.isAnnotationPresent(BlobHandler.class)) {
				BlobHandler blobHandler = field.getAnnotation(BlobHandler.class);
				if (blobHandler.minSize() != 0) {
					if (bin.length < blobHandler.minSize()) {
						throw new IllegalArgumentException("in " + record.getClass().getName() + "," + field.getName()
								+ " minSize is " + blobHandler.minSize());
					}
				}
				if (blobHandler.maxSize() != 0) {
					if (bin.length > blobHandler.maxSize()) {
						throw new IllegalArgumentException("in " + record.getClass().getName() + "," + field.getName()
								+ " maxSize is " + blobHandler.maxSize());
					}
				}
			}
		}
	}

	public static void check(Object entity, Field field) {
		if (SqlFieldUtil.isFacultyOfString(field.getType())) {
			stringCheck(entity, field);
		} else if (SqlFieldUtil.isFacultyOfNumber(field.getType())) {
			numberCheck(entity, field);
		} else if (SqlFieldUtil.isFacultyOfBoolean(field.getType())) {
			booleanCehck(entity, field);
		} else if (SqlFieldUtil.isFacultyOfDate(field.getType())) {
			dateCheck(entity, field);
		} else if (SqlFieldUtil.isFacultyOfEnum(field.getType())) {
			enumCheck(entity, field);
		} else if (SqlFieldUtil.isFacultyOfChar(field.getType())) {
			charCheck(entity, field);
		} else if (SqlFieldUtil.isFacultyOfBlob(field.getType())) {
			blobCheck(entity, field);
		}
	}
}
