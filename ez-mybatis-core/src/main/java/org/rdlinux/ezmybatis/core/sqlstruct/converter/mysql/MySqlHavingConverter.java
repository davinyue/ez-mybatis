package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Having;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlHavingConverter extends AbstractConverter<Having> implements Converter<Having> {
    private static volatile MySqlHavingConverter instance;

    protected MySqlHavingConverter() {
    }

    public static MySqlHavingConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlHavingConverter.class) {
                if (instance == null) {
                    instance = new MySqlHavingConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Having having, SqlGenerateContext sqlGenerateContext) {
        if (type != Type.SELECT) {
            throw new UnsupportedOperationException(String.format("%s model unsupported", type.name()));
        }
        if (having == null) {
            return;
        }
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        if (having.getConditions() == null || having.getConditions().isEmpty()) {
            sqlBuilder.append(" HAVING 1 = 1 ");
            return;
        }
        String sonSql = MySqlWhereConverter.conditionsToSql(type, sqlGenerateContext, having.getConditions())
                .toString();
        if (StringUtils.isBlank(sonSql)) {
            sonSql = " 1 = 1 ";
        }
        sqlBuilder.append(" HAVING ").append(sonSql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
