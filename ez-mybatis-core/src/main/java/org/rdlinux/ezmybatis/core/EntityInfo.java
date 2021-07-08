package org.rdlinux.ezmybatis.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.utils.Assert;
import org.rdlinux.ezmybatis.core.utils.HumpLineStringUtils;
import org.rdlinux.ezmybatis.core.utils.SqlReflectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EntityInfo {
    private Class<?> entityClass;
    private String tableName;
    private List<FieldInfo> fieldInfos;
    private Map<String, FieldInfo> columnMapFieldInfo;
    private FieldInfo primaryKeyInfo;

    public EntityInfo(Class<?> entityClass) {
        Assert.notNull(entityClass);
        Assert.isTrue(entityClass.isAnnotationPresent(Entity.class),
                "entity class must annotation present javax.persistence.Entity");
        this.tableName = HumpLineStringUtils.humpToLine(entityClass.getSimpleName());
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table annotation = entityClass.getAnnotation(Table.class);
            String tn = annotation.name();
            if (StringUtils.isNotEmpty(tn)) {
                this.tableName = tn;
            }
        }
        this.entityClass = entityClass;
        this.fieldInfos = new LinkedList<>();
        this.columnMapFieldInfo = new HashMap<>((int) (this.fieldInfos.size() / 0.75) + 1);
        List<Field> fields = SqlReflectionUtils.getSupportFields(entityClass);
        fields.forEach(field -> {
            FieldInfo fieldInfo = new FieldInfo(field);
            this.fieldInfos.add(fieldInfo);
            if (fieldInfo.isPrimaryKey) {
                this.primaryKeyInfo = fieldInfo;
            }
        });
        this.fieldInfos.forEach(fieldInfo -> this.columnMapFieldInfo.put(fieldInfo.getColumnName(), fieldInfo));
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<FieldInfo> getFieldInfos() {
        return this.fieldInfos;
    }

    public Map<String, FieldInfo> getColumnMapFieldInfo() {
        return this.columnMapFieldInfo;
    }

    public FieldInfo getPrimaryKeyInfo() {
        return this.primaryKeyInfo;
    }

    public static class FieldInfo {
        private Field field;
        private String columnName;
        private boolean isPrimaryKey = false;
        private TypeHandler<?> typeHandler;

        public FieldInfo(Field field) {
            field.setAccessible(true);
            this.field = field;
            this.columnName = HumpLineStringUtils.humpToLine(field.getName());
            if (field.isAnnotationPresent(Column.class)) {
                Column ccn = field.getAnnotation(Column.class);
                if (StringUtils.isNotEmpty(ccn.name())) {
                    this.columnName = ccn.name();
                }
            }
            if (field.isAnnotationPresent(Id.class)) {
                this.isPrimaryKey = true;
            }
            this.typeHandler = new IntegerTypeHandler();
        }

        public Field getField() {
            return this.field;
        }

        public String getColumnName() {
            return this.columnName;
        }

        public boolean isPrimaryKey() {
            return this.isPrimaryKey;
        }

        public TypeHandler<?> getTypeHandler() {
            return this.typeHandler;
        }
    }
}
