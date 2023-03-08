package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleKeywordsUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.KeywordsUpdateFieldItem;

public class DmKeywordsUpdateFieldItemConverter extends OracleKeywordsUpdateFieldItemConverter
        implements Converter<KeywordsUpdateFieldItem> {
    private static volatile DmKeywordsUpdateFieldItemConverter instance;

    protected DmKeywordsUpdateFieldItemConverter() {
    }

    public static DmKeywordsUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (DmKeywordsUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new DmKeywordsUpdateFieldItemConverter();
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
