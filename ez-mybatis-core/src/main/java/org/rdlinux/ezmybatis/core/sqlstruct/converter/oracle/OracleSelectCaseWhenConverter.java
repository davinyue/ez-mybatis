package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectCaseWhenConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectCaseWhen;

public class OracleSelectCaseWhenConverter extends MySqlSelectCaseWhenConverter implements Converter<SelectCaseWhen> {
    private static volatile OracleSelectCaseWhenConverter instance;

    protected OracleSelectCaseWhenConverter() {
    }

    public static OracleSelectCaseWhenConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectCaseWhenConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectCaseWhenConverter();
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
