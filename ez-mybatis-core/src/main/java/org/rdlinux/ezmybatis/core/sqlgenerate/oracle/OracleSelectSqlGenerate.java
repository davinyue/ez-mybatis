package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractSelectSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.PhysicalTableRoute;

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
        PhysicalTableRoute route = EzMybatisContent.resolveDynamicTableRoute(
                sqlGenerateContext.getConfiguration(), table);
        return "SELECT COUNT(1) FROM ALL_TABLES WHERE " +
                getTableExistsSchemaCondition(sqlGenerateContext, route.getSchema()) +
                " AND TABLE_NAME = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                route.getTableName()).getFormatedName();
    }

    private String getTableExistsSchemaCondition(SqlGenerateContext sqlGenerateContext, String schema) {
        if (schema == null) {
            return "OWNER = SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')";
        }
        return "OWNER = " + sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(
                schema).getFormatedName();
    }
}
