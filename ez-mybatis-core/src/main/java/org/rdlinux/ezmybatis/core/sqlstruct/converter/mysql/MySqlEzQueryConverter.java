package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

@SuppressWarnings("rawtypes")
public class MySqlEzQueryConverter extends AbstractConverter<EzQuery> implements Converter<EzQuery> {
    private static volatile MySqlEzQueryConverter instance;

    protected MySqlEzQueryConverter() {
    }

    public static MySqlEzQueryConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlEzQueryConverter.class) {
                if (instance == null) {
                    instance = new MySqlEzQueryConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       EzQuery obj, MybatisParamHolder mybatisParamHolder) {
        String sql = this.ezQueryToSql(configuration, obj, mybatisParamHolder);
        if (obj.getLimit() != null) {
            sql = " (SELECT * FROM " + sql + Alias.getAlias() + ") ";
        }
        return sqlBuilder.append(sql);
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
