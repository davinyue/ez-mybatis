package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectFormula;

public class MySqlSelectFormulaConverter extends AbstractConverter<SelectFormula> implements Converter<SelectFormula> {
    private static volatile MySqlSelectFormulaConverter instance;

    protected MySqlSelectFormulaConverter() {
    }

    public static MySqlSelectFormulaConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectFormulaConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectFormulaConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectFormula obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration, obj.getFormula()
                .getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getFormula(), mybatisParamHolder);
        String alias = obj.getAlias();
        if (StringUtils.isNotBlank(alias)) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(" ").append(keywordQM).append(alias).append(keywordQM).append(" ");
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
