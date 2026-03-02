package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlEzQueryConverter;

@SuppressWarnings("rawtypes")
public class OracleEzQueryConverter extends MySqlEzQueryConverter {
    private static volatile OracleEzQueryConverter instance;

    protected OracleEzQueryConverter() {
    }

    public static OracleEzQueryConverter getInstance() {
        if (instance == null) {
            synchronized (OracleEzQueryConverter.class) {
                if (instance == null) {
                    instance = new OracleEzQueryConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, EzQuery obj, SqlGenerateContext sqlGenerateContext) {
        String sql = this.ezQueryToSql(obj, sqlGenerateContext);
        sqlGenerateContext.getSqlBuilder().append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
