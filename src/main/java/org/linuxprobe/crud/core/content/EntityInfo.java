package org.linuxprobe.crud.core.content;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.Entity;
import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.utils.FieldUtils;
import org.linuxprobe.crud.utils.StringHumpTool;
import lombok.Getter;

@Getter
public class EntityInfo {
	public EntityInfo(Class<?> entityType) {
		if (entityType == null) {
			throw new NullPointerException("entityType can't be null");
		}
		if (!entityType.isAssignableFrom(Entity.class)) {
			throw new IllegalArgumentException(entityType.getName()
					+ " does not have callout org.linuxprobe.crud.core.annoatation.Entity annotation");
		} else {
			this.entityType = entityType;
			/** handle table name */
			if (entityType.isAssignableFrom(Entity.class)) {
				Entity table = entityType.getAnnotation(Entity.class);
				if (table.value().isEmpty()) {
					this.tableName = StringHumpTool.humpToLine2(entityType.getSimpleName(), "_");
				} else {
					this.tableName = table.value();
				}
			} else {
				this.tableName = StringHumpTool.humpToLine2(entityType.getSimpleName(), "_");
			}
			/** handle field */
			this.fieldInfos = new LinkedList<>();
			List<Field> fields = FieldUtils.getAllFields(entityType);
			if (null != fields && !fields.isEmpty()) {
				for (Field field : fields) {
					String fieldName = field.getName();
					String filedColumn = fieldName;
					Class<?> fiedCloumnType = field.getType();
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
						fieldInfo = new FieldInfo(fieldName, filedColumn, fiedCloumnType,
								field.getAnnotation(PrimaryKey.class));
						this.primaryKey = fieldInfo;
					} else {
						fieldInfo = new FieldInfo(fieldName, filedColumn, fiedCloumnType);
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
				if (fieldInfo.getFieldName().equals(field)) {
					return true;
				}
			}
		}
		return false;
	}

	@Getter
	public static class FieldInfo {
		public FieldInfo(String fieldName, String filedColumn, Class<?> fieldType, PrimaryKey primaryKey) {
			this.fieldName = fieldName;
			this.filedColumn = filedColumn;
			this.fieldType = fieldType;
			if (primaryKey != null) {
				this.primaryKey = primaryKey;
				this.isPrimaryKey = true;
			}
		}

		public FieldInfo(String fieldName, String filedColumn, Class<?> fieldType) {
			this(fieldName, filedColumn, fieldType, null);
		}

		private String fieldName;
		private String filedColumn;
		private Class<?> fieldType;
		private boolean isPrimaryKey = false;
		private PrimaryKey primaryKey;
	}
}
