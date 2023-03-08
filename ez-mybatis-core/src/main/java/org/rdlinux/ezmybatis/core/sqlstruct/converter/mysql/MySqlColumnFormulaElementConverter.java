package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.ColumnFormulaElement;

public class MySqlColumnFormulaElementConverter extends AbstractConverter<ColumnFormulaElement>
        implements Converter<ColumnFormulaElement> {
    private static volatile MySqlColumnFormulaElementConverter instance;

    protected MySqlColumnFormulaElementConverter() {
    }

    public static MySqlColumnFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlColumnFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlColumnFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ColumnFormulaElement obj,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" ")
                .append(obj.getTable().getAlias()).append(".").append(keywordQM)
                .append(obj.getColumn()).append(keywordQM).append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
