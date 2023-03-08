package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFormulaFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaFormulaElement;

public class OracleFormulaFormulaElementConverter extends MySqlFormulaFormulaElementConverter
        implements Converter<FormulaFormulaElement> {
    private static volatile OracleFormulaFormulaElementConverter instance;

    protected OracleFormulaFormulaElementConverter() {
    }

    public static OracleFormulaFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFormulaFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleFormulaFormulaElementConverter();
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
