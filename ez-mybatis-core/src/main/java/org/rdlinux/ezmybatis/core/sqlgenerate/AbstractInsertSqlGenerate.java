package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.rdlinux.ezmybatis.annotation.ColumnHandler;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzJdbcSqlParam;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractInsertSqlGenerate implements InsertSqlGenerate {

    private String getTableName(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                                Object model) {
        Assert.notNull(model, "model can not be null");
        if (model instanceof Collection) {
            throw new IllegalArgumentException("model can not instanceof Collection");
        }
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, model.getClass());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String tableName;
        if (table != null) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, table.getClass());
            tableName = converter.buildSql(Converter.Type.INSERT, new StringBuilder(), configuration, table,
                    mybatisParamHolder).toString();
        } else {
            tableName = entityClassInfo.getTableNameWithSchema(keywordQM);
        }
        return tableName;
    }

    @Override
    public String getInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                               Object entity) {
        String tableName = this.getTableName(configuration, mybatisParamHolder, table, entity);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, entity.getClass());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" ");
        StringBuilder columnBuilder = new StringBuilder("( ");
        StringBuilder paramBuilder = new StringBuilder("( ");
        int i = 1;
        for (String column : columnMapFieldInfo.keySet()) {
            EntityFieldInfo fieldInfo = columnMapFieldInfo.get(column);
            Method fieldGetMethod = fieldInfo.getFieldGetMethod();
            Object fieldValue = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            columnBuilder.append(keywordQM).append(column).append(keywordQM);
            paramBuilder.append(mybatisParamHolder.getMybatisParamName(entity.getClass(), fieldInfo.getField(),
                    fieldValue));
            if (i < columnMapFieldInfo.size()) {
                columnBuilder.append(", ");
                paramBuilder.append(", ");
            } else {
                columnBuilder.append(" )");
                paramBuilder.append(" )");
            }
            i++;
        }
        sqlBuilder.append(columnBuilder).append(" VALUES ").append(paramBuilder);
        return sqlBuilder.toString();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public EzJdbcBatchSql getJdbcBatchInsertSql(Configuration configuration, Table table, Collection<?> models) {
        Assert.notEmpty(models, "models can not be empty");
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(configuration, new HashMap<>());
        Object firstEntity = models.iterator().next();
        String tableName = this.getTableName(configuration, mybatisParamHolder, table, firstEntity);
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" ");
        StringBuilder columnBuilder = new StringBuilder("( ");
        StringBuilder paramBuilder = new StringBuilder("( ");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, firstEntity.getClass());
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        EzJdbcBatchSql ret = new EzJdbcBatchSql();
        int i = 1;
        List<List<EzJdbcSqlParam>> params = new ArrayList<>(models.size());
        for (Object ignored : models) {
            params.add(new ArrayList<>(columnMapFieldInfo.size()));
        }
        for (String column : columnMapFieldInfo.keySet()) {
            columnBuilder.append(keywordQM).append(column).append(keywordQM);
            paramBuilder.append("?");
            if (i < columnMapFieldInfo.size()) {
                columnBuilder.append(", ");
                paramBuilder.append(", ");
            } else {
                columnBuilder.append(" )");
                paramBuilder.append(" )");
            }
            EntityFieldInfo fieldInfo = columnMapFieldInfo.get(column);
            TypeHandler typeHandler = null;
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (fieldInfo.getField().isAnnotationPresent(ColumnHandler.class)) {
                ColumnHandler annotation = fieldInfo.getField().getAnnotation(ColumnHandler.class);
                typeHandler = typeHandlerRegistry.getMappingTypeHandler(
                        (Class<? extends TypeHandler<?>>) annotation.value());
            }
            Method fieldGetMethod = fieldInfo.getFieldGetMethod();
            int eti = 0;
            for (Object entity : models) {
                Object fieldValue = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
                fieldValue = EzMybatisContent.onBuildSqlGetField(configuration, entity.getClass(),
                        fieldInfo.getField(), fieldValue);
                JdbcType jdbcType = null;
                if (fieldValue == null) {
                    if (typeHandler == null) {
                        typeHandler = typeHandlerRegistry.getUnknownTypeHandler();
                    }
                    jdbcType = JdbcType.NULL;
                } else {
                    if (typeHandler == null) {
                        typeHandler = typeHandlerRegistry.getTypeHandler(fieldValue.getClass());
                    }
                }
                EzJdbcSqlParam param = new EzJdbcSqlParam(fieldValue, typeHandler, jdbcType);
                params.get(eti).add(param);
                eti++;
            }
            i++;
        }
        sqlBuilder.append(columnBuilder).append(" VALUES ").append(paramBuilder);
        ret.setSql(sqlBuilder.toString());
        ret.setBatchParams(params);
        return ret;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public String getInsertByQuerySql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                                      EzQuery<?> query) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(table, "query can not be null");
        Converter<? extends Table> tableConverter = EzMybatisContent.getConverter(configuration, table.getClass());
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        tableConverter.buildSql(Converter.Type.INSERT, sql, configuration, table, mybatisParamHolder);
        Converter<? extends EzQuery> querConverter = EzMybatisContent.getConverter(configuration, query.getClass());
        querConverter.buildSql(Converter.Type.INSERT, sql, configuration, query, mybatisParamHolder);
        return sql.toString();
    }
}
