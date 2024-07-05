package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.annotation.ColumnHandler;
import org.rdlinux.ezmybatis.utils.HumpLineStringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EntityFieldInfo {
    private Field field;
    private Method fieldGetMethod;
    private String fieldName;
    private String columnName;
    private boolean isPrimaryKey = false;
    private TypeHandler<?> typeHandler;
    private EntityInfoBuildConfig buildConfig;

    public EntityFieldInfo(Field field, Method fieldGetMethod, String column, boolean isPrimaryKey) {
        field.setAccessible(true);
        this.field = field;
        this.fieldGetMethod = fieldGetMethod;
        this.fieldName = field.getName();
        this.columnName = column;
        this.isPrimaryKey = isPrimaryKey;
        if (field.isAnnotationPresent(ColumnHandler.class)) {
            ColumnHandler annotation = field.getAnnotation(ColumnHandler.class);
            Class<?> typeHandlerClass = annotation.value();
            if (TypeHandler.class.isAssignableFrom(typeHandlerClass)) {
                try {
                    this.typeHandler = (TypeHandler<?>) typeHandlerClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("columnHandler must extend org.apache.ibatis.type.TypeHandler");
            }
        }
    }

    public EntityFieldInfo(Field field, Method fieldGetMethod, EntityInfoBuildConfig buildConfig) {
        field.setAccessible(true);
        this.field = field;
        this.fieldGetMethod = fieldGetMethod;
        this.fieldName = field.getName();
        this.columnName = field.getName();
        this.buildConfig = buildConfig;
        if (buildConfig.getColumnHandle() == EntityInfoBuildConfig.ColumnHandle.TO_UNDER) {
            this.columnName = HumpLineStringUtils.humpToLine(field.getName());
        } else if (buildConfig.getColumnHandle() == EntityInfoBuildConfig.ColumnHandle.TO_UNDER_AND_UPPER) {
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
        if (field.isAnnotationPresent(ColumnHandler.class)) {
            ColumnHandler annotation = field.getAnnotation(ColumnHandler.class);
            Class<?> typeHandlerClass = annotation.value();
            if (TypeHandler.class.isAssignableFrom(typeHandlerClass)) {
                try {
                    this.typeHandler = (TypeHandler<?>) typeHandlerClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("columnHandler must extend org.apache.ibatis.type.TypeHandler");
            }
        }
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

    public Method getFieldGetMethod() {
        return this.fieldGetMethod;
    }
}
