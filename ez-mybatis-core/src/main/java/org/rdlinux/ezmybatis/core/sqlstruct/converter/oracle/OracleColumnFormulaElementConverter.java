package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlColumnFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.ColumnFormulaElement;

public class OracleColumnFormulaElementConverter extends MySqlColumnFormulaElementConverter
        implements Converter<ColumnFormulaElement> {
    private static volatile OracleColumnFormulaElementConverter instance;

    protected OracleColumnFormulaElementConverter() {
    }

    public static OracleColumnFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleColumnFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleColumnFormulaElementConverter();
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
