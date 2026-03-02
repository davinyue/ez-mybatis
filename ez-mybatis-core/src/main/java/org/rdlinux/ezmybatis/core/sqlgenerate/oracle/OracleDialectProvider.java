package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.OracleConverterRegister;

/**
 * Oracle方言提供者
 */
public class OracleDialectProvider implements DbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.ORACLE;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.contains("oracle");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return OracleSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        OracleConverterRegister.register();
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
