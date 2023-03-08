package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleSelectKeywordsConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectKeywords;

public class DmSelectKeywordsConverter extends OracleSelectKeywordsConverter implements Converter<SelectKeywords> {
    private static volatile DmSelectKeywordsConverter instance;

    protected DmSelectKeywordsConverter() {
    }

    public static DmSelectKeywordsConverter getInstance() {
        if (instance == null) {
            synchronized (DmSelectKeywordsConverter.class) {
                if (instance == null) {
                    instance = new DmSelectKeywordsConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
