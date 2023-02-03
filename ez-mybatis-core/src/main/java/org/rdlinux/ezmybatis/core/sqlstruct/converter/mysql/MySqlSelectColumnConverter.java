package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectColumn;

public class MySqlSelectColumnConverter extends AbstractConverter<SelectColumn> implements Converter<SelectColumn> {
    private static volatile MySqlSelectColumnConverter instance;

    protected MySqlSelectColumnConverter() {
    }

    public static MySqlSelectColumnConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectColumnConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectColumn ojb, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " " + ojb.getTable().getAlias() + "." + keywordQM + ojb.getColumn() + keywordQM + " ";
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
