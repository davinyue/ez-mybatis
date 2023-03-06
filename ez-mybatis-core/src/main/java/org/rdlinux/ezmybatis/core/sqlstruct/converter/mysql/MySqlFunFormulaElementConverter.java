package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FunFormulaElement;

public class MySqlFunFormulaElementConverter extends AbstractConverter<FunFormulaElement>
        implements Converter<FunFormulaElement> {
    private static volatile MySqlFunFormulaElementConverter instance;

    protected MySqlFunFormulaElementConverter() {
    }

    public static MySqlFunFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFunFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlFunFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FunFormulaElement ojb,
                                       MybatisParamHolder mybatisParamHolder) {
        Function function = ojb.getFunction();
        Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration, function.getClass());
        converter.buildSql(type, sqlBuilder, configuration, function, mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
