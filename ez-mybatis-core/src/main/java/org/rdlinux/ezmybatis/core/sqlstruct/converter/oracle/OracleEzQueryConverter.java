package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlEzQueryConverter;

@SuppressWarnings("rawtypes")
public class OracleEzQueryConverter extends MySqlEzQueryConverter {
    private static volatile OracleEzQueryConverter instance;

    protected OracleEzQueryConverter() {
    }

    public static OracleEzQueryConverter getInstance() {
        if (instance == null) {
            synchronized (OracleEzQueryConverter.class) {
                if (instance == null) {
                    instance = new OracleEzQueryConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       EzQuery obj, MybatisParamHolder mybatisParamHolder) {
        String sql = this.ezQueryToSql(configuration, obj, mybatisParamHolder);
        return sqlBuilder.append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
