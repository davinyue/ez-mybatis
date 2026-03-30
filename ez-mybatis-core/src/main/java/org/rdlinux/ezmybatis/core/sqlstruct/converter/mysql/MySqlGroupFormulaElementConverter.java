package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaElement;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.GroupFormulaElement;

public class MySqlGroupFormulaElementConverter extends AbstractConverter<GroupFormulaElement>
        implements Converter<GroupFormulaElement> {
    private static volatile MySqlGroupFormulaElementConverter instance;

    protected MySqlGroupFormulaElementConverter() {
    }

    public static MySqlGroupFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlGroupFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlGroupFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, GroupFormulaElement obj, SqlGenerateContext sqlGenerateContext) {
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" (");
        for (FormulaElement element : obj.getElements()) {
            Converter<? extends FormulaElement> converter = EzMybatisContent
                    .getConverter(sqlGenerateContext.getConfiguration(), element.getClass());
            converter.buildSql(type, element, sqlGenerateContext);
        }
        sqlBuilder.append(") ");
    }

}
