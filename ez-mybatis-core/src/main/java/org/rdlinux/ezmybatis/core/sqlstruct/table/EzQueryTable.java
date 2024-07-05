package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.utils.AliasGenerate;
import org.rdlinux.ezmybatis.utils.Assert;

public class EzQueryTable extends AbstractTable implements Table {
    private EzQuery<?> ezQuery;

    private EzQueryTable(EzQuery<?> ezQuery) {
        super(AliasGenerate.getAlias(), null);
        Assert.notNull(ezQuery, "ezQuery can not be null");
        this.ezQuery = ezQuery;
    }

    public static EzQueryTable of(EzQuery<?> ezQuery) {
        return new EzQueryTable(ezQuery);
    }

    public EzQuery<?> getEzQuery() {
        return this.ezQuery;
    }

    public void setEzQuery(EzQuery<?> ezQuery) {
        this.ezQuery = ezQuery;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getTableName(Configuration configuration) {
        return this.alias;
    }

    @Override
    public String getSchema(Configuration configuration) {
        return null;
    }
}
