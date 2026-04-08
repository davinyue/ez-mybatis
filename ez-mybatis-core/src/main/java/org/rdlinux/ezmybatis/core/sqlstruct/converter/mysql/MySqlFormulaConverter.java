package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, Formula obj, SqlGenerateContext sqlGenerateContext) {
        List<FormulaElement> elements = obj.getElements();
        if (elements == null) {
            return;
        }
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(" (");
        for (FormulaElement element : elements) {
            Converter<? extends FormulaElement> converter = EzMybatisContent
                    .getConverter(sqlGenerateContext.getConfiguration(), element.getClass());
            converter.buildSql(type, element, sqlGenerateContext);
        }
        sqlBuilder.append(") ");
    }

}
