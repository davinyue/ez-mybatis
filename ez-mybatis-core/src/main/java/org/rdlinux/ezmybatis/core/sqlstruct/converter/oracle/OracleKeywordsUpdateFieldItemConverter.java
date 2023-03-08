package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlKeywordsUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.KeywordsUpdateFieldItem;

public class OracleKeywordsUpdateFieldItemConverter extends MySqlKeywordsUpdateFieldItemConverter
        implements Converter<KeywordsUpdateFieldItem> {
    private static volatile OracleKeywordsUpdateFieldItemConverter instance;

    protected OracleKeywordsUpdateFieldItemConverter() {
    }

    public static OracleKeywordsUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleKeywordsUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new OracleKeywordsUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
