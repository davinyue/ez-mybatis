package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.SqlServerConverterRegister;

/**
 * SQL Server方言提供者
 */
public class SqlServerDialectProvider implements DbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.SQL_SERVER;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("sqlserver");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return SqlServerSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        SqlServerConverterRegister.register();
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
