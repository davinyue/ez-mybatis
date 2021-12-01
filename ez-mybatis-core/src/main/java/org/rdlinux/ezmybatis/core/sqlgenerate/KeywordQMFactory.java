package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.constant.DbType;

import java.util.HashMap;
import java.util.Map;

public class KeywordQMFactory {
    private static final Map<DbType, KeywordQM> DB_TYPE_MAP_KEYWORD_QM = new HashMap<>();

    static {
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.MYSQL, () -> "`");
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.ORACLE, () -> "\"");
    }

    public static String getKeywordQM(DbType dbType) {
        return DB_TYPE_MAP_KEYWORD_QM.get(dbType).getKeywordQM();
    }
}
