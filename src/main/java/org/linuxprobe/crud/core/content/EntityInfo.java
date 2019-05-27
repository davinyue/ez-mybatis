package org.linuxprobe.crud.core.content;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.Entity;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.Table;
import org.linuxprobe.crud.core.annoatation.Transient;
import org.linuxprobe.crud.utils.SqlFieldUtil;
import org.linuxprobe.luava.string.StringUtils;

import lombok.Getter;

@Getter
public class EntityInfo {
	public EntityInfo(Class<?> entityType) {
		if (entityType == null) {
			throw new IllegalArgumentException("entityType can't be null");
		}
		if (!entityType.isAnnotationPresent(Entity.class)) {
			throw new IllegalArgumentException(entityType.getName()
					+ " does not have callout org.linuxprobe.crud.core.annoatation.Entity annotation");
		} else {
			this.entityType = entityType;
			/** handle table name */
			this.tableName = StringUtils.humpToLine(entityType.getSimpleName());
			if (entityType.isAnnotationPresent(Table.class)) {
				Table table = entityType.getAnnotation(Table.class);
				if (!table.value().isEmpty()) {
					this.tableName = table.value();
				}
			}
			/** handle field */
			this.fieldInfos = new LinkedList<>();
			List<Field> fields = SqlFieldUtil.getAllSqlSupportFields(entityType);
			if (null != fields && !fields.isEmpty()) {
				for (Field field : fields) {
					if (field.isAnnotationPresent(Transient.class)) {
						continue;
					}
					String fieldName = field.getName();
					String filedColumn = StringUtils.humpToLine(fieldName);
					/** 如果有column注解 */
					if (field.isAnnotationPresent(Column.class)) {
						Column column = field.getAnnotation(Column.class);
						if (!column.value().isEmpty()) {
							filedColumn = column.value();
						}
					}
					/** 如果该字段是主键 */
					FieldInfo fieldInfo = null;
					if (field.isAnnotationPresent(PrimaryKey.class)) {
						fieldInfo = new FieldInfo(field, filedColumn, field.getAnnotation(PrimaryKey.class));
						this.primaryKey = fieldInfo;
					} else {
						fieldInfo = new FieldInfo(field, filedColumn);
					}
					this.fieldInfos.add(fieldInfo);
					this.columnMapFieldInfo.put(fieldInfo.getColumnName(), fieldInfo);
				}
			}
		}
	}

	private Class<?> entityType;
	private String tableName;
	private List<FieldInfo> fieldInfos;
	private Map<String, FieldInfo> columnMapFieldInfo = new HashMap<>();
	private FieldInfo primaryKey;

	/** 是否有名称为cloumn的列 */
	public boolean hasColumn(String column) {
		if (fieldInfos == null || fieldInfos.isEmpty()) {
			return false;
		} else {
			for (FieldInfo fieldInfo : fieldInfos) {
				if (fieldInfo.getColumnName().equals(column)) {
					return true;
				}
			}
		}
		return false;
	}

	/** 是否有名称为field的属性 */
	public boolean hasField(String field) {
		if (fieldInfos == null || fieldInfos.isEmpty()) {
			return false;
		} else {
			for (FieldInfo fieldInfo : fieldInfos) {
				if (fieldInfo.getField().getName().equals(field)) {
					return true;
				}
			}
		}
		return false;
	}

	@Getter
	public static class FieldInfo {
		public FieldInfo(Field field, String columnName, PrimaryKey primaryKey) {
			this.field = field;
			this.columnName = columnName;
			if (primaryKey != null) {
				this.primaryKey = primaryKey;
				this.isPrimaryKey = true;
			}
		}

		public FieldInfo(Field field, String filedColumn) {
			this(field, filedColumn, null);
		}

		private Field field;
		private String columnName;
		private boolean isPrimaryKey = false;
		private PrimaryKey primaryKey;
	}
}
