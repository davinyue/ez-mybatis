package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.PhysicalTableRoute;

public class MySqlSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile MySqlSelectSqlGenerate instance;

    private MySqlSelectSqlGenerate() {
    }

    public static MySqlSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MySqlSelectSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return MySqlEzQueryToSql.getInstance().toSql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return MySqlEzQueryToSql.getInstance().toCountSql(sqlGenerateContext, query);
    }

    @Override
    public String getTableExistsSql(SqlGenerateContext sqlGenerateContext, DbTable table) {
        PhysicalTableRoute route = EzMybatisContent.resolveDynamicTableRoute(
                sqlGenerateContext.getConfiguration(), table);
        return "SELECT COUNT(1) FROM information_schema.tables WHERE " +
                getTableExistsSchemaCondition(sqlGenerateContext, route.getSchema()) +
                " AND table_name = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                route.getTableName()).getFormatedName();
    }

    private String getTableExistsSchemaCondition(SqlGenerateContext sqlGenerateContext, String schema) {
        if (schema == null) {
            return "table_schema = DATABASE()";
        }
        return "table_schema = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                schema).getFormatedName();
    }
}
