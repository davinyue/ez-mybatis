package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class PostgreSqlNormalAliasConditionConverter extends AbstractConverter<NormalAliasCondition> implements Converter<NormalAliasCondition> {
    private static volatile PostgreSqlNormalAliasConditionConverter instance;

    protected PostgreSqlNormalAliasConditionConverter() {
    }

    public static PostgreSqlNormalAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlNormalAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlNormalAliasConditionConverter();
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
        return PostgreSqlNormalFieldConditionConverter.getInstance().doBuildSql(null, null,
                sqlBuilder, configuration, obj, mybatisParamHolder, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
