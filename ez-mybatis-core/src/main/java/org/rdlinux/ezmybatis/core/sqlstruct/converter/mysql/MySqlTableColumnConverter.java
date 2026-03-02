package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

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
    protected void doBuildSql(Type type, TableColumn obj, SqlGenerateContext sqlGenerateContext) {
        String keywordQM = EzMybatisContent.getKeywordQM(sqlGenerateContext.getConfiguration());
        sqlGenerateContext.getSqlBuilder().append(obj.getTable().getAlias()).append(".").append(keywordQM)
                .append(SqlEscaping.nameEscaping(obj.getColumn()))
                .append(keywordQM);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
