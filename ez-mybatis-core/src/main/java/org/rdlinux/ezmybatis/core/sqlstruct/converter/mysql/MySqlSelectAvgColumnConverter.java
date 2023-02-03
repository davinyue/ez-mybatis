package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAvgColumn;

public class MySqlSelectAvgColumnConverter extends AbstractConverter<SelectAvgColumn> implements Converter<SelectAvgColumn> {
    private static volatile MySqlSelectAvgColumnConverter instance;

    protected MySqlSelectAvgColumnConverter() {
    }

    public static MySqlSelectAvgColumnConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectAvgColumnConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectAvgColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectAvgColumn ojb, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " AVG(" + ojb.getTable().getAlias() + "." + keywordQM + ojb.getColumn() + keywordQM + ") ";
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
