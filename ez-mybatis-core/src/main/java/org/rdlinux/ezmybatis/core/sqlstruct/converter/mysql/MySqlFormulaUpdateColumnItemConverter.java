package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FormulaUpdateColumnItem;

public class MySqlFormulaUpdateColumnItemConverter extends AbstractConverter<FormulaUpdateColumnItem>
        implements Converter<FormulaUpdateColumnItem> {
    private static volatile MySqlFormulaUpdateColumnItemConverter instance;

    protected MySqlFormulaUpdateColumnItemConverter() {
    }

    public static MySqlFormulaUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaUpdateColumnItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        Formula formula = obj.getFormula();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(obj.getColumn())
                .append(keywordQM).append(" = ");
        Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration, formula.getClass());
        converter.buildSql(type, sqlBuilder, configuration, formula, mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
