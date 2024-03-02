package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

import java.util.Collection;

public class OracleDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile OracleDeleteSqlGenerate instance;

    protected OracleDeleteSqlGenerate() {
    }

    public static OracleDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return OracleEzDeleteToSql.getInstance().toSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes) {
        return OracleEzDeleteToSql.getInstance().toSql(configuration, paramHolder, deletes);
    }
}
