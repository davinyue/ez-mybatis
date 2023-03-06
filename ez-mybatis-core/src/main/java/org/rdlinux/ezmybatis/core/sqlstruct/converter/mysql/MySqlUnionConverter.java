package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Union;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlUnionConverter extends AbstractConverter<Union> implements Converter<Union> {
    private static volatile MySqlUnionConverter instance;

    protected MySqlUnionConverter() {
    }

    public static MySqlUnionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUnionConverter.class) {
                if (instance == null) {
                    instance = new MySqlUnionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       Union obj, MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" UNION ");
        if (obj.isAll()) {
            sqlBuilder.append(" ALL ");
        }
        sqlBuilder.append("\n");
        sqlBuilder.append(SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getQuerySql(configuration, mybatisParamHolder, obj.getQuery()));
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
