package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractUpdateSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class OracleUpdateSqlGenerate extends AbstractUpdateSqlGenerate {
    private static volatile OracleUpdateSqlGenerate instance;

    private OracleUpdateSqlGenerate() {
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
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return "BEGIN \n" + super.getBatchUpdateSql(configuration, mybatisParamHolder, table, models, isReplace)
                + " END;";
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        return OracleEzUpdateToSql.getInstance().toSql(configuration, mybatisParamHolder, update);
    }

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                               Collection<EzUpdate> updates) {
        return OracleEzUpdateToSql.getInstance().toSql(configuration, mybatisParamHolder, updates);
    }
}
