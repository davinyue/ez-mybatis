package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.DmEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleDialectProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.Select;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.dm.DmLimitConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.dm.DmPageConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.dm.DmSelectConverter;

/**
 * 达梦数据库方言提供者
 */
public class DmDialectProvider extends OracleDialectProvider {
    public DmDialectProvider() {
        this.setEntityInfoBuilder(DmEntityInfoBuilder.getInstance());
    }


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
        super.registerConverters();
        this.addConverter(Select.class, DmSelectConverter.getInstance());
        this.addConverter(Page.class, DmPageConverter.getInstance());
        this.addConverter(Limit.class, DmLimitConverter.getInstance());
    }
}
