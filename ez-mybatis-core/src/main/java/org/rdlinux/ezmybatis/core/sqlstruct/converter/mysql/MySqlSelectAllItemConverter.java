package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAllItem;

public class MySqlSelectAllItemConverter extends AbstractConverter<SelectAllItem> implements Converter<SelectAllItem> {
    private static volatile MySqlSelectAllItemConverter instance;

    protected MySqlSelectAllItemConverter() {
    }

    public static MySqlSelectAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectAllItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectAllItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, SelectAllItem ojb, SqlGenerateContext sqlGenerateContext) {
        sqlGenerateContext.getSqlBuilder().append(" * ");
    }

}
