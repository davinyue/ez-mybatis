package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import org.apache.commons.lang3.StringUtils;
import org.rdlinux.ezmybatis.constant.TableNamePattern;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.HumpLineStringUtils;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;
import org.rdlinux.ezmybatis.utils.SqlReflectionUtils;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DefaultEntityClassInfo extends AbstractEntityClassInfo {
    public DefaultEntityClassInfo(Class<?> entityClass, EntityInfoBuildConfig buildConfig) {
        Assert.notNull(entityClass, "entityClass can not be null");
        this.tableName = HumpLineStringUtils.humpToLine(entityClass.getSimpleName());
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table annotation = entityClass.getAnnotation(Table.class);
            String tn = annotation.name();
            if (StringUtils.isNotEmpty(tn)) {
                this.tableName = tn;
            }
            this.schema = annotation.schema();
        }
        if (buildConfig.getTableNamePattern() == TableNamePattern.UPPER_CASE) {
            this.tableName = this.tableName.toUpperCase();
        } else if (buildConfig.getTableNamePattern() == TableNamePattern.LOWER_CASE) {
            this.tableName = this.tableName.toLowerCase();
        }
        this.entityClass = entityClass;
        this.fieldInfos = new LinkedList<>();
        List<Field> fields = SqlReflectionUtils.getSupportFields(entityClass);
        for (Field field : fields) {
            Method fieldGetMethod;
            try {
                fieldGetMethod = ReflectionUtils.getMethodOfFieldGet(entityClass, field);
            } catch (Exception e) {
                continue;
            }
            EntityFieldInfo fieldInfo = new EntityFieldInfo(field, fieldGetMethod, buildConfig);
            this.fieldInfos.add(fieldInfo);
            if (fieldInfo.isPrimaryKey()) {
                this.primaryKeyInfo = fieldInfo;
            }
        }
        this.columnMapFieldInfo = new HashMap<>((int) (this.fieldInfos.size() / 0.75) + 1);
        this.filedNameMapFieldInfo = new HashMap<>((int) (this.fieldInfos.size() / 0.75) + 1);
        this.fieldInfos.forEach(fieldInfo -> {
            this.columnMapFieldInfo.put(fieldInfo.getColumnName(), fieldInfo);
            this.filedNameMapFieldInfo.put(fieldInfo.getFieldName(), fieldInfo);
        });
    }
}
