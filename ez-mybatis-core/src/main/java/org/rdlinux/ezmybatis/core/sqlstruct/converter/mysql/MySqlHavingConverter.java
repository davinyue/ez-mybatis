package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Having having,
                                       MybatisParamHolder mybatisParamHolder) {
        if (type != Type.SELECT) {
            throw new UnsupportedOperationException(String.format("%s model unsupported", type.name()));
        }
        if (having == null) {
            return sqlBuilder;
        }
        if (having.getConditions() == null || having.getConditions().isEmpty()) {
            return sqlBuilder.append(" HAVING 1 = 1 ");
        }
        String sonSql = MySqlWhereConverter.conditionsToSql(type, new StringBuilder(), configuration,
                mybatisParamHolder, having.getConditions()).toString();
        if (StringUtils.isBlank(sonSql)) {
            sonSql = " 1 = 1 ";
        }
        sqlBuilder.append(" HAVING ").append(sonSql);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
