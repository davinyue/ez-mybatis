package org.linuxprobe.crud.core.sql.generator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.Transient;
import org.linuxprobe.crud.core.annoatation.Column.EnumHandler;
import org.linuxprobe.crud.core.annoatation.Column.LengthHandler;
import org.linuxprobe.crud.core.sql.field.ColumnField;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.utils.EntityUtils;
import org.linuxprobe.crud.utils.FieldUtils;
import org.linuxprobe.crud.utils.SqlEscapeUtil;
import org.linuxprobe.crud.utils.StringHumpTool;

/** 插入sql语句生成器 */
public class InsertSqlGenerator {

	private InsertSqlGenerator() {
	}

	private volatile static InsertSqlGenerator instance = new InsertSqlGenerator();

	public static InsertSqlGenerator getInstance() {
		return instance;
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** 生成插入sql */
	public static String toInsertSql(Object entity) {
		String table = EntityUtils.getTable(entity.getClass());
		StringBuffer sqlBuffer = new StringBuffer("insert into `" + table + "` ");
		StringBuffer clounms = new StringBuffer("(");
		StringBuffer values = new StringBuffer(" values(");
		List<ColumnField> columnFields = getColumnFields(entity);
		if (columnFields.isEmpty()) {
			throw new OperationNotSupportedException("该实体类没有任何字段");
		}
		for (int i = 0; i < columnFields.size(); i++) {
			ColumnField columnField = columnFields.get(i);
			if (i + 1 == columnFields.size()) {
				clounms.append("`" + columnField.getColumn() + "`)");
				values.append(columnField.getValue() + ")");
			} else {
				clounms.append("`" + columnField.getColumn() + "`, ");
				values.append(columnField.getValue() + ", ");
			}
		}
		sqlBuffer.append(clounms);
		sqlBuffer.append(values);
		return sqlBuffer.toString();
	}

	/** 生成同一模型的批量插入sql */
	public static String toBatchInsertSql(List<?> entitys) {
		if (entitys == null || entitys.isEmpty()) {
			throw new OperationNotSupportedException("没有需要被保存的实体");
		} else {
			StringBuffer sqlBuffer = new StringBuffer();
			for (int i = 0; i < entitys.size(); i++) {
				Object entity = entitys.get(i);
				if (i == 0) {
					sqlBuffer.append(toInsertSql(entity));
				} else {
					String sql = toInsertSql(entity);
					String sqlValue = sql.substring(sql.indexOf("values") + 6);
					sqlBuffer.append(", " + sqlValue);
				}
			}
			return sqlBuffer.toString();
		}
	}

	private static List<ColumnField> getColumnFields(Object entity) {
		List<ColumnField> result = new LinkedList<>();
		List<Field> fields = FieldUtils.getAllFields(entity.getClass());
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			ColumnField columnField = new ColumnField();
			String fieldName = field.getName();
			columnField.setName(fieldName);
			boolean needAppend = true;
			/** 获取本次参数的方法 */
			String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getCurrnetClounm = null;
			try {
				getCurrnetClounm = entity.getClass().getMethod("get" + funSuffix);
			} catch (NoSuchMethodException | SecurityException e) {
				continue;
			}
			String value = null;
			Object fieldValue = null;
			try {
				/** 得到本次参数 */
				fieldValue = getCurrnetClounm.invoke(entity);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				continue;
			}
			if (String.class.isAssignableFrom(field.getType())) {
				String clounmValue = (String) fieldValue;
				if (clounmValue != null) {
					if (field.isAnnotationPresent(Column.class)) {
						Column column = field.getAnnotation(Column.class);
						if (column.length() > 0) {
							if (clounmValue.length() > column.length()) {
								if (column.lengthHandler().equals(LengthHandler.Sub)) {
									clounmValue = clounmValue.substring(0, column.length());
								} else {
									throw new IllegalArgumentException(
											field.getName() + "字段的赋值超出规定长度" + column.length());
								}
							}
						}
					}
					clounmValue = SqlEscapeUtil.escape(clounmValue);
					value = "'" + clounmValue + "'";
				} else {
					value = null;
				}
			} else if (Number.class.isAssignableFrom(field.getType())) {
				Number clounmValue = (Number) fieldValue;
				if (clounmValue != null) {
					value = clounmValue.toString();
				} else {
					value = null;
				}
			} else if (Boolean.class.isAssignableFrom(field.getType())) {
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
			} else if (Date.class.isAssignableFrom(field.getType())) {
				Date clounmValue = (Date) fieldValue;
				if (clounmValue != null) {
					value = "'" + dateFormat.format(clounmValue) + "'";
				} else {
					value = null;
				}
			} else if (Enum.class.isAssignableFrom(field.getType())) {
				Enum<?> clounmValue = (Enum<?>) fieldValue;
				if (clounmValue != null) {
					value = clounmValue.ordinal() + "";
					/** 如果成员变量有Column注解 */
					if (field.isAnnotationPresent(Column.class)) {
						Column column = field.getAnnotation(Column.class);
						if (column.enumHandler().equals(EnumHandler.Name)) {
							value = "'" + clounmValue.toString() + "'";
						}
					}
				} else {
					value = null;
				}
			} else {
				needAppend = false;
			}
			if (needAppend) {
				/** 如果有忽略该字段注解 */
				if (field.isAnnotationPresent(Transient.class)) {
					continue;
				}
				/** 主键检测 */
				boolean isPrimaryKey = false;
				if (field.isAnnotationPresent(PrimaryKey.class)) {
					isPrimaryKey = true;
					if (value == null) {
						PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
						/** 如果指定主键生成模式是uuid */
						if (primaryKey.value().equals(PrimaryKey.Strategy.UUID)) {
							try {
								String uuid = UUID.randomUUID().toString();
								Method setFun = entity.getClass().getMethod("set" + funSuffix, String.class);
								setFun.invoke(entity, uuid);
								value = "'" + uuid + "'";
							} catch (Exception e) {
								throw new OperationNotSupportedException("未找到主键的set方法", e);
							}
						}
						/** 如果指定主键生成模式是程序指定 */
						else if (primaryKey.value().equals(PrimaryKey.Strategy.ASSIGNED)) {
							throw new NullPointerException("主键不能为空");
						}
					}
				}
				/** 设置数据库列是成员名称转下划线 */
				columnField.setColumn(StringHumpTool.humpToLine2(fieldName, "_"));
				/** 如果成员变量有Column注解 */
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					if (column.notNull() && value == null) {
						throw new IllegalArgumentException(fieldName + "不能为空");
					}
					String strColumn = column.value();
					if (strColumn != null && !strColumn.trim().isEmpty()) {
						columnField.setColumn(strColumn);
					}
				}
				columnField.setIsPrimaryKey(isPrimaryKey);
				columnField.setValue(value);
				result.add(columnField);
			}
		}
		/** 去重 */
		List<ColumnField> distinctResult = new LinkedList<>();
		if (result != null) {
			for (ColumnField columnField : result) {
				if (!distinctResult.contains(columnField)) {
					distinctResult.add(columnField);
				}
			}
		}
		return distinctResult;
	}
}
