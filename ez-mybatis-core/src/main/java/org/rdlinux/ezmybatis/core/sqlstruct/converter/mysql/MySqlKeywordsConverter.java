package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Keywords;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class MySqlKeywordsConverter extends AbstractConverter<Keywords> implements Converter<Keywords> {
    private static volatile MySqlKeywordsConverter instance;

    protected MySqlKeywordsConverter() {
    }

    public static MySqlKeywordsConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlKeywordsConverter.class) {
                if (instance == null) {
                    instance = new MySqlKeywordsConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Keywords obj, SqlGenerateContext sqlGenerateContext) {
        sqlGenerateContext.getSqlBuilder().append(SqlEscaping.nameEscaping(obj.getKeywords()));
    }

}
