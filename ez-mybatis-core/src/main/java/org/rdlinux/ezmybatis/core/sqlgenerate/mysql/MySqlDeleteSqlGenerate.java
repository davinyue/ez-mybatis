package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDeleteSqlGenerate;

import java.util.List;
import java.util.Map;

public class MySqlDeleteSqlGenerate extends AbstractDeleteSqlGenerate {
    private static volatile MySqlDeleteSqlGenerate instance;

    private MySqlDeleteSqlGenerate() {
        super(MySqlSelectSqlGenerate.getInstance());
    }

    public static MySqlDeleteSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlDeleteSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlDeleteSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getKeywordQM() {
        return "`";
    }

    @Override
    public String getDeleteSql(Configuration configuration, EzDelete delete, Map<String, Object> mybatisParam) {
        return MySqlEzDeleteToSql.getInstance().toSql(configuration, delete, mybatisParam);
    }

    @Override
    public String getDeleteSql(Configuration configuration, List<EzDelete> deletes, Map<String, Object> mybatisParam) {
        return MySqlEzDeleteToSql.getInstance().toSql(configuration, deletes, mybatisParam);
    }
}
