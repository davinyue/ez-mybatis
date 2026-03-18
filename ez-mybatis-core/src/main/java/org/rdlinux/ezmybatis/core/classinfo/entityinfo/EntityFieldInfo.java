package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.utils.HumpLineStringUtils;
import org.rdlinux.ezmybatis.utils.TypeHandlerUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@Setter
@Accessors(chain = true)
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
        this.typeHandler = TypeHandlerUtils.getCustomTypeHandle(field);
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
        this.typeHandler = TypeHandlerUtils.getCustomTypeHandle(field);
    }
}
