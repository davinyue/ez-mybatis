package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       GroupFormulaElement obj,
                                       MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" (");
        for (FormulaElement element : obj.getElements()) {
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
