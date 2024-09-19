package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class SqlServerTableColumnConverter extends AbstractConverter<TableColumn> implements Converter<TableColumn> {
    private static volatile SqlServerTableColumnConverter instance;

    protected SqlServerTableColumnConverter() {
    }

    public static SqlServerTableColumnConverter getInstance() {
        if (instance == null) {
            synchronized (SqlServerTableColumnConverter.class) {
                if (instance == null) {
                    instance = new SqlServerTableColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       TableColumn obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        if (type == Type.SELECT) {
            sqlBuilder.append(obj.getTable().getAlias()).append(".");
        }
        return sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(obj.getColumn()))
                .append(keywordQM);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.SQL_SERVER;
    }
}
