package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;
import java.util.Map;

public abstract class AbstractEntityClassInfo implements EntityClassInfo {
    protected Class<?> entityClass;
    protected String tableName;
    protected String schema;
    protected List<EntityFieldInfo> fieldInfos;
    protected Map<String, EntityFieldInfo> columnMapFieldInfo;
    protected Map<String, EntityFieldInfo> filedNameMapFieldInfo;
    protected EntityFieldInfo primaryKeyInfo;

    @Override
    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public List<EntityFieldInfo> getFieldInfos() {
        return this.fieldInfos;
    }

    @Override
    public Map<String, EntityFieldInfo> getColumnMapFieldInfo() {
        return this.columnMapFieldInfo;
    }

    @Override
    public String getFieldNameByColumn(String column) {
        EntityFieldInfo entityFieldInfo = this.columnMapFieldInfo.get(column);
        if (entityFieldInfo == null) {
            return null;
        } else {
            return entityFieldInfo.getFieldName();
        }
    }

    @Override
    public EntityFieldInfo getFieldInfo(String field) {
        EntityFieldInfo fieldInfo = this.filedNameMapFieldInfo.get(field);
        Assert.notNull(fieldInfo, String.format("class %s not found '%s' field", this.getEntityClass()
                .getName(), field));
        return fieldInfo;
    }

    @Override
    public EntityFieldInfo getPrimaryKeyInfo() {
        Assert.notNull(this.primaryKeyInfo, "can not find primary key info");
        return this.primaryKeyInfo;
    }

}
