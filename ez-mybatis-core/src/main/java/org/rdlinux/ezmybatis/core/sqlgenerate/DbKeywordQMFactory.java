package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库关键字引号
 */
public class DbKeywordQMFactory {
    private static final Map<DbType, String> DB_TYPE_MAP_KEYWORD_QM = new HashMap<>();

    static {
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.MYSQL, "`");
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.ORACLE, "\"");
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.DM, "\"");
    }

    private EzMybatisConfig ezMybatisConfig;

    public DbKeywordQMFactory(EzMybatisConfig ezMybatisConfig) {
        this.ezMybatisConfig = ezMybatisConfig;
    }

    public String getKeywordQM(DbType dbType) {
        if (this.ezMybatisConfig.isEscapeKeyword()) {
            return DB_TYPE_MAP_KEYWORD_QM.get(dbType);
        } else {
            return "";
        }
    }
}
