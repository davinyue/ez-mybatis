package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFunFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FunFormulaElement;

public class OracleFunFormulaElementConverter extends MySqlFunFormulaElementConverter
        implements Converter<FunFormulaElement> {
    private static volatile OracleFunFormulaElementConverter instance;

    protected OracleFunFormulaElementConverter() {
    }

    public static OracleFunFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFunFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleFunFormulaElementConverter();
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
