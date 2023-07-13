package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FormulaCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;

public class MySqlFormulaCompareValueConditionConverter extends AbstractConverter<FormulaCompareValueCondition>
        implements Converter<FormulaCompareValueCondition> {
    private static volatile MySqlFormulaCompareValueConditionConverter instance;

    protected MySqlFormulaCompareValueConditionConverter() {
    }

    public static MySqlFormulaCompareValueConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaCompareValueConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaCompareValueConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaCompareValueCondition obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration,
                ((Formula) obj.getFormula()).getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getFormula(), mybatisParamHolder);
        sqlBuilder.append(" ").append(obj.getOperator().getOperator()).append(" ");
        return sqlBuilder.append(mybatisParamHolder.getMybatisParamName(obj.getValue())).append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
