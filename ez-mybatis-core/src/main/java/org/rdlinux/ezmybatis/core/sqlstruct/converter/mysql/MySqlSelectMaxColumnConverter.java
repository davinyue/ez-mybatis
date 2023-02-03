package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectMaxColumn;

public class MySqlSelectMaxColumnConverter extends AbstractConverter<SelectMaxColumn> implements Converter<SelectMaxColumn> {
    private static volatile MySqlSelectMaxColumnConverter instance;

    protected MySqlSelectMaxColumnConverter() {
    }

    public static MySqlSelectMaxColumnConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectMaxColumnConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectMaxColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectMaxColumn ojb, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " MAX(" + ojb.getTable().getAlias() + "." + keywordQM + ojb.getColumn() + keywordQM + ") ";
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
