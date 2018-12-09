package org.linuxprobe.crud.core.content;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.Entity;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.Transient;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.StringHumpTool;
import lombok.Getter;

@Getter
public class EntityInfo {
	public EntityInfo(Class<?> entityType) {
		if (entityType == null) {
			throw new NullPointerException("entityType can't be null");
		}
		if (!entityType.isAnnotationPresent(Entity.class)) {
			throw new IllegalArgumentException(entityType.getName()
					+ " does not have callout org.linuxprobe.crud.core.annoatation.Entity annotation");
		} else {
			this.entityType = entityType;
			/** handle table name */
			Entity table = entityType.getAnnotation(Entity.class);
			if (table.value().isEmpty()) {
				this.tableName = StringHumpTool.humpToLine2(entityType.getSimpleName(), "_");
			} else {
				this.tableName = table.value();
			}
			/** handle field */
			this.fieldInfos = new LinkedList<>();
			List<Field> fields = FieldUtil.getAllSqlSupportFields(entityType);
			if (null != fields && !fields.isEmpty()) {
				for (Field field : fields) {
					if (field.isAnnotationPresent(Transient.class)) {
						continue;
					}
					String fieldName = field.getName();
					String filedColumn = StringHumpTool.humpToLine2(fieldName, "_");
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
				}
			}
		}
	}

	private Class<?> entityType;
	private String tableName;
	private List<FieldInfo> fieldInfos;
	private FieldInfo primaryKey;

	/** 是否有名称为cloumn的列 */
	public boolean hasColumn(String column) {
		if (fieldInfos == null || fieldInfos.isEmpty()) {
			return false;
		} else {
			for (FieldInfo fieldInfo : fieldInfos) {
				if (fieldInfo.getFiledColumn().equals(column)) {
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
		public FieldInfo(Field field, String filedColumn, PrimaryKey primaryKey) {
			this.field = field;
			this.filedColumn = filedColumn;
			if (primaryKey != null) {
				this.primaryKey = primaryKey;
				this.isPrimaryKey = true;
			}
		}

		public FieldInfo(Field field, String filedColumn) {
			this(field, filedColumn, null);
		}

		private Field field;
		private String filedColumn;
		private boolean isPrimaryKey = false;
		private PrimaryKey primaryKey;
	}
}
