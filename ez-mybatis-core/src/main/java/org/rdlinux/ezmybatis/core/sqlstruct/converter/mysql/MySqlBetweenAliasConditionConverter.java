package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlBetweenAliasConditionConverter extends AbstractConverter<BetweenAliasCondition> implements Converter<BetweenAliasCondition> {
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

    protected static StringBuilder doBuildSql(String paramName, StringBuilder sqlBuilder, Configuration configuration,
                                              BetweenCondition obj, MybatisParamHolder mybatisParamHolder,
                                              String column) {
        if (obj.getMinValue() == null || obj.getMaxValue() == null) {
            return sqlBuilder;
        }
        String sql = " " + column + " " + obj.getOperator().getOperator() + " " +
                Condition.valueToSqlStruct(paramName, configuration, mybatisParamHolder, obj.getMinValue()) + " AND " +
                Condition.valueToSqlStruct(paramName, configuration, mybatisParamHolder, obj.getMaxValue()) + " ";
        return sqlBuilder.append(sql);
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       BetweenAliasCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = " " + keywordQM + obj.getAlias() + keywordQM + " ";
        return this.doBuildSql(obj.getAlias(), sqlBuilder, configuration, obj, mybatisParamHolder, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
