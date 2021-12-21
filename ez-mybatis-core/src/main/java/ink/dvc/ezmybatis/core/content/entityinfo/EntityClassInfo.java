package ink.dvc.ezmybatis.core.content.entityinfo;

import org.apache.commons.lang3.StringUtils;
import ink.dvc.ezmybatis.core.utils.Assert;
import ink.dvc.ezmybatis.core.utils.HumpLineStringUtils;
import ink.dvc.ezmybatis.core.utils.SqlReflectionUtils;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EntityClassInfo {
    private Class<?> entityClass;
    private String tableName;
    private List<EntityFieldInfo> fieldInfos;
    private Map<String, EntityFieldInfo> columnMapFieldInfo;
    private Map<String, EntityFieldInfo> filedNameMapFieldInfo;
    private EntityFieldInfo primaryKeyInfo;

    public EntityClassInfo(Class<?> entityClass, EntityInfoBuildConfig buildConfig) {
        Assert.notNull(entityClass);
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
        this.filedNameMapFieldInfo = new HashMap<>((int) (this.fieldInfos.size() / 0.75) + 1);
        List<Field> fields = SqlReflectionUtils.getSupportFields(entityClass);
        fields.forEach(field -> {
            EntityFieldInfo fieldInfo = new EntityFieldInfo(field, buildConfig);
            this.fieldInfos.add(fieldInfo);
            if (fieldInfo.isPrimaryKey()) {
                this.primaryKeyInfo = fieldInfo;
            }
        });
        this.fieldInfos.forEach(fieldInfo -> {
            this.columnMapFieldInfo.put(fieldInfo.getColumnName(), fieldInfo);
            this.filedNameMapFieldInfo.put(fieldInfo.getFieldName(), fieldInfo);
        });
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<EntityFieldInfo> getFieldInfos() {
        return this.fieldInfos;
    }

    public Map<String, EntityFieldInfo> getColumnMapFieldInfo() {
        return this.columnMapFieldInfo;
    }

    public String getFieldNameByColumn(String column) {
        EntityFieldInfo entityFieldInfo = this.columnMapFieldInfo.get(column);
        if (entityFieldInfo == null) {
            return null;
        } else {
            return entityFieldInfo.getFieldName();
        }
    }

    public EntityFieldInfo getFieldInfo(String field) {
        EntityFieldInfo fieldInfo = this.filedNameMapFieldInfo.get(field);
        Assert.notNull(fieldInfo, String.format("class %s not found '%s' field", this.getEntityClass()
                .getName(), field));
        return fieldInfo;
    }

    public EntityFieldInfo getPrimaryKeyInfo() {
        return this.primaryKeyInfo;
    }

}
