package org.rdlinux.ezmybatis.expand.mssql.converter;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.expand.oracle.update.Merge;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

import java.util.List;

public class SqlServerMergeConverter extends AbstractConverter<Merge> implements Converter<Merge> {
    private static volatile SqlServerMergeConverter instance;

    protected SqlServerMergeConverter() {
    }

    public static SqlServerMergeConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerMergeConverter.class) {
                if (instance == null) {
                    instance = new SqlServerMergeConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Merge merge, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(" MERGE INTO ").append(keywordQM).append(merge.getMergeTable().getTableName(configuration))
                .append(keywordQM).append(" ")
                .append(merge.getMergeTable().getAlias()).append(" USING ");
        Converter<? extends EzQueryTable> tableConverter = EzMybatisContent.getConverter(configuration,
                merge.getUseTable().getClass());
        tableConverter.buildSql(Type.SELECT, merge.getUseTable(), sqlGenerateContext);
        sqlBuilder.append(" ON ( ");
        this.conditionsToSql(Type.SELECT, sqlGenerateContext, merge.getOn());
        sqlBuilder.append(" ) WHEN MATCHED THEN UPDATE SET ");

        List<UpdateItem> items = merge.getSet().getItems();
        for (int i = 0; i < items.size(); i++) {
            this.buildUpdateItemSql(items.get(i), sqlGenerateContext);
            if (i + 1 < items.size()) {
                sqlBuilder.append(", ");
            }
        }
        if (merge.getNotMatchedInsertEntity() != null) {
            List<EzMybatisInsertListener> listeners = EzMybatisContent
                    .getInsertListeners(sqlGenerateContext.getConfiguration());
            if (listeners != null) {
                for (EzMybatisInsertListener listener : listeners) {
                    listener.onInsert(merge.getNotMatchedInsertEntity());
                }
            }
            AbstractInsertSqlGenerate.InsertSqlParts insertSqlParts = AbstractInsertSqlGenerate.getInsertSqlParts(
                    sqlGenerateContext, merge.getNotMatchedInsertEntity());
            sqlBuilder.append(" WHEN NOT MATCHED THEN INSERT ")
                    .append(insertSqlParts.getColumnsSql())
                    .append(" VALUES ")
                    .append(insertSqlParts.getValuesSql());
        }
        sqlBuilder.append(";");
    }

    private void conditionsToSql(Type type, SqlGenerateContext sqlGenerateContext, List<Condition> conditions) {
        boolean lastConditionEmpty = true;
        Configuration configuration = sqlGenerateContext.getConfiguration();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        for (Condition condition : conditions) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, condition.getClass());
            SqlGenerateContext sqlPartCt = SqlGenerateContext.copyOf(sqlGenerateContext);
            converter.buildSql(type, condition, sqlPartCt);
            String sqlPart = sqlPartCt.getSqlBuilder().toString();
            boolean emptySql = sqlPart.trim().isEmpty();
            if (!lastConditionEmpty && !emptySql) {
                sqlBuilder.append(condition.getAndOr().name()).append(" ");
            }
            if (!emptySql) {
                lastConditionEmpty = false;
                sqlBuilder.append(sqlPart);
            } else {
                lastConditionEmpty = true;
            }
        }
    }

    private void buildUpdateItemSql(UpdateItem updateItem, SqlGenerateContext sqlGenerateContext) {
        if (updateItem instanceof UpdateFieldItem) {
            this.buildUpdateFieldItemSql((UpdateFieldItem) updateItem, sqlGenerateContext);
            return;
        }
        if (updateItem instanceof UpdateColumnItem) {
            this.buildUpdateColumnItemSql((UpdateColumnItem) updateItem, sqlGenerateContext);
            return;
        }
        Converter<? extends UpdateItem> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                updateItem.getClass());
        converter.buildSql(Type.SELECT, updateItem, sqlGenerateContext);
    }

    private void buildUpdateFieldItemSql(UpdateFieldItem updateItem, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, updateItem.getEntityTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(updateItem.getField());
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlGenerateContext.pushAccessField(EntityField.of(updateItem.getEntityTable(), updateItem.getField()));
        sqlBuilder.append(updateItem.getTable().getAlias()).append(".")
                .append(keywordQM).append(SqlEscaping.nameEscaping(fieldInfo.getColumnName())).append(keywordQM)
                .append(" = ");
        this.buildOperandSql(updateItem.getValue(), sqlGenerateContext);
        sqlGenerateContext.popAccessField();
    }

    private void buildUpdateColumnItemSql(UpdateColumnItem updateItem, SqlGenerateContext sqlGenerateContext) {
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(sqlGenerateContext.getConfiguration());
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(updateItem.getTable().getAlias()).append(".")
                .append(keywordQM).append(SqlEscaping.nameEscaping(updateItem.getColumn())).append(keywordQM)
                .append(" = ");
        this.buildOperandSql(updateItem.getValue(), sqlGenerateContext);
    }

    private void buildOperandSql(Operand operand, SqlGenerateContext sqlGenerateContext) {
        Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                operand.getClass());
        argConverter.buildSql(Type.SELECT, operand, sqlGenerateContext);
    }
}
