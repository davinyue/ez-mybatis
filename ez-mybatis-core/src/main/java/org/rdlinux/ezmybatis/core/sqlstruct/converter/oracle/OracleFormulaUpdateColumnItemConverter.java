package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFormulaUpdateColumnItemConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FormulaUpdateColumnItem;

public class OracleFormulaUpdateColumnItemConverter extends MySqlFormulaUpdateColumnItemConverter
        implements Converter<FormulaUpdateColumnItem> {
    private static volatile OracleFormulaUpdateColumnItemConverter instance;

    protected OracleFormulaUpdateColumnItemConverter() {
    }

    public static OracleFormulaUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFormulaUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new OracleFormulaUpdateColumnItemConverter();
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
