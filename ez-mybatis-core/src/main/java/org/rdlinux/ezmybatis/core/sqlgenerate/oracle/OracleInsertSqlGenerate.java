package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

public class OracleInsertSqlGenerate extends AbstractInsertSqlGenerate {
    private static volatile OracleInsertSqlGenerate instance;

    private OracleInsertSqlGenerate() {
    }

    public static OracleInsertSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (OracleInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new OracleInsertSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getBatchInsertSql(SqlGenerateContext sqlGenerateContext,
                                    Table table, Collection<Object> models) {
        Assert.notEmpty(models, "models cannot be empty");
        StringBuilder sqlBuilder = new StringBuilder("INSERT ALL\n");
        for (Object entity : models) {
            String tableName = getTableName(sqlGenerateContext, table, entity);
            InsertSqlParts insertSqlParts = getInsertSqlParts(sqlGenerateContext, entity);
            sqlBuilder.append("INTO ")
                    .append(tableName)
                    .append(" ")
                    .append(insertSqlParts.getColumnsSql())
                    .append(" VALUES ")
                    .append(insertSqlParts.getValuesSql())
                    .append("\n");
        }
        sqlBuilder.append("SELECT 1 FROM DUAL");
        return sqlBuilder.toString();
    }
}
