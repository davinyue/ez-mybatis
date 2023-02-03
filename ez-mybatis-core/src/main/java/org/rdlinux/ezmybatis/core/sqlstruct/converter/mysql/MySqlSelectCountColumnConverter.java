package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectCountColumn;

public class MySqlSelectCountColumnConverter extends AbstractConverter<SelectCountColumn> implements Converter<SelectCountColumn> {
    private static volatile MySqlSelectCountColumnConverter instance;

    protected MySqlSelectCountColumnConverter() {
    }

    public static MySqlSelectCountColumnConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectCountColumnConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectCountColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectCountColumn ojb, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String distinctStr = "";
        if (ojb.isDistinct()) {
            distinctStr = " DISTINCT ";
        }
        String sql = " COUNT(" + distinctStr + ojb.getTable().getAlias() + "." + keywordQM + ojb.getColumn() + keywordQM
                + ") ";
        String alias = ojb.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sqlBuilder.append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
