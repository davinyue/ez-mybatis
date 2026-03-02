package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.DmConverterRegister;

/**
 * 达梦数据库方言提供者
 */
public class DmDialectProvider implements DbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.DM;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("dmdriver");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return DmSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        DmConverterRegister.register();
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
