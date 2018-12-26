package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.linuxprobe.crud.core.annoatation.BlobHandler;
import org.linuxprobe.crud.core.annoatation.BooleanHandler;
import org.linuxprobe.crud.core.annoatation.BooleanHandler.BooleanCustomerType;
import org.linuxprobe.crud.core.annoatation.CharHandler;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.DateHandler;
import org.linuxprobe.crud.core.annoatation.DateHandler.DateCustomerType;
import org.linuxprobe.crud.core.annoatation.EnumHandler;
import org.linuxprobe.crud.core.annoatation.EnumHandler.EnumCustomerType;
import org.linuxprobe.crud.core.annoatation.NumberHandler;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.StringHandler;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.SqlEscapeUtil;
import org.linuxprobe.crud.utils.SqlFieldUtil;
import org.springframework.util.StreamUtils;

public class MysqlFieldValueConversion {
	/**
	 * 获取字符串值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getStringValue(Object record, Field field, boolean enalbeCheckRule) {
		String fieldValue = (String) FieldUtil.getFieldValue(record, field);
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (fieldValue != null) {
			if (enalbeCheckRule && field.isAnnotationPresent(StringHandler.class)) {
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
		fieldValue = SqlEscapeUtil.mysqlEscape(fieldValue);
		fieldValue = "'" + fieldValue + "'";
		return fieldValue;
	}

	/**
	 * 获取时间值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getDateValue(Object record, Field field, boolean enalbeCheckRule) {
		Date fieldValue = (Date) FieldUtil.getFieldValue(record, field);
		String result = null;
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (fieldValue != null) {
			if (field.isAnnotationPresent(DateHandler.class)) {
				DateHandler dateHandler = field.getAnnotation(DateHandler.class);
				if (dateHandler.customerType().equals(DateCustomerType.String)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(dateHandler.pattern());
					result = "'" + dateFormat.format(fieldValue) + "'";
				} else {
					result = fieldValue.getTime() + "";
				}
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				result = "'" + dateFormat.format(fieldValue) + "'";
			}
		}
		return result;
	}

	/**
	 * 获取枚举值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getEnumValue(Object record, Field field, boolean enalbeCheckRule) {
		Enum<?> fieldValue = (Enum<?>) FieldUtil.getFieldValue(record, field);
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		String result = null;
		if (fieldValue != null) {
			result = fieldValue.ordinal() + "";
			if (field.isAnnotationPresent(EnumHandler.class)) {
				EnumHandler enumHandler = field.getAnnotation(EnumHandler.class);
				if (enumHandler.value().equals(EnumCustomerType.Name)) {
					result = "'" + fieldValue.name() + "'";
				}
			}
		}
		return result;
	}

	/**
	 * 获取数字值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getNumberValue(Object record, Field field, boolean enalbeCheckRule) {
		Number fieldValue = (Number) FieldUtil.getFieldValue(record, field);
		String result = fieldValue + "";
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		if (enalbeCheckRule && field.isAnnotationPresent(NumberHandler.class)) {
			NumberHandler numberHandler = field.getAnnotation(NumberHandler.class);
			BigDecimal bigDecimalValue = new BigDecimal(result);
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
		return result;
	}

	/**
	 * 获取数字值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getBooleanValue(Object record, Field field, boolean enalbeCheckRule) {
		Boolean fieldValue = (Boolean) FieldUtil.getFieldValue(record, field);
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		String result = null;
		if (fieldValue != null) {
			if (fieldValue) {
				result = "1";
			} else {
				result = "0";
			}
			if (field.isAnnotationPresent(BooleanHandler.class)) {
				BooleanHandler booleanHandler = field.getAnnotation(BooleanHandler.class);
				if (booleanHandler.value().equals(BooleanCustomerType.YesAndNo)) {
					if (fieldValue) {
						result = "'yes'";
					} else {
						result = "'no'";
					}
				} else if (booleanHandler.value().equals(BooleanCustomerType.TrueAndFalse)) {
					if (fieldValue) {
						result = "'true'";
					} else {
						result = "'false'";
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获取字符值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getCharValue(Object record, Field field, boolean enalbeCheckRule) {
		Character fieldValue = (Character) FieldUtil.getFieldValue(record, field);
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		String result = null;
		if (fieldValue != null) {
			result = (int) fieldValue + "";
			if (enalbeCheckRule && field.isAnnotationPresent(CharHandler.class)) {
				CharHandler charHandler = field.getAnnotation(CharHandler.class);
				if (charHandler.value().equals(CharHandler.CharCustomerType.ToString)) {
					result = "'" + fieldValue + "'";
				}
			}
		}
		return result;
	}

	/**
	 * 获取二进制值
	 * 
	 * @param record          保存对象
	 * @param field           属性
	 * @param enalbeCheckRule 启用校验规则
	 */
	private static String getBlobValue(Object record, Field field, boolean enalbeCheckRule) {
		Object fieldValue = FieldUtil.getFieldValue(record, field);
		if (enalbeCheckRule && field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.notNull() && fieldValue == null) {
				throw new IllegalArgumentException(
						"in " + record.getClass().getName() + "," + field.getName() + " can't be null");
			}
		}
		String result = null;
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
			if (enalbeCheckRule && field.isAnnotationPresent(BlobHandler.class)) {
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
			try {
				result = new String(bin, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			result = SqlEscapeUtil.mysqlEscape(result);
			result = "'" + result + "'";
			result = "CONVERT( " + result + ", BINARY )";
		}
		return result;
	}

	/** 值转换 */
	public static String conversion(Object entity, Field field, boolean enalbeCheckRule) {
		String result = null;
		if (SqlFieldUtil.isFacultyOfString(field.getType())) {
			result = getStringValue(entity, field, enalbeCheckRule);
		} else if (SqlFieldUtil.isFacultyOfNumber(field.getType())) {
			result = getNumberValue(entity, field, enalbeCheckRule);
		} else if (SqlFieldUtil.isFacultyOfBoolean(field.getType())) {
			result = getBooleanValue(entity, field, enalbeCheckRule);
		} else if (SqlFieldUtil.isFacultyOfDate(field.getType())) {
			result = getDateValue(entity, field, enalbeCheckRule);
		} else if (SqlFieldUtil.isFacultyOfEnum(field.getType())) {
			result = getEnumValue(entity, field, enalbeCheckRule);
		} else if (SqlFieldUtil.isFacultyOfChar(field.getType())) {
			result = getCharValue(entity, field, enalbeCheckRule);
		} else if (SqlFieldUtil.isFacultyOfBlob(field.getType())) {
			result = getBlobValue(entity, field, enalbeCheckRule);
		}
		return result;
	}

	/** 删除模式，不检测id和不生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号 */
	public static String deleteModelConversion(Object entity, Field field) {
		String result = null;
		result = conversion(entity, field, false);
		return result;
	}

	/** 更新模式，不检测id和不生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号 */
	public static String updateModelConversion(Object entity, Field field) {
		String result = null;
		result = conversion(entity, field, true);
		return result;
	}

	/** 插入模式，检测id和生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号 */
	public static String insertModelConversion(Object entity, Field field) {
		String result = updateModelConversion(entity, field);
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
