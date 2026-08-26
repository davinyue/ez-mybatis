package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.PhysicalTableRoute;

public class SqlServerSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile SqlServerSelectSqlGenerate instance;

    private SqlServerSelectSqlGenerate() {
    }

    public static SqlServerSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new SqlServerSelectSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return SqlServerEzQueryToSql.getInstance().toSql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return SqlServerEzQueryToSql.getInstance().toCountSql(sqlGenerateContext, query);
    }

    @Override
    public String getTableExistsSql(SqlGenerateContext sqlGenerateContext, DbTable table) {
        PhysicalTableRoute route = EzMybatisContent.resolveDynamicTableRoute(
                sqlGenerateContext.getConfiguration(), table);
        return "SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE " +
                getTableExistsSchemaCondition(sqlGenerateContext, route.getSchema()) +
                " AND TABLE_NAME = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                route.getTableName()).getFormatedName();
    }

    private String getTableExistsSchemaCondition(SqlGenerateContext sqlGenerateContext, String schema) {
        if (schema == null) {
            return "TABLE_SCHEMA = SCHEMA_NAME()";
        }
        return "TABLE_SCHEMA = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                schema).getFormatedName();
    }
}
