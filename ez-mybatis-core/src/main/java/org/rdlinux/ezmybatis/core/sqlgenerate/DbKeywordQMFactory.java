package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

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

    private String keywordQM;

    public DbKeywordQMFactory(EzMybatisConfig ezMybatisConfig) {
        if (ezMybatisConfig.isEscapeKeyword()) {
            this.keywordQM = DB_TYPE_MAP_KEYWORD_QM.get(DbTypeUtils.getDbType(ezMybatisConfig.getConfiguration()));
        } else {
            this.keywordQM = "";
        }
    }

    public String getKeywordQM() {
        return this.keywordQM;
    }
}
