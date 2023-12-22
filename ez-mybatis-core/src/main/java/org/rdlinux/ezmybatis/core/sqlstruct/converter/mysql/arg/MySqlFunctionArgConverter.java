package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.FunctionArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlFunctionArgConverter extends AbstractConverter<FunctionArg> implements Converter<FunctionArg> {
    private static volatile MySqlFunctionArgConverter instance;

    protected MySqlFunctionArgConverter() {
    }

    public static MySqlFunctionArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFunctionArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlFunctionArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FunctionArg obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration,
                obj.getFunction().getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getFunction(), mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
