package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.AliasCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlAliasCompareConditionConverter extends AbstractConverter<AliasCompareCondition> implements Converter<AliasCompareCondition> {
    private static volatile MySqlAliasCompareConditionConverter instance;

    protected MySqlAliasCompareConditionConverter() {
    }

    public static MySqlAliasCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlAliasCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlAliasCompareConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       AliasCompareCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " " + keywordQM + obj.getLeftAlias() + keywordQM +
                " " +
                obj.getOperator().getOperator() +
                " " +
                keywordQM + obj.getRightAlias() + keywordQM +
                " ";
        return sqlBuilder.append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
