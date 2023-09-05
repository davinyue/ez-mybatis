package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlBetweenAliasConditionConverter extends AbstractConverter<BetweenAliasCondition>
        implements Converter<BetweenAliasCondition> {
    private static volatile MySqlBetweenAliasConditionConverter instance;

    protected MySqlBetweenAliasConditionConverter() {
    }

    public static MySqlBetweenAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlBetweenAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlBetweenAliasConditionConverter();
                }
            }
        }
        return instance;
    }


    protected static String betweenConditionValueToSql(Configuration configuration,
                                                       MybatisParamHolder mybatisParamHolder, Object value) {
        String sql;
        if (value instanceof EzQuery) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, EzQuery.class);
            sql = converter.buildSql(Converter.Type.SELECT, new StringBuilder(), configuration,
                    value, mybatisParamHolder).toString();
        } else {
            sql = mybatisParamHolder.getMybatisParamName(value);
        }
        return sql;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       BetweenAliasCondition obj, MybatisParamHolder mybatisParamHolder) {
        if (obj.getMinValue() == null || obj.getMaxValue() == null) {
            return sqlBuilder;
        }
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(" ")
                .append(keywordQM).append(obj.getAlias()).append(keywordQM)
                .append(" ")
                .append(obj.getOperator().getOperator()).append(" ")
                .append(betweenConditionValueToSql(configuration, mybatisParamHolder, obj.getMinValue()))
                .append(" AND ")
                .append(betweenConditionValueToSql(configuration, mybatisParamHolder, obj.getMaxValue()))
                .append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
