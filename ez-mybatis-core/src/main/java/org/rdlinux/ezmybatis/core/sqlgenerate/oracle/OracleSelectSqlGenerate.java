package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;

public class OracleSelectSqlGenerate extends AbstractSelectSqlGenerate {
    private static volatile OracleSelectSqlGenerate instance;

    private OracleSelectSqlGenerate() {
    }

    public static OracleSelectSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleSelectSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleSelectSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return OracleEzQueryToSql.getInstance().toSql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return OracleEzQueryToSql.getInstance().toCountSql(sqlGenerateContext, query);
    }

    @Override
    public String getTableExistsSql(SqlGenerateContext sqlGenerateContext, DbTable table) {
        return "SELECT COUNT(1) FROM ALL_TABLES WHERE " +
                getTableExistsSchemaCondition(sqlGenerateContext, table) +
                " AND TABLE_NAME = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                table.getTableName(sqlGenerateContext.getConfiguration())).getFormatedName();
    }

    private String getTableExistsSchemaCondition(SqlGenerateContext sqlGenerateContext, DbTable table) {
        if (table.getSchema(sqlGenerateContext.getConfiguration()) == null) {
            return "OWNER = SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')";
        }
        return "OWNER = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                table.getSchema(sqlGenerateContext.getConfiguration())).getFormatedName();
    }
}
