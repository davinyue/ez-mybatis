package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaOperandElement;

public class MySqlFormulaOperandElementConverter extends AbstractConverter<FormulaOperandElement>
        implements Converter<FormulaOperandElement> {
    private static volatile MySqlFormulaOperandElementConverter instance;

    protected MySqlFormulaOperandElementConverter() {
    }

    public static MySqlFormulaOperandElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaOperandElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaOperandElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaOperandElement obj,
                                       MybatisParamHolder mybatisParamHolder) {
        Operand operand = obj.getOperand();
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" ");
        Converter<? extends Operand> converter = EzMybatisContent.getConverter(configuration, operand.getClass());
        converter.buildSql(type, sqlBuilder, configuration, operand, mybatisParamHolder);
        sqlBuilder.append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
