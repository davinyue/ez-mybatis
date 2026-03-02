package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.MySqlConverterRegister;

/**
 * MySQL方言提供者
 */
public class MySqlDialectProvider implements DbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.MYSQL;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.contains("mysql");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return MySqlSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        MySqlConverterRegister.register();
    }

    @Override
    public String getKeywordQuoteMark() {
        return "`";
    }
}
