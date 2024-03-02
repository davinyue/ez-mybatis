package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleDeleteSqlGenerate;

import java.util.Collection;

public class DmDeleteSqlGenerate extends OracleDeleteSqlGenerate {
    private static volatile DmDeleteSqlGenerate instance;

    private DmDeleteSqlGenerate() {
    }

    public static DmDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (DmDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new DmDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return DmEzDeleteToSql.getInstance().toSql(configuration, paramHolder, delete);
    }

    @Override
    public String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes) {
        return DmEzDeleteToSql.getInstance().toSql(configuration, paramHolder, deletes);
    }
}
