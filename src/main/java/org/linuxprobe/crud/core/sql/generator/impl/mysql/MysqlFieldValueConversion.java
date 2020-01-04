package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import org.linuxprobe.crud.core.annoatation.*;
import org.linuxprobe.crud.core.annoatation.BooleanHandler.BooleanCustomerType;
import org.linuxprobe.crud.core.annoatation.DateHandler.DateCustomerType;
import org.linuxprobe.crud.core.annoatation.EnumHandler.EnumCustomerType;
import org.linuxprobe.crud.core.validation.FieldValidation;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.utils.SqlFieldUtil;
import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MysqlFieldValueConversion {
    private static MysqlEscape mysqlEscape = new MysqlEscape();

    /**
     * 获取字符串值
     *
     * @param record 保存对象
     * @param field  属性
     */
    private static String getStringValue(Object record, Field field) {
        String fieldValue = (String) ReflectionUtils.getFieldValue(record, field);
        if (fieldValue != null) {
            fieldValue = mysqlEscape.escape(fieldValue);
            fieldValue = mysqlEscape.getQuotation() + fieldValue + mysqlEscape.getQuotation();
        }
        return fieldValue;
    }

    /**
     * 获取时间值
     *
     * @param record 保存对象
     * @param field  属性
     */
    private static String getDateValue(Object record, Field field) {
        Date fieldValue = (Date) ReflectionUtils.getFieldValue(record, field);
        String result = null;
        if (fieldValue != null) {
            if (field.isAnnotationPresent(DateHandler.class)) {
                DateHandler dateHandler = field.getAnnotation(DateHandler.class);
                if (dateHandler.customerType().equals(DateCustomerType.String)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(dateHandler.pattern());
                    result = mysqlEscape.getQuotation() + dateFormat.format(fieldValue) + mysqlEscape.getQuotation();
                } else {
                    result = fieldValue.getTime() + "";
                }
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                result = mysqlEscape.getQuotation() + dateFormat.format(fieldValue) + mysqlEscape.getQuotation();
            }
        }
        return result;
    }

    /**
     * 获取枚举值
     *
     * @param record 保存对象
     * @param field  属性
     */
    private static String getEnumValue(Object record, Field field) {
        Enum<?> fieldValue = (Enum<?>) ReflectionUtils.getFieldValue(record, field);
        String result = null;
        if (fieldValue != null) {
            result = fieldValue.ordinal() + "";
            if (field.isAnnotationPresent(EnumHandler.class)) {
                EnumHandler enumHandler = field.getAnnotation(EnumHandler.class);
                if (enumHandler.value().equals(EnumCustomerType.Name)) {
                    result = mysqlEscape.getQuotation() + fieldValue.name() + mysqlEscape.getQuotation();
                }
            }
        }
        return result;
    }

    /**
     * 获取数字值
     *
     * @param record 保存对象
     * @param field  属性
     */
    private static String getNumberValue(Object record, Field field) {
        Number fieldValue = (Number) ReflectionUtils.getFieldValue(record, field);
        String result = null;
        if (fieldValue != null) {
            result = fieldValue + "";
        }
        return result;
    }

    /**
     * 获取布尔值
     *
     * @param record          保存对象
     * @param field           属性
     * @param enalbeCheckRule 启用校验规则
     */
    private static String getBooleanValue(Object record, Field field) {
        Boolean fieldValue = (Boolean) ReflectionUtils.getFieldValue(record, field);
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
     * @param record 保存对象
     * @param field  属性
     */
    private static String getCharValue(Object record, Field field) {
        Character fieldValue = (Character) ReflectionUtils.getFieldValue(record, field);
        String result = null;
        if (fieldValue != null) {
            result = (int) fieldValue + "";
            if (field.isAnnotationPresent(CharHandler.class)) {
                CharHandler charHandler = field.getAnnotation(CharHandler.class);
                if (charHandler.value().equals(CharHandler.CharCustomerType.ToString)) {
                    result = mysqlEscape.getQuotation() + fieldValue + mysqlEscape.getQuotation();
                }
            }
        }
        return result;
    }

    /**
     * 获取二进制值
     *
     * @param record 保存对象
     * @param field  属性
     */
    private static String getBlobValue(Object record, Field field) {
        Object fieldValue = ReflectionUtils.getFieldValue(record, field);
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
            try {
                result = new String(bin, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            result = mysqlEscape.escape(result);
            result = mysqlEscape.getQuotation() + result + mysqlEscape.getQuotation();
            result = "CONVERT( " + result + ", BINARY )";
        }
        return result;
    }

    /**
     * 值转换
     */
    public static String conversion(Object entity, Field field, boolean enalbeCheckRule) {
        if (enalbeCheckRule) {
            FieldValidation.universalValidation(entity, field);
        }
        String result = null;
        if (SqlFieldUtil.isFacultyOfString(field.getType())) {
            result = getStringValue(entity, field);
        } else if (SqlFieldUtil.isFacultyOfNumber(field.getType())) {
            result = getNumberValue(entity, field);
        } else if (SqlFieldUtil.isFacultyOfBoolean(field.getType())) {
            result = getBooleanValue(entity, field);
        } else if (SqlFieldUtil.isFacultyOfDate(field.getType())) {
            result = getDateValue(entity, field);
        } else if (SqlFieldUtil.isFacultyOfEnum(field.getType())) {
            result = getEnumValue(entity, field);
        } else if (SqlFieldUtil.isFacultyOfChar(field.getType())) {
            result = getCharValue(entity, field);
        } else if (SqlFieldUtil.isFacultyOfBlob(field.getType())) {
            result = getBlobValue(entity, field);
        }
        return result;
    }

    /**
     * 删除模式，不检测id和不生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号
     */
    public static String deleteModelConversion(Object entity, Field field) {
        return conversion(entity, field, false);
    }

    /**
     * 更新模式，不检测id和不生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号
     */
    public static String updateModelConversion(Object entity, Field field) {
        String result = null;
        result = conversion(entity, field, true);
        return result;
    }

    /**
     * 插入模式，检测id和生成id，获取field的值，并把它转换为sql语句的部分，如果是字符串类型的值则会添加上单引号
     */
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
                        ReflectionUtils.setFieldValue(entity, field, uuid, true);
                        result = mysqlEscape.getQuotation() + uuid + mysqlEscape.getQuotation();
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
