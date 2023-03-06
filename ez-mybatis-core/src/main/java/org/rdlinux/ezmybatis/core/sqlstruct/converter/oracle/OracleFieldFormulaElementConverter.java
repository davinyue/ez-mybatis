package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFieldFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FieldFormulaElement;

public class OracleFieldFormulaElementConverter extends MySqlFieldFormulaElementConverter
        implements Converter<FieldFormulaElement> {
    private static volatile OracleFieldFormulaElementConverter instance;

    protected OracleFieldFormulaElementConverter() {
    }

    public static OracleFieldFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFieldFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleFieldFormulaElementConverter();
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
