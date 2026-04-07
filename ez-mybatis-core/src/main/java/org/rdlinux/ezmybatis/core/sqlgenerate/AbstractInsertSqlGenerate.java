package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.*;
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

public abstract class AbstractInsertSqlGenerate implements InsertSqlGenerate {

    public static InsertSqlParts getInsertSqlParts(SqlGenerateContext sqlGenerateContext, Object entity) {
        Assert.notNull(sqlGenerateContext, "sqlGenerateContext can not be null");
        Assert.notNull(entity, "entity can not be null");
        if (entity instanceof Collection) {
            throw new IllegalArgumentException("entity can not instanceof Collection");
        }
        Configuration configuration = sqlGenerateContext.getConfiguration();
        MybatisParamHolder mybatisParamHolder = sqlGenerateContext.getMybatisParamHolder();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, entity.getClass());
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        StringBuilder columnBuilder = new StringBuilder("( ");
        StringBuilder valueBuilder = new StringBuilder("( ");
        int i = 1;
        for (String column : columnMapFieldInfo.keySet()) {
            EntityFieldInfo fieldInfo = columnMapFieldInfo.get(column);
            Method fieldGetMethod = fieldInfo.getFieldGetMethod();
            Object fieldValue = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            columnBuilder.append(keywordQM).append(column).append(keywordQM);
            valueBuilder.append(mybatisParamHolder.entityPersistGetMybatisParamName(entity.getClass(), fieldInfo.getField(),
                    fieldValue));
            if (i < columnMapFieldInfo.size()) {
                columnBuilder.append(", ");
                valueBuilder.append(", ");
            } else {
                columnBuilder.append(" )");
                valueBuilder.append(" )");
            }
            i++;
        }
        return new InsertSqlParts(columnBuilder.toString(), valueBuilder.toString());
    }

    public static String getTableName(SqlGenerateContext sqlGenerateContext, Table table, Object model) {
        Assert.notNull(model, "model can not be null");
        if (model instanceof Collection) {
            throw new IllegalArgumentException("model can not instanceof Collection");
        }
        Configuration configuration = sqlGenerateContext.getConfiguration();
        MybatisParamHolder mybatisParamHolder = sqlGenerateContext.getMybatisParamHolder();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, model.getClass());
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        String tableName;
        if (table != null) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, table.getClass());
            converter.buildSql(Converter.Type.INSERT, table, sqlGenerateContext);
            tableName = sqlGenerateContext.getSqlBuilder().toString();
            sqlGenerateContext.getSqlBuilder().setLength(0);
        } else {
            tableName = entityClassInfo.getTableNameWithSchema(keywordQM);
        }
        return tableName;
    }

    @Override
    public String getInsertSql(SqlGenerateContext sqlGenerateContext, Table table, Object entity) {
        String tableName = AbstractInsertSqlGenerate.getTableName(sqlGenerateContext, table, entity);
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" ");
        InsertSqlParts insertSqlParts = getInsertSqlParts(sqlGenerateContext, entity);
        sqlBuilder.append(insertSqlParts.getColumnsSql()).append(" VALUES ").append(insertSqlParts.getValuesSql());
        return sqlBuilder.toString();
    }

    @Override
    public EzJdbcBatchSql getJdbcBatchInsertSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<?> models) {
        Assert.notEmpty(models, "models can not be empty");
        Configuration configuration = sqlGenerateContext.getConfiguration();
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(configuration, new HashMap<>());
        Object firstEntity = models.iterator().next();
        String tableName = AbstractInsertSqlGenerate.getTableName(sqlGenerateContext, table, firstEntity);
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" ");
        StringBuilder columnBuilder = new StringBuilder("( ");
        StringBuilder paramBuilder = new StringBuilder("( ");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, firstEntity.getClass());
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        EzJdbcBatchSql ret = new EzJdbcBatchSql();
        int i = 1;
        List<List<EzJdbcSqlParam>> params = new ArrayList<>(models.size());
        for (Object model : models) {
            if (!firstEntity.getClass().getName().equals(model.getClass().getName())) {
                throw new IllegalArgumentException("Inconsistent object types within the container");
            }
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
            Method fieldGetMethod = fieldInfo.getFieldGetMethod();
            TypeHandler<?> typeHandler = TypeHandlerUtils.getTypeHandle(configuration, fieldInfo.getField());
            int eti = 0;
            for (Object entity : models) {
                Object fieldValue = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
                JdbcType jdbcType = TypeHandlerUtils.getJdbcType(fieldValue);
                fieldValue = EzMybatisContent.onBuildSqlGetField(configuration, FieldAccessScope.ENTITY_PERSIST,
                        entity.getClass(), fieldInfo.getField(), fieldValue);
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
    public String getInsertByQuerySql(SqlGenerateContext sqlGenerateContext, Table table, EzQuery<?> query) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(table, "query can not be null");
        Configuration configuration = sqlGenerateContext.getConfiguration();
        Converter<? extends Table> tableConverter = EzMybatisContent.getConverter(configuration, table.getClass());
        sqlGenerateContext.getSqlBuilder().append("INSERT INTO ");
        tableConverter.buildSql(Converter.Type.INSERT, table, sqlGenerateContext);
        String querySql = EzMybatisContent.getDbDialectProvider(configuration)
                .getSqlGenerate().getQuerySql(SqlGenerateContext.copyOf(sqlGenerateContext), query);
        sqlGenerateContext.getSqlBuilder().append(" ");
        boolean insertParenthesis = this.insertByQueryAppendParenthesis();
        if (insertParenthesis) {
            sqlGenerateContext.getSqlBuilder().append("(");
        }
        sqlGenerateContext.getSqlBuilder().append(querySql);
        if (insertParenthesis) {
            sqlGenerateContext.getSqlBuilder().append(")");
        }
        return sqlGenerateContext.getSqlBuilder().toString();
    }

    /**
     * 根据查询插入数据是否在查询sql外层添加圆括号
     */
    protected boolean insertByQueryAppendParenthesis() {
        return true;
    }

    public static class InsertSqlParts {
        private final String columnsSql;
        private final String valuesSql;

        public InsertSqlParts(String columnsSql, String valuesSql) {
            this.columnsSql = columnsSql;
            this.valuesSql = valuesSql;
        }

        public String getColumnsSql() {
            return this.columnsSql;
        }

        public String getValuesSql() {
            return this.valuesSql;
        }
    }
}
