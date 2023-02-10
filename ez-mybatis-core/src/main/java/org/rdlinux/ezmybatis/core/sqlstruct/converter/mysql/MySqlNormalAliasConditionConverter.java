package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlNormalAliasConditionConverter extends AbstractConverter<NormalAliasCondition> implements Converter<NormalAliasCondition> {
    private static volatile MySqlNormalAliasConditionConverter instance;

    protected MySqlNormalAliasConditionConverter() {
    }

    public static MySqlNormalAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlNormalAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlNormalAliasConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NormalAliasCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = keywordQM + obj.getAlias() + keywordQM;
        return MySqlNormalFieldConditionConverter.doBuildSql(obj.getAlias(), sqlBuilder, configuration, obj,
                mybatisParamHolder, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
