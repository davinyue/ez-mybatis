package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleKeywordsFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.KeywordsFormulaElement;

public class DmKeywordsFormulaElementConverter extends OracleKeywordsFormulaElementConverter
        implements Converter<KeywordsFormulaElement> {
    private static volatile DmKeywordsFormulaElementConverter instance;

    protected DmKeywordsFormulaElementConverter() {
    }

    public static DmKeywordsFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (DmKeywordsFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new DmKeywordsFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.DM;
    }
}
