package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Union;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.AliasGenerate;

public class MySqlUnionConverter extends AbstractConverter<Union> implements Converter<Union> {
    private static volatile MySqlUnionConverter instance;

    protected MySqlUnionConverter() {
    }

    public static MySqlUnionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUnionConverter.class) {
                if (instance == null) {
                    instance = new MySqlUnionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Union obj, SqlGenerateContext sqlGenerateContext) {
        if (obj.getQuery() == null) {
            return;
        }
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(" UNION ");
        if (obj.isAll()) {
            sqlBuilder.append(" ALL ");
        }
        sqlBuilder.append("\n(");
        Configuration configuration = sqlGenerateContext.getConfiguration();
        String unionSql = EzMybatisContent.getDbDialectProvider(configuration)
                .getSqlGenerate().getQuerySql(SqlGenerateContext.copyOf(sqlGenerateContext), obj.getQuery());
        if (obj.getQuery().getOrderBy() != null) {
            unionSql = String.format("SELECT * FROM (%s) %s", unionSql, AliasGenerate.getAlias());
        }
        sqlBuilder.append(unionSql).append(") ");
    }


}
