package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FunctionCompareValueCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlFunctionCompareValueConditionConverter extends AbstractConverter<FunctionCompareValueCondition>
        implements Converter<FunctionCompareValueCondition> {
    private static volatile MySqlFunctionCompareValueConditionConverter instance;

    protected MySqlFunctionCompareValueConditionConverter() {
    }

    public static MySqlFunctionCompareValueConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFunctionCompareValueConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFunctionCompareValueConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FunctionCompareValueCondition obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration,
                ((Function) obj.getFunction()).getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getFunction(), mybatisParamHolder);
        sqlBuilder.append(" ").append(obj.getOperator().getOperator()).append(" ");
        return sqlBuilder.append(mybatisParamHolder.getMybatisParamName(obj.getValue())).append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
