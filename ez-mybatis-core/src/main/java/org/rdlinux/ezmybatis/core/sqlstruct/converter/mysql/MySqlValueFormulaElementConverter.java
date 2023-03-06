package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.ValueFormulaElement;

public class MySqlValueFormulaElementConverter extends AbstractConverter<ValueFormulaElement>
        implements Converter<ValueFormulaElement> {
    private static volatile MySqlValueFormulaElementConverter instance;

    protected MySqlValueFormulaElementConverter() {
    }

    public static MySqlValueFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlValueFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlValueFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ValueFormulaElement ojb,
                                       MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(mybatisParamHolder.getMybatisParamName(ojb.getValue()));
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
