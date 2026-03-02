package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;

import java.util.Collection;

public class MySqlUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile MySqlUpdateSqlGenerate instance;

    private MySqlUpdateSqlGenerate() {
    }

    public static MySqlUpdateSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlUpdateSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        return MySqlEzUpdateToSql.getInstance().toSql(sqlGenerateContext, update);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, Collection<EzUpdate> updates) {
        return MySqlEzUpdateToSql.getInstance().toSql(sqlGenerateContext, updates);
    }
}
