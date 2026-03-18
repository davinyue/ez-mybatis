package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleUpdateSqlGenerate;

import java.util.Collection;

public class DmUpdateSqlGenerate extends OracleUpdateSqlGenerate {
    private static volatile DmUpdateSqlGenerate instance;

    private DmUpdateSqlGenerate() {
    }

    public static DmUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (DmUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new DmUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        return DmEzUpdateToSql.getInstance().toSql(sqlGenerateContext, update);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, Collection<EzUpdate> updates) {
        return DmEzUpdateToSql.getInstance().toSql(sqlGenerateContext, updates);
    }
}
