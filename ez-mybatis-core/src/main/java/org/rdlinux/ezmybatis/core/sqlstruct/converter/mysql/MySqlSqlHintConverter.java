package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlHint;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlSqlHintConverter extends AbstractConverter<SqlHint> implements Converter<SqlHint> {
    private static volatile MySqlSqlHintConverter instance;

    protected MySqlSqlHintConverter() {
    }

    public static MySqlSqlHintConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSqlHintConverter.class) {
                if (instance == null) {
                    instance = new MySqlSqlHintConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, SqlHint obj, SqlGenerateContext sqlGenerateContext) {
        if (obj == null || StringUtils.isEmpty(obj.getHint())) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" /*+ ").append(obj.getHint()).append(" */ ");
    }

}
