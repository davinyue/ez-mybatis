package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

import java.util.List;

public class MySqlFunctionConverter extends AbstractConverter<Function> implements Converter<Function> {
    private static volatile MySqlFunctionConverter instance;

    protected MySqlFunctionConverter() {
    }

    public static MySqlFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFunctionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFunctionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Function ojb,
                                       MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" ").append(ojb.getFunName()).append("(");
        List<Function.FunArg> funArgs = ojb.getFunArgs();
        if (funArgs != null && !funArgs.isEmpty()) {
            for (int i = 0; i < funArgs.size(); i++) {
                Function.FunArg arg = funArgs.get(i);
                if (arg.isDistinct()) {
                    sqlBuilder.append(" DISTINCT ");
                }
                Converter<? extends Operand> converter = EzMybatisContent.getConverter(configuration,
                        arg.getArgValue().getClass());
                converter.buildSql(type, sqlBuilder, configuration, arg.getArgValue(), mybatisParamHolder);
                if (i + 1 < funArgs.size()) {
                    sqlBuilder.append(", ");
                }
            }
        }
        sqlBuilder.append(") ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
