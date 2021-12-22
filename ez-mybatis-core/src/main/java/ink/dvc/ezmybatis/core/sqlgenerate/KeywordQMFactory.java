package ink.dvc.ezmybatis.core.sqlgenerate;

import ink.dvc.ezmybatis.core.constant.DbType;

import java.util.HashMap;
import java.util.Map;

public class KeywordQMFactory {
    private static final Map<DbType, KeywordQM> DB_TYPE_MAP_KEYWORD_QM = new HashMap<>();

    static {
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.MYSQL, () -> "`");
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.ORACLE, () -> "\"");
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.DM, () -> "\"");
    }

    public static String getKeywordQM(DbType dbType) {
        return DB_TYPE_MAP_KEYWORD_QM.get(dbType).getKeywordQM();
    }
}
