package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.CaseWhenFormulaElement;

public class MySqlCaseWhenFormulaElementConverter extends AbstractConverter<CaseWhenFormulaElement>
        implements Converter<CaseWhenFormulaElement> {
    private static volatile MySqlCaseWhenFormulaElementConverter instance;

    protected MySqlCaseWhenFormulaElementConverter() {
    }

    public static MySqlCaseWhenFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlCaseWhenFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlCaseWhenFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhenFormulaElement obj,
                                       MybatisParamHolder mybatisParamHolder) {
        CaseWhen caseWhen = obj.getCaseWhen();
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" ");
        Converter<? extends CaseWhen> converter = EzMybatisContent.getConverter(configuration, caseWhen.getClass());
        converter.buildSql(type, sqlBuilder, configuration, caseWhen, mybatisParamHolder);
        sqlBuilder.append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
