package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlSqlGenerate;

import java.util.HashMap;
import java.util.Map;

public class SqlGenerateFactory {
    private static final Map<String, SqlGenerate> sqlGenerateMap = new HashMap<>();

    static {
        sqlGenerateMap.put("mysql", MySqlSqlGenerate.getInstance());
    }

    public static SqlGenerate getSqlGenerate(Configuration configuration) {
        return sqlGenerateMap.get("mysql");
    }
}
