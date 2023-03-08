package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FunctionUpdateColumnItem;

public class MySqlFunctionUpdateColumnItemConverter extends AbstractConverter<FunctionUpdateColumnItem>
        implements Converter<FunctionUpdateColumnItem> {
    private static volatile MySqlFunctionUpdateColumnItemConverter instance;

    protected MySqlFunctionUpdateColumnItemConverter() {
    }

    public static MySqlFunctionUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFunctionUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlFunctionUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FunctionUpdateColumnItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        Function function = obj.getFunction();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(obj.getColumn())
                .append(keywordQM).append(" = ");
        Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration, function.getClass());
        converter.buildSql(type, sqlBuilder, configuration, function, mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
