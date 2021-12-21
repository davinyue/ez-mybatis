package ink.dvc.ezmybatis.core.sqlgenerate.mysql;

import ink.dvc.ezmybatis.core.sqlgenerate.AbstractEzDeleteToSql;
import ink.dvc.ezmybatis.core.EzDelete;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;

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
