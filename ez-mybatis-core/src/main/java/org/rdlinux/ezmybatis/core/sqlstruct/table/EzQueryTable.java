package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

public class EzQueryTable implements Table {
    private String alias;
    private EzQuery<?> ezQuery;

    private EzQueryTable(EzQuery<?> ezQuery) {
        Assert.notNull(ezQuery, "ezQuery can not be null");
        this.ezQuery = ezQuery;
        this.alias = Alias.getAlias();
    }

    public static EzQueryTable of(EzQuery<?> ezQuery) {
        return new EzQueryTable(ezQuery);
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getTableName(Configuration configuration) {
        return this.alias;
    }

    @Override
    public String toSqlStruct(Configuration configuration, MybatisParamHolder paramHolder) {
        String querySql = SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                .getQuerySql(configuration, paramHolder, this.ezQuery);
        return " (" + querySql + ") " + this.alias + " ";
    }
}
