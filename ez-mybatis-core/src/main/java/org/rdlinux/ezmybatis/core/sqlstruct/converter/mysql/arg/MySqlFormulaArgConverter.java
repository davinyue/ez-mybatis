package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.FormulaArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;

public class MySqlFormulaArgConverter extends AbstractConverter<FormulaArg> implements Converter<FormulaArg> {
    private static volatile MySqlFormulaArgConverter instance;

    protected MySqlFormulaArgConverter() {
    }

    public static MySqlFormulaArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaArg obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration,
                obj.getFormula().getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getFormula(), mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
