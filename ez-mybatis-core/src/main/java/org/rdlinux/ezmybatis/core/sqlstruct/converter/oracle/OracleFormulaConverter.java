package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFormulaConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;

public class OracleFormulaConverter extends MySqlFormulaConverter implements Converter<Formula> {
    private static volatile OracleFormulaConverter instance;

    protected OracleFormulaConverter() {
    }

    public static OracleFormulaConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFormulaConverter.class) {
                if (instance == null) {
                    instance = new OracleFormulaConverter();
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
