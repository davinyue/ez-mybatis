package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectSumColumn;

public class MySqlSelectSumColumnConverter extends AbstractConverter<SelectSumColumn> implements Converter<SelectSumColumn> {
    private static volatile MySqlSelectSumColumnConverter instance;

    protected MySqlSelectSumColumnConverter() {
    }

    public static MySqlSelectSumColumnConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectSumColumnConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectSumColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectSumColumn ojb, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " SUM(" + ojb.getTable().getAlias() + "." + keywordQM + ojb.getColumn() + keywordQM + ") ";
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
