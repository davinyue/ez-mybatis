package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlFunctionConverter;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

import java.util.List;

public class OracleFunctionConverter extends MySqlFunctionConverter implements Converter<Function> {
    private static volatile OracleFunctionConverter instance;

    protected OracleFunctionConverter() {
    }

    public static OracleFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (OracleFunctionConverter.class) {
                if (instance == null) {
                    instance = new OracleFunctionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Function obj, SqlGenerateContext sqlGenerateContext) {
        List<Function.FunArg> funArgs = obj.getFunArgs();
        if ("CONCAT".equalsIgnoreCase(obj.getFunName()) && funArgs != null && funArgs.size() > 2) {
            StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
            sqlBuilder.append(" ");
            for (int i = 0; i < funArgs.size(); i++) {
                Function.FunArg arg = funArgs.get(i);
                if (i > 0) {
                    sqlBuilder.append(" || ");
                }
                Converter<? extends Operand> converter = EzMybatisContent.getConverter(
                        sqlGenerateContext.getConfiguration(), arg.getArgValue().getClass());
                converter.buildSql(type, arg.getArgValue(), sqlGenerateContext);
            }
            sqlBuilder.append(" ");
            return;
        }
        super.doBuildSql(type, obj, sqlGenerateContext);
    }
}
