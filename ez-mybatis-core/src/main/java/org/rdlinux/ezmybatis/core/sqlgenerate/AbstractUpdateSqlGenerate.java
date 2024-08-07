package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzJdbcSqlParam;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;
import org.rdlinux.ezmybatis.utils.TypeHandlerUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractUpdateSqlGenerate implements UpdateSqlGenerate {

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                               Object entity, boolean isReplace) {
        Assert.notNull(entity, "entity can not be null");
        if (entity instanceof Collection) {
            throw new IllegalArgumentException("entity can not instanceof Collection");
        }
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, entity.getClass());
        String tableName;
        if (table != null) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, table.getClass());
            tableName = converter.buildSql(Converter.Type.UPDATE, new StringBuilder(), configuration, table,
                    mybatisParamHolder).toString();
        } else {
            tableName = entityClassInfo.getTableNameWithSchema(keywordQM);
        }
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        EntityFieldInfo primaryKeyInfo = entityClassInfo.getPrimaryKeyInfo();
        String idColumn = primaryKeyInfo.getColumnName();
        Object idValue = ReflectionUtils.getFieldValue(entity, primaryKeyInfo.getField());
        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        boolean invalidSql = true;
        for (String column : columnMapFieldInfo.keySet()) {
            EntityFieldInfo entityFieldInfo = columnMapFieldInfo.get(column);
            Method fieldGetMethod = entityFieldInfo.getFieldGetMethod();
            Object fieldValue = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            if ((!isReplace && fieldValue == null) || column.equals(idColumn)) {
                continue;
            }
            sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ");
            sqlBuilder.append(mybatisParamHolder.simpleGetMybatisParamName(entity.getClass(),
                    entityFieldInfo.getField(), fieldValue)).append(", ");
            //有字段更新, sql才有效
            invalidSql = false;
        }
        Assert.isTrue(!invalidSql, "cannot update empty entity");
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
        sqlBuilder.append(" WHERE ").append(keywordQM).append(primaryKeyInfo.getColumnName()).append(keywordQM)
                .append(" = ").append(mybatisParamHolder.simpleGetMybatisParamName(entity.getClass(),
                primaryKeyInfo.getField(), idValue));
        return sqlBuilder.toString();
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> entitys, boolean isReplace) {
        Assert.notEmpty(entitys, "entitys can not be empty");
        StringBuilder sqlBuilder = new StringBuilder();
        for (Object entity : entitys) {
            String sqlTmpl = this.getUpdateSql(configuration, mybatisParamHolder, table, entity, isReplace);
            sqlBuilder.append(sqlTmpl).append(";\n");
        }
        return sqlBuilder.toString();
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchUpdateSql(Configuration configuration, Table table, Collection<?> models,
                                                Collection<String> updateFields, boolean isReplace) {
        Assert.notNull(models, "models can not be null");
        Object firstEntity = models.iterator().next();
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(configuration, new HashMap<>());
        String tableName = AbstractInsertSqlGenerate.getTableName(configuration, mybatisParamHolder, table,
                firstEntity);
        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, firstEntity.getClass());
        EntityFieldInfo primaryKeyInfo = entityClassInfo.getPrimaryKeyInfo();
        List<EntityFieldInfo> updateFieldInfo = new ArrayList<>();
        Map<String, EntityFieldInfo> allColumnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        //已经存在的更新字段
        Set<String> existsUpdateFieldInfos = new HashSet<>();
        //replace模式更新全部字段, 否则更新非空字段, 以第一个实体的非空字段为准
        if (isReplace) {
            updateFieldInfo = allColumnMapFieldInfo.values().stream().filter(e -> !e.isPrimaryKey())
                    .collect(Collectors.toList());
        } else {
            if (updateFields != null && !updateFields.isEmpty()) {
                Collection<EntityFieldInfo> entityFieldInfos = allColumnMapFieldInfo.values();
                for (String updateField : updateFields) {
                    if (updateField == null || updateField.isEmpty()) {
                        continue;
                    }
                    boolean exField = false;
                    for (EntityFieldInfo fieldInfo : entityFieldInfos) {
                        if (fieldInfo.getFieldName().equals(updateField)) {
                            if (!existsUpdateFieldInfos.contains(fieldInfo.getFieldName()) &&
                                    !fieldInfo.isPrimaryKey()) {
                                updateFieldInfo.add(fieldInfo);
                                existsUpdateFieldInfos.add(fieldInfo.getFieldName());
                            }
                            exField = true;
                            break;
                        }
                    }
                    Assert.isTrue(exField, String.format("Class %s not found '%s' field", firstEntity.getClass()
                            .getName(), updateField));
                }
            } else {
                for (EntityFieldInfo fieldInfo : allColumnMapFieldInfo.values()) {
                    Method fieldGetMethod = fieldInfo.getFieldGetMethod();
                    for (Object model : models) {
                        if (!firstEntity.getClass().getName().equals(model.getClass().getName())) {
                            throw new IllegalArgumentException("Inconsistent object types within the container");
                        }
                        Object fieldValue = ReflectionUtils.invokeMethod(model, fieldGetMethod);
                        if (fieldValue != null && !fieldInfo.isPrimaryKey()) {
                            if (!existsUpdateFieldInfos.contains(fieldInfo.getFieldName())) {
                                updateFieldInfo.add(fieldInfo);
                                existsUpdateFieldInfos.add(fieldInfo.getFieldName());
                            }
                        }
                    }
                }
            }
        }
        Assert.isTrue(!updateFieldInfo.isEmpty(), String.format("Class %s no fields found for updating",
                firstEntity.getClass().getName()));
        updateFieldInfo.add(primaryKeyInfo);
        EzJdbcBatchSql ret = new EzJdbcBatchSql();
        List<List<EzJdbcSqlParam>> params = new ArrayList<>(models.size());
        for (Object ignored : models) {
            params.add(new ArrayList<>(updateFieldInfo.size()));
        }
        int i = 1;
        for (EntityFieldInfo fieldInfo : updateFieldInfo) {
            if (!fieldInfo.isPrimaryKey()) {
                sqlBuilder.append(keywordQM).append(fieldInfo.getColumnName()).append(keywordQM).append(" = ?");
            }
            if (i < updateFieldInfo.size() - 1) {
                sqlBuilder.append(", ");
            }
            TypeHandler<?> typeHandler = TypeHandlerUtils.getTypeHandle(configuration, fieldInfo.getField());
            Method fieldGetMethod = fieldInfo.getFieldGetMethod();
            int eti = 0;
            for (Object model : models) {
                Object fieldValue = ReflectionUtils.invokeMethod(model, fieldGetMethod);
                fieldValue = EzMybatisContent.onBuildSqlGetField(configuration, Boolean.TRUE, model.getClass(),
                        fieldInfo.getField(), fieldValue);
                JdbcType jdbcType = TypeHandlerUtils.getJdbcType(fieldValue);
                EzJdbcSqlParam param = new EzJdbcSqlParam(fieldValue, typeHandler, jdbcType);
                params.get(eti).add(param);
                eti++;
            }
            i++;
        }
        sqlBuilder.append(" WHERE ").append(keywordQM).append(primaryKeyInfo.getColumnName()).append(keywordQM)
                .append(" = ?");
        ret.setSql(sqlBuilder.toString());
        ret.setBatchParams(params);
        return ret;
    }
}
