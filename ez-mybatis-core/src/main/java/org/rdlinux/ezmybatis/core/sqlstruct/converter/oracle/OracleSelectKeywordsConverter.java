package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectKeywordsConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectKeywords;

public class OracleSelectKeywordsConverter extends MySqlSelectKeywordsConverter implements Converter<SelectKeywords> {
    private static volatile OracleSelectKeywordsConverter instance;

    protected OracleSelectKeywordsConverter() {
    }

    public static OracleSelectKeywordsConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectKeywordsConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectKeywordsConverter();
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
