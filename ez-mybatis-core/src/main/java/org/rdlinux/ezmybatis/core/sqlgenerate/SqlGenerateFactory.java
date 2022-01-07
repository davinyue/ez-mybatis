package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.dm.DmSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleSqlGenerate;

import java.util.HashMap;
import java.util.Map;

public class SqlGenerateFactory {
    private static final Map<DbType, SqlGenerate> sqlGenerateMap = new HashMap<>();


    static {
        sqlGenerateMap.put(DbType.MYSQL, MySqlSqlGenerate.getInstance());
        sqlGenerateMap.put(DbType.ORACLE, OracleSqlGenerate.getInstance());
        sqlGenerateMap.put(DbType.DM, DmSqlGenerate.getInstance());
    }

    public static SqlGenerate getSqlGenerate(DbType dbType) {
        return sqlGenerateMap.get(dbType);
    }
}
