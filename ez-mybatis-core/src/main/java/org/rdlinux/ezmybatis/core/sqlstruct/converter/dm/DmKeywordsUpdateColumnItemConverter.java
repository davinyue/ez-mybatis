package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleKeywordsUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.KeywordsUpdateColumnItem;

public class DmKeywordsUpdateColumnItemConverter extends OracleKeywordsUpdateColumnItemConverter
        implements Converter<KeywordsUpdateColumnItem> {
    private static volatile DmKeywordsUpdateColumnItemConverter instance;

    protected DmKeywordsUpdateColumnItemConverter() {
    }

    public static DmKeywordsUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmKeywordsUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new DmKeywordsUpdateColumnItemConverter();
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
