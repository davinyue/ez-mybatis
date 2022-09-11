package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

public class SqlTable implements Table {
    private String alias;
    private String sql;

    private SqlTable(String sql) {
        Assert.notEmpty(sql, "sql can not be null");
        this.sql = sql;
        this.alias = Alias.getAlias();
    }

    public static SqlTable of(String sql) {
        return new SqlTable(sql);
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getTableName(Configuration configuration) {
        return this.alias;
    }

    @Override
    public String getSchema(Configuration configuration) {
        return null;
    }

    @Override
    public String toSqlStruct(Converter.Type type, Configuration configuration, MybatisParamHolder paramHolder) {
        if (type == Converter.Type.SELECT || type == Converter.Type.UPDATE) {
            return " (" + this.sql + ") " + this.alias + " ";
        } else {
            return " (" + this.sql + ") ";
        }
    }
}
