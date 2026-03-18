package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, FormulaOperandElement obj, SqlGenerateContext sqlGenerateContext) {
        Operand operand = obj.getOperand();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" ");
        Converter<? extends Operand> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                operand.getClass());
        converter.buildSql(type, operand, sqlGenerateContext);
        sqlBuilder.append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
