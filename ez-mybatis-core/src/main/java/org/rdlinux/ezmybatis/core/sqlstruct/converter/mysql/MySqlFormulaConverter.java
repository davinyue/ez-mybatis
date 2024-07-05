package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaElement;

import java.util.List;

public class MySqlFormulaConverter extends AbstractConverter<Formula> implements Converter<Formula> {
    private static volatile MySqlFormulaConverter instance;

    protected MySqlFormulaConverter() {
    }

    public static MySqlFormulaConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Formula obj,
                                       MybatisParamHolder mybatisParamHolder) {
        List<FormulaElement> elements = obj.getElements();
        if (elements == null) {
            return sqlBuilder;
        }
        sqlBuilder.append(" (");
        for (FormulaElement element : elements) {
            Converter<? extends FormulaElement> converter = EzMybatisContent.getConverter(configuration,
                    element.getClass());
            converter.buildSql(type, sqlBuilder, configuration, element, mybatisParamHolder);
        }
        sqlBuilder.append(") ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
