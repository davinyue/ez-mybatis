package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class OracleUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile OracleUpdateSqlGenerate instance;

    protected OracleUpdateSqlGenerate() {
    }

    public static OracleUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getBatchUpdateSql(SqlGenerateContext sqlGenerateContext,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return "BEGIN \n" + super.getBatchUpdateSql(sqlGenerateContext, table, models, isReplace)
                + " END;";
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        return OracleEzUpdateToSql.getInstance().toSql(sqlGenerateContext, update);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext,
                               Collection<EzUpdate> updates) {
        return OracleEzUpdateToSql.getInstance().toSql(sqlGenerateContext, updates);
    }
}
