package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import org.linuxprobe.crud.core.annoatation.UpdateIgnore;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.sql.generator.Escape;
import org.linuxprobe.crud.core.sql.generator.UpdateSqlGenerator;
import org.linuxprobe.crud.exception.OperationNotSupportedException;

import java.util.List;

public class MysqlUpdateSqlGenerator extends MysqlEscape implements UpdateSqlGenerator, Escape {
    /**
     * 校验主键并获取值
     */
    private String checkPrimaryKeyAndGetValue(Object entity, EntityInfo entityInfo) {
        if (entityInfo.getPrimaryKey() == null) {
            throw new OperationNotSupportedException(entity.getClass()
                    + " cannot be without primary key, please use org.linuxprobe.crud.core.annoatation.PrimaryKey Mark the primary key");
        }
        // 获取主键值
        String primaryKeyValue = MysqlFieldValueConversion.updateModelConversion(entity,
                entityInfo.getPrimaryKey().getField());
        if (primaryKeyValue == null) {
            throw new IllegalArgumentException("The primary key cannot be null");
        }
        if (String.class.isAssignableFrom(entityInfo.getPrimaryKey().getField().getType())) {
            if (primaryKeyValue.isEmpty()) {
                throw new IllegalArgumentException("The primary key cannot be empty");
            }
        }
        return primaryKeyValue;
    }

    /**
     * 生成字段全更新sql
     */
    @Override
    public String toGlobalUpdateSql(Object entity) {
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
        String table = entityInfo.getTableName();
        // 获取主键值
        String primaryKeyValue = this.checkPrimaryKeyAndGetValue(entity, entityInfo);
        StringBuilder sqlBuilder = new StringBuilder("UPDATE `" + table + "` SET ");
        List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
        for (FieldInfo fieldInfo : fieldInfos) {
            // 如果指定忽略更新，跳过
            if (fieldInfo.getField().isAnnotationPresent(UpdateIgnore.class)) {
                continue;
            }
            String value = MysqlFieldValueConversion.updateModelConversion(entity, fieldInfo.getField());
            sqlBuilder.append("`").append(fieldInfo.getColumnName()).append("` = ").append(value).append(", ");
        }
        if (sqlBuilder.indexOf(",") != -1) {
            sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), " ");
        }
        sqlBuilder.append("WHERE `").append(entityInfo.getPrimaryKey().getColumnName()).append("` = ").append(primaryKeyValue);
        return sqlBuilder.toString();
    }

    /**
     * 生成字段选择更新sql
     */
    @Override
    public String toLocalUpdateSql(Object entity) {
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
        String table = entityInfo.getTableName();
        // 获取主键值
        String primaryKeyValue = this.checkPrimaryKeyAndGetValue(entity, entityInfo);
        StringBuilder sqlBuilder = new StringBuilder("UPDATE `" + table + "` SET ");
        List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
        for (FieldInfo fieldInfo : fieldInfos) {
            // 如果指定忽略更新，跳过
            if (fieldInfo.getField().isAnnotationPresent(UpdateIgnore.class)) {
                continue;
            }
            String value = MysqlFieldValueConversion.updateModelConversion(entity, fieldInfo.getField());
            if (value != null) {
                sqlBuilder.append("`").append(fieldInfo.getColumnName()).append("` = ").append(value).append(", ");
            }
        }
        if (sqlBuilder.indexOf(",") != -1) {
            sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), " ");
        }
        sqlBuilder.append("WHERE `").append(entityInfo.getPrimaryKey().getColumnName()).append("` = ").append(primaryKeyValue);
        return sqlBuilder.toString();
    }
}
