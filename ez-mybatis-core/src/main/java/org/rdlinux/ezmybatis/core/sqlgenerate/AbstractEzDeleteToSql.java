package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

public abstract class AbstractEzDeleteToSql implements EzDeleteToSql {
    @Override
    public String toSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        Assert.notNull(delete, "delete can not be null");
        return this.doToSql(sqlGenerateContext, delete);
    }

    @Override
    public String toSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes) {
        StringBuilder sql = new StringBuilder();
        for (EzDelete delete : deletes) {
            sql.append(this.doToSql(SqlGenerateContext.copyOf(sqlGenerateContext), delete)).append(";\n");
        }
        return sql.toString();
    }

    protected String doToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        this.deleteToSql(sqlGenerateContext, delete);
        this.fromToSql(sqlGenerateContext, delete);
        this.joinsToSql(sqlGenerateContext, delete);
        this.whereToSql(sqlGenerateContext, delete);
        return sqlGenerateContext.getSqlBuilder().toString();
    }

    protected void deleteToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        sqlGenerateContext.getSqlBuilder().append("DELETE ");
    }

    protected void fromToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        From from = delete.getFrom();
        Converter<From> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), From.class);
        converter.buildSql(Converter.Type.DELETE, from, sqlGenerateContext);
    }

    protected void joinsToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        if (delete.getJoins() != null) {
            Converter<Join> converter = EzMybatisContent.getConverter(configuration, Join.class);
            for (Join join : delete.getJoins()) {
                converter.buildSql(Converter.Type.DELETE, join, sqlGenerateContext);
            }
        }
    }

    protected void whereToSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        Where where = delete.getWhere();
        Converter<Where> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), Where.class);
        converter.buildSql(Converter.Type.DELETE, where, sqlGenerateContext);
    }
}
