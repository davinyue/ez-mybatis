package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       SqlHint obj, MybatisParamHolder mybatisParamHolder) {
        if (obj == null || StringUtils.isEmpty(obj.getHint())) {
            return sqlBuilder;
        }
        return sqlBuilder.append(" /*+ ").append(obj.getHint()).append(" */ ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
