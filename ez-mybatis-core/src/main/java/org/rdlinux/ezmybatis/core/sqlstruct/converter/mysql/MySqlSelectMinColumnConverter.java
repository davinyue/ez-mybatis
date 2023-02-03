package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectMinColumn;

public class MySqlSelectMinColumnConverter extends AbstractConverter<SelectMinColumn> implements Converter<SelectMinColumn> {
    private static volatile MySqlSelectMinColumnConverter instance;

    protected MySqlSelectMinColumnConverter() {
    }

    public static MySqlSelectMinColumnConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectMinColumnConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectMinColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectMinColumn ojb, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " MIN(" + ojb.getTable().getAlias() + "." + keywordQM + ojb.getColumn() + keywordQM + ") ";
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
