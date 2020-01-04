package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.Escape;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MysqlDeleteSqlGenerator extends MysqlEscape implements DeleteSqlGenerator, Escape {
    @Override
    public String generateDeleteSql(Object record) {
        if (record == null) {
            throw new NullPointerException("entity can't be null");
        }
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(record.getClass());
        String idValue = MysqlFieldValueConversion.deleteModelConversion(record, entityInfo.getPrimaryKey().getField());
        return "DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
                + entityInfo.getPrimaryKey().getColumnName() + "` = " + idValue;
    }

    @Override
    public String generateDeleteSqlByPrimaryKey(Serializable id, Class<?> type) {
        if (id == null) {
            throw new NullPointerException("id can't be null");
        }
        if (type == null) {
            throw new NullPointerException("type can't be null");
        }
        if (String.class.isAssignableFrom(id.getClass())) {
            id = super.getQuotation() + id + super.getQuotation();
        }
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(type);
        return "DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
                + entityInfo.getPrimaryKey().getColumnName() + "` = " + id;
    }

    @Override
    public String generateBatchDeleteSql(Collection<?> entitys) {
        if (entitys == null || entitys.isEmpty()) {
            throw new IllegalArgumentException("entitys can't be empty");
        }
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM `");
        Iterator<?> entityIterator = entitys.iterator();
        String table = null;
        EntityInfo entityInfo = null;
        while (entityIterator.hasNext()) {
            Object entity = entityIterator.next();
            if (table == null) {
                entityInfo = UniversalCrudContent.getEntityInfo(entity.getClass());
                table = entityInfo.getTableName();
                sqlBuilder.append(table).append("` WHERE `").append(entityInfo.getPrimaryKey().getColumnName()).append("` IN(");
            }
            String idValue = MysqlFieldValueConversion.deleteModelConversion(entity,
                    entityInfo.getPrimaryKey().getField());
            if (idValue == null) {
                throw new NullPointerException(entity.toString() + " id can't be null");
            }
            if (String.class.isAssignableFrom(entityInfo.getPrimaryKey().getField().getType())) {
                idValue = super.getQuotation() + idValue + super.getQuotation();
            }
            sqlBuilder.append(idValue).append(", ");
        }
        if (sqlBuilder.lastIndexOf(", ") != -1) {
            sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), "");
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    @Override
    public <T extends Serializable> String generateBatchDeleteSqlByPrimaryKey(Collection<T> ids, Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type can't be null");
        }
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ids can't be null");
        }
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(type);
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM `" + entityInfo.getTableName() + "` WHERE `"
                + entityInfo.getPrimaryKey().getColumnName() + "` IN (");
        for (Serializable idValue : ids) {
            if (String.class.isAssignableFrom(idValue.getClass())) {
                idValue = super.getQuotation() + idValue + "', ";
            }
            sqlBuilder.append(idValue).append(", ");
        }
        if (sqlBuilder.lastIndexOf(", ") != -1) {
            sqlBuilder.replace(sqlBuilder.length() - 2, sqlBuilder.length(), "");
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    @Override
    public String generateDeleteSqlByColumnName(String columnName, Serializable columnValue, Class<?> modelType) {
        if (columnName == null) {
            throw new IllegalArgumentException("column cannot be null");
        }
        if (modelType == null) {
            throw new IllegalArgumentException("modelType cannot be null");
        }
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
        String table = entityInfo.getTableName();
        if (String.class.isAssignableFrom(columnValue.getClass())) {
            columnValue = super.escape((String) columnValue);
            columnValue = super.getQuotation() + columnValue + super.getQuotation();
        }
        return "DElETE  FROM `" + table + "` WHERE `" + columnName + "` = " + columnValue;
    }

    @Override
    public String generateDeleteSqlByFieldName(String fieldName, Serializable fieldValue, Class<?> modelType) {
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
        List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
        String columnName = null;
        for (FieldInfo fieldInfo : fieldInfos) {
            if (fieldInfo.getField().getName().equals(fieldName)) {
                columnName = fieldInfo.getColumnName();
                break;
            }
        }
        if (columnName == null) {
            throw new IllegalArgumentException(fieldName + " is not " + modelType.getName() + " field");
        }
        return this.generateDeleteSqlByColumnName(columnName, fieldValue, modelType);
    }

    @Override
    public String generateDeleteSqlByColumnNames(String[] columnNames, Serializable[] columnValues,
                                                 Class<?> modelType) {
        if (columnNames == null || columnNames.length == 0) {
            throw new IllegalArgumentException("column cannot be null");
        }
        if (columnValues == null || columnValues.length == 0) {
            throw new IllegalArgumentException("columnValues cannot be null");
        }
        if (columnNames.length != columnValues.length) {
            throw new IllegalArgumentException("columnNames length uneq columnValues length");
        }
        if (modelType == null) {
            throw new IllegalArgumentException("modelType cannot be null");
        }
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
        String table = entityInfo.getTableName();
        StringBuilder sqlBuilder = new StringBuilder("DElETE  FROM `" + table + "` WHERE ");
        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            Serializable columnValue = columnValues[i];
            if (String.class.isAssignableFrom(columnValue.getClass())) {
                columnValue = super.escape((String) columnValue);
                columnValue = super.getQuotation() + columnValue + super.getQuotation();
            }
            if (i + 1 < columnNames.length) {
                sqlBuilder.append("`").append(columnName).append("` = ").append(columnValue).append(" AND ");
            } else {
                sqlBuilder.append("`").append(columnName).append("` = ").append(columnValue);
            }
        }
        return sqlBuilder.toString();
    }

    @Override
    public String generateDeleteSqlByFieldNames(String[] fieldNames, Serializable[] fieldValues, Class<?> modelType) {
        if (fieldNames == null || fieldNames.length == 0) {
            throw new IllegalArgumentException("fieldNames cannot be null");
        }
        if (fieldValues == null || fieldValues.length == 0) {
            throw new IllegalArgumentException("fieldValues cannot be null");
        }
        if (fieldNames.length != fieldValues.length) {
            throw new IllegalArgumentException("fieldNames length uneq fieldValues length");
        }
        if (modelType == null) {
            throw new IllegalArgumentException("modelType cannot be null");
        }
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
        List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
        String[] columnNames = new String[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            String columnName = null;
            for (FieldInfo fieldInfo : fieldInfos) {
                if (fieldInfo.getField().getName().equals(fieldName)) {
                    columnName = fieldInfo.getColumnName();
                    break;
                }
            }
            if (columnName == null) {
                throw new IllegalArgumentException("No column corresponding to " + fieldName + " was found");
            } else {
                columnNames[i] = columnName;
            }
        }
        return this.generateDeleteSqlByColumnNames(columnNames, fieldValues, modelType);
    }
}
