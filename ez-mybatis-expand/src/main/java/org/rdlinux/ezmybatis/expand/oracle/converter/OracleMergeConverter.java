package org.rdlinux.ezmybatis.expand.oracle.converter;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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

    protected static void conditionsToSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                          MybatisParamHolder mybatisParamHolder,
                                          List<Condition> conditions) {
        boolean lastConditionEmpty = true;
        for (Condition condition : conditions) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, condition.getClass());
            String sqlPart = converter.buildSql(type, new StringBuilder(), configuration, condition, mybatisParamHolder)
                    .toString();
            boolean emptySql = sqlPart.trim().isEmpty();
            if (!lastConditionEmpty && !emptySql) {
                sqlBuilder.append(condition.getLogicalOperator().name()).append(" ");
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       Merge merge, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(" MERGE INTO ").append(keywordQM).append(merge.getMergeTable().getTableName(configuration))
                .append(keywordQM).append(" ")
                .append(merge.getMergeTable().getAlias()).append(" USING ");
        Converter<? extends EzQueryTable> tableConverter = EzMybatisContent.getConverter(configuration,
                merge.getUseTable().getClass());
        tableConverter.buildSql(type, sqlBuilder, configuration, merge.getUseTable(), mybatisParamHolder);
        sqlBuilder.append(" ON ( ");
        conditionsToSql(type, sqlBuilder, configuration, mybatisParamHolder, merge.getOn());
        sqlBuilder.append(" ) WHEN MATCHED THEN UPDATE SET ");

        List<UpdateItem> items = merge.getSet().getItems();
        for (int i = 0; i < items.size(); i++) {
            UpdateItem updateItem = items.get(i);
            Converter<? extends UpdateItem> converter = EzMybatisContent.getConverter(configuration,
                    updateItem.getClass());
            sqlBuilder = converter.buildSql(type, sqlBuilder, configuration, updateItem,
                    mybatisParamHolder);
            if (i + 1 < items.size()) {
                sqlBuilder.append(", ");
            }
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
