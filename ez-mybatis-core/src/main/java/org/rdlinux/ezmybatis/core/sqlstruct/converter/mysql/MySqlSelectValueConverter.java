package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectValue;

public class MySqlSelectValueConverter extends AbstractConverter<SelectValue> implements Converter<SelectValue> {
    private static volatile MySqlSelectValueConverter instance;

    protected MySqlSelectValueConverter() {
    }

    public static MySqlSelectValueConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectValueConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectValueConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectValue obj, MybatisParamHolder mybatisParamHolder) {
        String paramName = mybatisParamHolder.getMybatisParamName(obj.getValue());
        sqlBuilder.append(" ").append(paramName);
        String alias = obj.getAlias();
        if (StringUtils.isNotBlank(alias)) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(" AS ").append(keywordQM).append(alias).append(keywordQM).append(" ");
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
