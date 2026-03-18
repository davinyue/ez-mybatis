package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

import java.util.Collection;

public class OracleEzDeleteToSql extends AbstractEzDeleteToSql {
    private static volatile OracleEzDeleteToSql instance;

    protected OracleEzDeleteToSql() {
    }

    public static OracleEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (OracleEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new OracleEzDeleteToSql();
                }
            }
        }
        return instance;
    }


    @Override
    public String toSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes) {
        String sql = super.toSql(sqlGenerateContext, deletes);
        return "BEGIN \n" + sql + "END;";
    }

    @Override
    protected void joinsToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
    }
}
