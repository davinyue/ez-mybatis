package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;
import java.util.Map;

@Setter
@Accessors(chain = true)
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
    public String getTableNameWithSchema(String keywordQM) {
        if (keywordQM == null) {
            keywordQM = "";
        }
        if (this.schema != null && !this.schema.isEmpty()) {
            return keywordQM + this.schema + keywordQM + "." + keywordQM + this.tableName + keywordQM;
        } else {
            return keywordQM + this.tableName + keywordQM;
        }
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
    public Map<String, EntityFieldInfo> getFiledNameMapFieldInfo() {
        return this.filedNameMapFieldInfo;
    }

    @Override
    public String getFieldNameByColumn(String column) {
        //先根据原始列名查询
        EntityFieldInfo entityFieldInfo = this.columnMapFieldInfo.get(column);
        //如果没有则根据小写列名查询
        if (entityFieldInfo == null) {
            entityFieldInfo = this.columnMapFieldInfo.get(column.toLowerCase());
        }
        //如果没有则根据大写列名查询
        if (entityFieldInfo == null) {
            entityFieldInfo = this.columnMapFieldInfo.get(column.toUpperCase());
        }
        if (entityFieldInfo == null) {
            return null;
        } else {
            return entityFieldInfo.getFieldName();
        }
    }

    @Override
    public EntityFieldInfo getFieldInfo(String field) {
        EntityFieldInfo fieldInfo = this.filedNameMapFieldInfo.get(field);
        Assert.notNull(fieldInfo, String.format("Class %s not found '%s' field", this.getEntityClass()
                .getName(), field));
        return fieldInfo;
    }

    @Override
    public EntityFieldInfo getPrimaryKeyInfo() {
        Assert.notNull(this.primaryKeyInfo, String.format("Class %s not found primary key info",
                this.getEntityClass().getName()));
        return this.primaryKeyInfo;
    }
}
