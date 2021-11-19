package org.rdlinux.ezmybatis.core.content.entityinfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.utils.HumpLineStringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;

public class EntityFieldInfo {
    private Field field;
    private String fieldName;
    private String columnName;
    private boolean isPrimaryKey = false;
    private TypeHandler<?> typeHandler;
    private EntityInfoBuildConfig buildConfig;

    public EntityFieldInfo(Field field, EntityInfoBuildConfig buildConfig) {
        field.setAccessible(true);
        this.field = field;
        this.fieldName = field.getName();
        this.columnName = field.getName();
        this.buildConfig = buildConfig;
        if (buildConfig.getColumnHandle() == EntityInfoBuildConfig.ColumnHandle.ToUnder) {
            this.columnName = HumpLineStringUtils.humpToLine(field.getName());
        } else if (buildConfig.getColumnHandle() == EntityInfoBuildConfig.ColumnHandle.ToUnderAndUpper) {
            this.columnName = HumpLineStringUtils.humpToLine(field.getName()).toUpperCase();
        }
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

    public EntityInfoBuildConfig getBuildConfig() {
        return this.buildConfig;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
