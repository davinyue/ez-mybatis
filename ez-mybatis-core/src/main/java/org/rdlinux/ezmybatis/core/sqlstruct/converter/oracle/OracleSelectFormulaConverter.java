package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectFormulaConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectFormula;

public class OracleSelectFormulaConverter extends MySqlSelectFormulaConverter implements Converter<SelectFormula> {
    private static volatile OracleSelectFormulaConverter instance;

    protected OracleSelectFormulaConverter() {
    }

    public static OracleSelectFormulaConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectFormulaConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectFormulaConverter();
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
