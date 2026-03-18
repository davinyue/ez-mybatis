package org.rdlinux.ezmybatis.expand.oracle.converter;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.expand.oracle.update.Merge;

import java.util.List;

public class OracleMergeConverter extends AbstractConverter<Merge> implements Converter<Merge> {
    private static volatile OracleMergeConverter instance;

    protected OracleMergeConverter() {
    }

    public static OracleMergeConverter getInstance() {
        if (instance == null) {
            synchronized (OracleMergeConverter.class) {
                if (instance == null) {
                    instance = new OracleMergeConverter();
                }
            }
        }
        return instance;
    }

    protected static void conditionsToSql(Type type, SqlGenerateContext sqlGenerateContext, List<Condition> conditions) {
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
        conditionsToSql(type, sqlGenerateContext, merge.getOn());
        sqlBuilder.append(" ) WHEN MATCHED THEN UPDATE SET ");

        List<UpdateItem> items = merge.getSet().getItems();
        for (int i = 0; i < items.size(); i++) {
            UpdateItem updateItem = items.get(i);
            Converter<? extends UpdateItem> converter = EzMybatisContent.getConverter(configuration,
                    updateItem.getClass());
            converter.buildSql(type, updateItem, sqlGenerateContext);
            if (i + 1 < items.size()) {
                sqlBuilder.append(", ");
            }
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
