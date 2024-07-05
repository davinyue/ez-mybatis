package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class DmLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile DmLimitConverter instance;

    protected DmLimitConverter() {
    }

    public static DmLimitConverter getInstance() {
        if (instance == null) {
            synchronized (DmLimitConverter.class) {
                if (instance == null) {
                    instance = new DmLimitConverter();
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
        return sqlBuilder.append(" FETCH FIRST ").append(limit.getSize()).append(" ROWS ONLY ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
