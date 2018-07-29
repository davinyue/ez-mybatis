package org.linuxprobe.crud.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.exception.UnknownTableException;
import org.linuxprobe.crud.model.BaseModel;
import org.linuxprobe.crud.persistence.annotation.Column;
import org.linuxprobe.crud.persistence.annotation.PrimaryKey;
import org.linuxprobe.crud.persistence.annotation.Table;
import org.linuxprobe.crud.utils.StringHumpTool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Sqlr {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String toInsertSql(Object entity) {
		if (BaseModel.class.isAssignableFrom(entity.getClass())) {
			try {
				return ((BaseModel) entity).getSqlr().toInsertSql();
			} catch (Exception e) {
				return null;
			}
		}
		String table = Sqlr.getTable(entity);
		if (table == null || table.trim().isEmpty()) {
			throw new UnknownTableException("请在实体类上标注注解@Table(\"table\")");
		}
		StringBuffer sqlBuffer = new StringBuffer("insert into " + table + " ");
		StringBuffer clounms = new StringBuffer("(");
		StringBuffer values = new StringBuffer(" values(");
		List<Attribute> attributes = getAttributes(entity);
		if (attributes.isEmpty()) {
			throw new OperationNotSupportedException("该实体类没有任何字段");
		}
		for (int i = 0; i < attributes.size(); i++) {
			Attribute attribute = attributes.get(i);
			if (i + 1 == attributes.size()) {
				clounms.append(attribute.getName() + ")");
				values.append(attribute.getValue() + ")");
			} else {
				clounms.append(attribute.getName() + ", ");
				values.append(attribute.getValue() + ", ");
			}
		}
		sqlBuffer.append(clounms);
		sqlBuffer.append(values);
		return sqlBuffer.toString();
	}

	public static String toGlobalUpdateSql(Object entity) {
		if (BaseModel.class.isAssignableFrom(entity.getClass())) {
			try {
				return ((BaseModel) entity).getSqlr().toGlobalUpdateSql();
			} catch (Exception e) {
				return null;
			}
		}
		String table = Sqlr.getTable(entity);
		StringBuffer sqlBuffer = new StringBuffer("update " + table + " set ");
		List<Attribute> attributes = getAttributes(entity);
		Attribute primaryKey = getPrimaryKey(entity);
		if(primaryKey == null){
			throw new OperationNotSupportedException("请使用@PrimaryKey指定主键");
		}
		attributes.remove(primaryKey);
		for (int i = 0; i < attributes.size(); i++) {
			Attribute attribute = attributes.get(i);
			if (attribute.getName().equals("create_time") || attribute.getName().equals("creater_id")) {
				continue;
			}
			sqlBuffer.append(attribute.getName() + " = " + attribute.getValue() + ", ");
		}
		if (sqlBuffer.indexOf(",") != -1)
			sqlBuffer.replace(sqlBuffer.length() - 2, sqlBuffer.length(), " ");
		sqlBuffer.append("where " + primaryKey.getName() + " = " + primaryKey.getValue());
		return sqlBuffer.toString();
	}

	public static String toLocalUpdateSql(Object entity) {
		if (BaseModel.class.isAssignableFrom(entity.getClass())) {
			try {
				return ((BaseModel) entity).getSqlr().toGlobalUpdateSql();
			} catch (Exception e) {
				return null;
			}
		}
		String table = Sqlr.getTable(entity);
		StringBuffer sqlBuffer = new StringBuffer("update " + table + " set ");
		List<Attribute> attributes = getAttributes(entity);
		Attribute primaryKey = getPrimaryKey(entity);
		attributes.remove(primaryKey);
		for (int i = 0; i < attributes.size(); i++) {
			Attribute attribute = attributes.get(i);
			if (attribute.getValue() != null) {
				sqlBuffer.append(attribute.getName() + " = " + attribute.getValue() + ", ");
			}
		}
		if (sqlBuffer.indexOf(",") != -1)
			sqlBuffer.replace(sqlBuffer.length() - 2, sqlBuffer.length(), " ");
		else {
			sqlBuffer.append(primaryKey.getName() + " = " + primaryKey.getValue()+" ");
		}
		sqlBuffer.append("where " + primaryKey.getName() + " = " + primaryKey.getValue());
		return sqlBuffer.toString();
	}

	private static <T extends Object> String getTable(T entity) {
		Class<?> entityClass = entity.getClass();
		boolean classHasTableAnno = entityClass.isAnnotationPresent(Table.class);
		if (classHasTableAnno) {
			Table annotation = entityClass.getAnnotation(Table.class);
			return annotation.value();
		}
		return null;
	}

	private static List<Attribute> getAttributes(Object entity) {
		List<Attribute> result = new LinkedList<>();
		List<Field> fields = Arrays.asList(entity.getClass().getDeclaredFields());
		fields = new ArrayList<Field>(fields);
		Class<?> superClass = entity.getClass().getSuperclass();
		if (superClass != null) {
			for (;;) {
				fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
				if (superClass.equals(Object.class)) {
					break;
				} else {
					superClass = superClass.getSuperclass();
				}
			}
		}
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			String tableColumn = field.getName();
			boolean needAppend = true;
			/** 获取本次参数的方法 */
			String afterOfGet = tableColumn.substring(0, 1).toUpperCase() + tableColumn.substring(1);
			Method getCurrnetClounm = null;
			try {
				getCurrnetClounm = entity.getClass().getMethod("get" + afterOfGet);
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
					clounmValue = clounmValue.replaceAll("\\\\", "\\\\\\\\");
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
				} else {
					value = null;
				}
			} else {
				needAppend = false;
			}
			if (needAppend) {
				boolean isPrimaryKey = false;
				tableColumn = StringHumpTool.humpToLine2(tableColumn, "_");
				if (field.isAnnotationPresent(PrimaryKey.class)) {
					isPrimaryKey = true;
					PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
					/** 如果指定主键生成模式是uuid */
					if (primaryKey.value().equals(PrimaryKey.Strategy.UUID)) {
						value = "'" + UUID.randomUUID().toString() + "'";
					}
					/** 如果指定主键生成模式是程序指定 */
					else if (primaryKey.value().equals(PrimaryKey.Strategy.ASSIGNED)) {
						throw new NullPointerException("主键不能为空");
					}
				}
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					String strColumn = column.value();
					if (strColumn != null && !strColumn.trim().isEmpty()) {
						tableColumn = strColumn;
					}
				}
				result.add(new Attribute(tableColumn, value, isPrimaryKey));
			}
		}
		return result;
	}

	private static Attribute getPrimaryKey(Object entity) {
		List<Attribute> attributes = getAttributes(entity);
		for (Attribute attribute : attributes) {
			if (attribute.isPrimaryKey) {
				return attribute;
			}
		}
		return null;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Attribute {
		private String name;
		private String value;
		boolean isPrimaryKey;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Attribute other = (Attribute) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}
}
