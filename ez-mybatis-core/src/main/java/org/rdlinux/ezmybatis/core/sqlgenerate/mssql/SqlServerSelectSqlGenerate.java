package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;

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
        return "SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE " +
                getTableExistsSchemaCondition(sqlGenerateContext, table) +
                " AND TABLE_NAME = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                table.getTableName(sqlGenerateContext.getConfiguration())).getFormatedName();
    }

    private String getTableExistsSchemaCondition(SqlGenerateContext sqlGenerateContext, DbTable table) {
        if (table.getSchema(sqlGenerateContext.getConfiguration()) == null) {
            return "TABLE_SCHEMA = SCHEMA_NAME()";
        }
        return "TABLE_SCHEMA = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                table.getSchema(sqlGenerateContext.getConfiguration())).getFormatedName();
    }
}
