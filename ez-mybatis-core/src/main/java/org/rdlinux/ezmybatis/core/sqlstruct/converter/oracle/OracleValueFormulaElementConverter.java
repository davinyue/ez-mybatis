package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlValueFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.ValueFormulaElement;

public class OracleValueFormulaElementConverter extends MySqlValueFormulaElementConverter
        implements Converter<ValueFormulaElement> {
    private static volatile OracleValueFormulaElementConverter instance;

    protected OracleValueFormulaElementConverter() {
    }

    public static OracleValueFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleValueFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleValueFormulaElementConverter();
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
