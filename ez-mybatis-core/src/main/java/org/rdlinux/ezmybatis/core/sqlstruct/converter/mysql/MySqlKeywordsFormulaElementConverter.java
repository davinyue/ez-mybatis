package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.KeywordsFormulaElement;

public class MySqlKeywordsFormulaElementConverter extends AbstractConverter<KeywordsFormulaElement>
        implements Converter<KeywordsFormulaElement> {
    private static volatile MySqlKeywordsFormulaElementConverter instance;

    protected MySqlKeywordsFormulaElementConverter() {
    }

    public static MySqlKeywordsFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlKeywordsFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlKeywordsFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       KeywordsFormulaElement obj,
                                       MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" ").append(obj.getOperator().getSymbol()).append(" ");
        sqlBuilder.append(obj.getKeywords()).append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
