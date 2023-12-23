package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlTableColumnConverter extends AbstractConverter<TableColumn> implements Converter<TableColumn> {
    private static volatile MySqlTableColumnConverter instance;

    protected MySqlTableColumnConverter() {
    }

    public static MySqlTableColumnConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlTableColumnConverter.class) {
                if (instance == null) {
                    instance = new MySqlTableColumnConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       TableColumn obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(obj.getColumn())
                .append(keywordQM);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
