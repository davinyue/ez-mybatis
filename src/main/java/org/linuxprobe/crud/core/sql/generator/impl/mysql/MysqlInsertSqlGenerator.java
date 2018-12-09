package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.Column.EnumHandler;
import org.linuxprobe.crud.core.annoatation.Column.LengthHandler;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.sql.generator.InsertSqlGenerator;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.SqlEscapeUtil;

public class MysqlInsertSqlGenerator implements InsertSqlGenerator {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** 生成同一模型的批量插入sql */
	public String toBatchInsertSql(Collection<?> records) {
		if (records == null || records.isEmpty()) {
			throw new OperationNotSupportedException("没有需要被保存的实体");
		} else {
			StringBuilder sqlBuilder = new StringBuilder();
			Iterator<?> iterator = records.iterator();
			boolean isFisrtLoop = true;
			while (iterator.hasNext()) {
				Object entity = iterator.next();
				if (isFisrtLoop) {
					sqlBuilder.append(toInsertSql(entity));
				} else {
					String sql = toInsertSql(entity);
					String sqlValue = sql.substring(sql.indexOf("VALUES") + 6);
					sqlBuilder.append(", " + sqlValue);
				}
				isFisrtLoop = false;
			}
			return sqlBuilder.toString();
		}
	}

	/** 生成插入sql */
	public String toInsertSql(Object record) {
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(record.getClass());
		String table = entityInfo.getTableName();
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO `" + table + "` ");
		StringBuilder clounms = new StringBuilder("(");
		StringBuilder values = new StringBuilder(" VALUES(");
		List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
		if (fieldInfos == null || fieldInfos.isEmpty()) {
			throw new OperationNotSupportedException("该实体类没有任何字段");
		}
		for (int i = 0; i < fieldInfos.size(); i++) {
			FieldInfo fieldInfo = fieldInfos.get(i);
			if (i + 1 == fieldInfos.size()) {
				clounms.append("`" + fieldInfo.getFiledColumn() + "`)");
				values.append(this.getFieldSqlValue(record, fieldInfo.getField()) + ")");
			} else {
				clounms.append("`" + fieldInfo.getFiledColumn() + "`, ");
				values.append(this.getFieldSqlValue(record, fieldInfo.getField()) + ", ");
			}
		}
		sqlBuilder.append(clounms);
		sqlBuilder.append(values);
		return sqlBuilder.toString();
	}

	private Serializable getFieldSqlValue(Object record, Field field) {
		Method getMethod = FieldUtil.getMethodOfFieldGet(record.getClass(), field);
		Object value = null;
		try {
			value = getMethod.invoke(record);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
		if (String.class.isAssignableFrom(field.getType())) {
			String clounmValue = (String) value;
			if (clounmValue != null) {
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					if (column.length() > 0) {
						if (clounmValue.length() > column.length()) {
							if (column.lengthHandler().equals(LengthHandler.Sub)) {
								clounmValue = clounmValue.substring(0, column.length());
							} else {
								throw new IllegalArgumentException(field.getName() + "字段的赋值超出规定长度" + column.length());
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
			Number clounmValue = (Number) value;
			if (clounmValue != null) {
				value = clounmValue.toString();
			} else {
				value = null;
			}
		} else if (Boolean.class.isAssignableFrom(field.getType())) {
			Boolean clounmValue = (Boolean) value;
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
			Date clounmValue = (Date) value;
			if (clounmValue != null) {
				value = "'" + dateFormat.format(clounmValue) + "'";
			} else {
				value = null;
			}
		} else if (Enum.class.isAssignableFrom(field.getType())) {
			Enum<?> clounmValue = (Enum<?>) value;
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
		}
		/** 如果是主键 */
		if (field.isAnnotationPresent(PrimaryKey.class)) {
			if (value == null) {
				PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
				/** 如果指定主键生成模式是uuid */
				if (primaryKey.value().equals(PrimaryKey.Strategy.UUID)) {
					try {
						String uuid = UUID.randomUUID().toString().replaceAll("-", "");
						FieldUtil.setField(record, field, uuid);
						value = "'" + uuid + "'";
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
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}
}
