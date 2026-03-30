package org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, TableColumn obj, SqlGenerateContext sqlGenerateContext) {
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(sqlGenerateContext.getConfiguration());
        if (type == Type.SELECT) {
            sqlGenerateContext.getSqlBuilder().append(obj.getTable().getAlias()).append(".");
        }
        sqlGenerateContext.getSqlBuilder().append(keywordQM).append(SqlEscaping.nameEscaping(obj.getColumn()))
                .append(keywordQM);
    }

}
