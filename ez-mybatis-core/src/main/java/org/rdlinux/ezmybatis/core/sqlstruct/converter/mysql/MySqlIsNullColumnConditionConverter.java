package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlIsNullColumnConditionConverter extends AbstractConverter<IsNullColumnCondition> implements Converter<IsNullColumnCondition> {
    private static volatile MySqlIsNullColumnConditionConverter instance;

    protected MySqlIsNullColumnConditionConverter() {
    }

    public static MySqlIsNullColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlIsNullColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlIsNullColumnConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       IsNullColumnCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = obj.getTable().getAlias() + "." + keywordQM + obj.getColumn() + keywordQM;
        return MySqlIsNullAliasConditionConverter.doBuildSql(sqlBuilder, obj, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
