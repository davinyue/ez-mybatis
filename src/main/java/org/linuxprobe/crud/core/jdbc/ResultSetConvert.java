package org.linuxprobe.crud.core.jdbc;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.utils.SqlFieldUtil;
import org.linuxprobe.luava.string.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ResultSetConvert {
    public static <T> T convert(ResultSet resultSet, Class<T> type) throws SQLException {
        T instance = null;
        try {
            instance = type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(e);
        }
        List<Field> fields = SqlFieldUtil.getAllSqlSupportFields(type);
        List<String> columns = getColumns(resultSet);
        for (Field field : fields) {
            String columnName = null;
            if (field.isAnnotationPresent(Column.class)) {
                columnName = field.getAnnotation(Column.class).value();
            } else {
                columnName = field.getName();
                if (!columns.contains(columnName)) {
                    columnName = StringUtils.humpToLine(columnName);
                }
            }
            Object value = null;
            try {
                if (SqlFieldUtil.isFacultyOfBlob(field.getType())) {
                    value = resultSet.getBlob(columnName);
                } else if (SqlFieldUtil.isFacultyOfBoolean(field.getType())) {
                    value = resultSet.getBoolean(columnName);
                } else if (SqlFieldUtil.isFacultyOfChar(field.getType())) {
                    try {
                        value = resultSet.getString(columnName);
                    } catch (Exception e) {
                        value = resultSet.getInt(columnName);
                    }
                } else if (SqlFieldUtil.isFacultyOfDate(field.getType())) {
                    value = resultSet.getDate(columnName);
                } else if (SqlFieldUtil.isFacultyOfEnum(field.getType())) {
                    try {
                        value = resultSet.getString(columnName);
                    } catch (Exception e) {
                        value = resultSet.getInt(columnName);
                    }
                } else if (SqlFieldUtil.isFacultyOfNumber(field.getType())) {
                    if (BigDecimal.class.isAssignableFrom(field.getType())) {
                        value = resultSet.getBigDecimal(columnName);
                    } else if (byte.class.isAssignableFrom(field.getType())
                            || Byte.class.isAssignableFrom(field.getType())) {
                        value = resultSet.getByte(columnName);
                    } else if (short.class.isAssignableFrom(field.getType())
                            || Short.class.isAssignableFrom(field.getType())) {
                        value = resultSet.getShort(columnName);
                    } else if (int.class.isAssignableFrom(field.getType())
                            || Integer.class.isAssignableFrom(field.getType())) {
                        value = resultSet.getInt(columnName);
                    } else if (long.class.isAssignableFrom(field.getType())
                            || Long.class.isAssignableFrom(field.getType())) {
                        value = resultSet.getLong(columnName);
                    } else if (float.class.isAssignableFrom(field.getType())
                            || Float.class.isAssignableFrom(field.getType())) {
                        value = resultSet.getFloat(columnName);
                    } else if (double.class.isAssignableFrom(field.getType())
                            || Double.class.isAssignableFrom(field.getType())) {
                        value = resultSet.getDouble(columnName);
                    }
                } else if (SqlFieldUtil.isFacultyOfString(field.getType())) {
                    value = resultSet.getString(columnName);
                }
            } catch (SQLException e) {
                continue;
            }
            SqlFieldUtil.setFieldValue(instance, field, value);
        }
        return instance;
    }

    public static List<String> getColumns(ResultSet resultSet) {
        int count = 0;
        try {
            count = resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        List<String> columns = new LinkedList<>();
        while (count > 0) {
            try {
                columns.add(resultSet.getMetaData().getColumnName(count));
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
            count--;
        }
        return columns;
    }
}
