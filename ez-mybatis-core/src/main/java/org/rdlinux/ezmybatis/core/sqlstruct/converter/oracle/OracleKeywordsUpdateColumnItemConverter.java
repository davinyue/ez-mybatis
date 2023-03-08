package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlKeywordsUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.KeywordsUpdateColumnItem;

public class OracleKeywordsUpdateColumnItemConverter extends MySqlKeywordsUpdateColumnItemConverter
        implements Converter<KeywordsUpdateColumnItem> {
    private static volatile OracleKeywordsUpdateColumnItemConverter instance;

    protected OracleKeywordsUpdateColumnItemConverter() {
    }

    public static OracleKeywordsUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleKeywordsUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new OracleKeywordsUpdateColumnItemConverter();
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
