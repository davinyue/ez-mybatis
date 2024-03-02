package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class OracleLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile OracleLimitConverter instance;

    protected OracleLimitConverter() {
    }

    public static OracleLimitConverter getInstance() {
        if (instance == null) {
            synchronized (OracleLimitConverter.class) {
                if (instance == null) {
                    instance = new OracleLimitConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Limit limit,
                                       MybatisParamHolder mybatisParamHolder) {
        if (limit == null) {
            return sqlBuilder;
        }
        return sqlBuilder.append(" AND ROWNUM <= ").append(limit.getSize()).append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
