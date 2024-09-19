package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;

public class MySqlEzQueryTableConverter extends AbstractConverter<EzQueryTable> implements Converter<EzQueryTable> {
    private static volatile MySqlEzQueryTableConverter instance;

    protected MySqlEzQueryTableConverter() {
    }

    public static MySqlEzQueryTableConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlEzQueryTableConverter.class) {
                if (instance == null) {
                    instance = new MySqlEzQueryTableConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       EzQueryTable table, MybatisParamHolder mybatisParamHolder) {
        if (type != Type.SELECT) {
            throw new IllegalArgumentException("EzQueryTable only supports query");
        }
        String querySql = SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getQuerySql(configuration, mybatisParamHolder, table.getEzQuery());
        return sqlBuilder.append(" (").append(querySql).append(") ").append(table.getAlias()).append(" ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
