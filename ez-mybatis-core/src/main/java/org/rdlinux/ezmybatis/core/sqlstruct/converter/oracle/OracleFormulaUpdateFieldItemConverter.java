package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFormulaUpdateFieldItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FormulaUpdateFieldItem;

public class OracleFormulaUpdateFieldItemConverter extends MySqlFormulaUpdateFieldItemConverter
        implements Converter<FormulaUpdateFieldItem> {
    private static volatile OracleFormulaUpdateFieldItemConverter instance;

    protected OracleFormulaUpdateFieldItemConverter() {
    }

    public static OracleFormulaUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFormulaUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new OracleFormulaUpdateFieldItemConverter();
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
