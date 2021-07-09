package org.rdlinux.ezmybatis.core.content;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.utils.HumpLineStringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;

public class EntityFieldInfo {
    private Field field;
    private String columnName;
    private boolean isPrimaryKey = false;
    private TypeHandler<?> typeHandler;

    public EntityFieldInfo(Field field, boolean isMapUnderscoreToCamelCase) {
        field.setAccessible(true);
        this.field = field;
        this.columnName = field.getName();
        if (isMapUnderscoreToCamelCase) {
            this.columnName = HumpLineStringUtils.humpToLine(field.getName());
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
}
