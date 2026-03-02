package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.PostgreSqlConverterRegister;

/**
 * PostgreSQL方言提供者
 */
public class PostgreDialectProvider implements DbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.POSTGRE_SQL;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("postgresql");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return PostgreSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        PostgreSqlConverterRegister.register();
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
