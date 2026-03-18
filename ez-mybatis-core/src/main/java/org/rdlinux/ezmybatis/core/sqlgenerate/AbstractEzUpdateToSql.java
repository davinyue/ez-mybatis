package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;
import java.util.List;

public abstract class AbstractEzUpdateToSql implements EzUpdateToSql {
    @Override
    public String toSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        Assert.notNull(update, "update can not be null");
        return this.doToSql(sqlGenerateContext, update);
    }

    @Override
    public String toSql(SqlGenerateContext sqlGenerateContext, Collection<EzUpdate> updates) {
        Assert.notEmpty(updates, "updates can not be empty");
        StringBuilder sql = new StringBuilder();
        for (EzUpdate update : updates) {
            sql.append(this.doToSql(SqlGenerateContext.copyOf(sqlGenerateContext), update)).append(";\n");
        }
        return sql.toString();
    }

    protected String doToSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        this.updateToSql(sqlGenerateContext);
        this.fromToSql(sqlGenerateContext, update);
        this.setToSql(sqlGenerateContext, update);
        this.joinsToSql(sqlGenerateContext, update);
        this.whereToSql(sqlGenerateContext, update);
        return sqlGenerateContext.getSqlBuilder().toString();
    }

    protected void updateToSql(SqlGenerateContext sqlGenerateContext) {
        sqlGenerateContext.getSqlBuilder().append("UPDATE ");
    }

    protected void setToSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        UpdateSet set = update.getSet();
        if (set == null || set.getItems() == null || set.getItems().isEmpty()) {
            throw new IllegalArgumentException("update items can not be null");
        }
        Configuration configuration = sqlGenerateContext.getConfiguration();
        List<UpdateItem> items = set.getItems();
        sqlGenerateContext.getSqlBuilder().append(" ").append("SET ");
        for (int i = 0; i < items.size(); i++) {
            UpdateItem updateItem = items.get(i);
            Converter<? extends UpdateItem> converter = EzMybatisContent.getConverter(configuration,
                    updateItem.getClass());
            converter.buildSql(Converter.Type.UPDATE, updateItem, sqlGenerateContext);
            if (i + 1 < items.size()) {
                sqlGenerateContext.getSqlBuilder().append(", ");
            }
        }
    }

    protected void fromToSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        From from = update.getFrom();
        Converter<From> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), From.class);
        converter.buildSql(Converter.Type.UPDATE, from, sqlGenerateContext);
    }

    protected void joinsToSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        if (update.getJoins() != null) {
            Converter<Join> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                    Join.class);
            for (Join join : update.getJoins()) {
                converter.buildSql(Converter.Type.UPDATE, join, sqlGenerateContext);
            }
        }
    }

    protected void whereToSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        Where where = update.getWhere();
        Converter<Where> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(), Where.class);
        converter.buildSql(Converter.Type.UPDATE, where, sqlGenerateContext);
    }
}
