package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaFormulaElement;

public class MySqlFormulaFormulaElementConverter extends AbstractConverter<FormulaFormulaElement>
        implements Converter<FormulaFormulaElement> {
    private static volatile MySqlFormulaFormulaElementConverter instance;

    protected MySqlFormulaFormulaElementConverter() {
    }

    public static MySqlFormulaFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaFormulaElement obj,
                                       MybatisParamHolder mybatisParamHolder) {
        Formula formula = obj.getFormula();
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" ");
        Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration, formula.getClass());
        sqlBuilder = converter.buildSql(type, sqlBuilder, configuration, formula, mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
