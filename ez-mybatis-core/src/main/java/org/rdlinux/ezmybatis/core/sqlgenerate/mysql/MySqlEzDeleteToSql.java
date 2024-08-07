package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.List;

public class MySqlEzDeleteToSql extends AbstractEzDeleteToSql {
    private static volatile MySqlEzDeleteToSql instance;

    private MySqlEzDeleteToSql() {
    }

    public static MySqlEzDeleteToSql getInstance() {
        if (instance == null) {
            synchronized (MySqlEzDeleteToSql.class) {
                if (instance == null) {
                    instance = new MySqlEzDeleteToSql();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder deleteToSql(StringBuilder sqlBuilder, EzDelete delete) {
        StringBuilder sql = super.deleteToSql(sqlBuilder, delete);
        List<Table> deleteTables = delete.getDeletes();
        for (int i = 0; i < deleteTables.size(); i++) {
            sql.append(deleteTables.get(i).getAlias());
            if (i + 1 < deleteTables.size()) {
                sql.append(", ");
            }
        }
        return sql;
    }
}
