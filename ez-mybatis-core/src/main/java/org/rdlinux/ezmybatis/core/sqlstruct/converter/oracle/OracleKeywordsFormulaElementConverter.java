package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlKeywordsFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.KeywordsFormulaElement;

public class OracleKeywordsFormulaElementConverter extends MySqlKeywordsFormulaElementConverter
        implements Converter<KeywordsFormulaElement> {
    private static volatile OracleKeywordsFormulaElementConverter instance;

    protected OracleKeywordsFormulaElementConverter() {
    }

    public static OracleKeywordsFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleKeywordsFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleKeywordsFormulaElementConverter();
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
