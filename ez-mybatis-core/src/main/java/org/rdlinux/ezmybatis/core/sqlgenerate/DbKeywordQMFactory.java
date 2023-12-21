package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;

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
        DB_TYPE_MAP_KEYWORD_QM.put(DbType.POSTGRE_SQL, "\"");
    }

    private EzMybatisConfig ezMybatisConfig;
    private volatile String keywordQM;

    public DbKeywordQMFactory(EzMybatisConfig ezMybatisConfig) {
        this.ezMybatisConfig = ezMybatisConfig;
    }

    public String getKeywordQM() {
        if (this.keywordQM == null) {
            synchronized (this) {
                if (this.keywordQM != null) {
                    return this.keywordQM;
                }
                if (this.ezMybatisConfig.isEscapeKeyword()) {
                    this.keywordQM = DB_TYPE_MAP_KEYWORD_QM.get(EzMybatisContent
                            .getDbType(this.ezMybatisConfig.getConfiguration()));
                } else {
                    this.keywordQM = "";
                }
            }
        }
        return this.keywordQM;
    }
}
