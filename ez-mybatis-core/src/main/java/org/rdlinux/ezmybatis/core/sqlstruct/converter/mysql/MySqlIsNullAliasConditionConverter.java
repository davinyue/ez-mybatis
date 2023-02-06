package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlIsNullAliasConditionConverter extends AbstractConverter<IsNullAliasCondition> implements Converter<IsNullAliasCondition> {
    private static volatile MySqlIsNullAliasConditionConverter instance;

    protected MySqlIsNullAliasConditionConverter() {
    }

    public static MySqlIsNullAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlIsNullAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlIsNullAliasConditionConverter();
                }
            }
        }
        return instance;
    }

    protected static StringBuilder doBuildSql(StringBuilder sqlBuilder, IsNullCondition obj, String column) {
        String sql = " " + column + " " + obj.getOperator().getOperator() + " ";
        return sqlBuilder.append(sql);
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       IsNullAliasCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = " " + keywordQM + obj.getAlias() + keywordQM + " ";
        return doBuildSql(sqlBuilder, obj, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
