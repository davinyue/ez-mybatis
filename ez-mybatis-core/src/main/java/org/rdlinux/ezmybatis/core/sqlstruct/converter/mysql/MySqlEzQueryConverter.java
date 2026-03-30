package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProviderLoader;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.AliasGenerate;

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
    protected void doBuildSql(Type type, EzQuery obj, SqlGenerateContext sqlGenerateContext) {
        String sql = this.ezQueryToSql(obj, sqlGenerateContext);
        if (obj.getPage() != null) {
            sql = " (SELECT * FROM " + sql + AliasGenerate.getAlias() + ") ";
        }
        sqlGenerateContext.getSqlBuilder().append(sql);
    }

    protected String ezQueryToSql(EzQuery<?> obj, SqlGenerateContext sqlGenerateContext) {
        return " (" + DbDialectProviderLoader.getProvider(EzMybatisContent
                        .getDbType(sqlGenerateContext.getConfiguration()))
                .getSqlGenerate().getQuerySql(SqlGenerateContext.copyOf(sqlGenerateContext), obj) + ") ";
    }

}
