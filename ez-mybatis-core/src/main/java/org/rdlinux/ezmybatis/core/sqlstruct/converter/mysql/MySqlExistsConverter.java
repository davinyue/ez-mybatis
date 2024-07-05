package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

@SuppressWarnings("rawtypes")
public class MySqlExistsConverter extends AbstractConverter<ExistsCondition> implements Converter<ExistsCondition> {
    private static volatile MySqlExistsConverter instance;

    protected MySqlExistsConverter() {
    }

    public static MySqlExistsConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlExistsConverter.class) {
                if (instance == null) {
                    instance = new MySqlExistsConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ExistsCondition obj, MybatisParamHolder mybatisParamHolder) {
        if (obj.getQuery() == null) {
            return sqlBuilder;
        }
        if (obj.isNot()) {
            sqlBuilder.append(" NOT ");
        }
        sqlBuilder.append(" EXISTS  ");
        Converter<EzQuery> ezQueryConverter = EzMybatisContent.getConverter(configuration, EzQuery.class);
        return ezQueryConverter.buildSql(type, sqlBuilder, configuration, obj.getQuery(), mybatisParamHolder);

    }

    protected String ezQueryToSql(Configuration configuration, EzQuery<?> obj, MybatisParamHolder mybatisParamHolder) {
        return " (" + SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getQuerySql(configuration, mybatisParamHolder, obj) + ") ";
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
