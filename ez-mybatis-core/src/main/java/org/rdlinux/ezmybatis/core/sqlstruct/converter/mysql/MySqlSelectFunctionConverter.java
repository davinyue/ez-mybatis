package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectFunction;

public class MySqlSelectFunctionConverter extends AbstractConverter<SelectFunction> implements Converter<SelectFunction> {
    private static volatile MySqlSelectFunctionConverter instance;

    protected MySqlSelectFunctionConverter() {
    }

    public static MySqlSelectFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectFunctionConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectFunctionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectFunction obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration, obj.getFunction()
                .getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getFunction(), mybatisParamHolder);
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
