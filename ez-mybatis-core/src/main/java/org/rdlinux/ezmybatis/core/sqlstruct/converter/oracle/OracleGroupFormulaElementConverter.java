package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlGroupFormulaElementConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.GroupFormulaElement;

public class OracleGroupFormulaElementConverter extends MySqlGroupFormulaElementConverter
        implements Converter<GroupFormulaElement> {
    private static volatile OracleGroupFormulaElementConverter instance;

    protected OracleGroupFormulaElementConverter() {
    }

    public static OracleGroupFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (OracleGroupFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new OracleGroupFormulaElementConverter();
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
