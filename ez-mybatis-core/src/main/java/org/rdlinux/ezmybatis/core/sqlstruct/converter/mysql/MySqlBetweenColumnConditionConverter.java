package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlBetweenColumnConditionConverter extends AbstractConverter<BetweenColumnCondition> implements Converter<BetweenColumnCondition> {
    private static volatile MySqlBetweenColumnConditionConverter instance;

    protected MySqlBetweenColumnConditionConverter() {
    }

    public static MySqlBetweenColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlBetweenColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlBetweenColumnConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       BetweenColumnCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = obj.getTable().getAlias() + "." + keywordQM + obj.getColumn() + keywordQM;
        return MySqlBetweenAliasConditionConverter.doBuildSql(obj.getColumn(), sqlBuilder, configuration, obj,
                mybatisParamHolder, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
