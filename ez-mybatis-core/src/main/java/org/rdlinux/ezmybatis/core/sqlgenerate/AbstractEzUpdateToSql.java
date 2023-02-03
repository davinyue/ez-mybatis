package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Update;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;
import java.util.List;

public abstract class AbstractEzUpdateToSql implements EzUpdateToSql {
    @Override
    public String toSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update) {
        Assert.notNull(update, "update can not be null");
        return this.toSql(configuration, update, mybatisParamHolder);
    }

    @Override
    public String toSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                        Collection<EzUpdate> updates) {
        Assert.notEmpty(updates, "updates can not be empty");
        StringBuilder sql = new StringBuilder();
        for (EzUpdate update : updates) {
            sql.append(this.toSql(configuration, update, mybatisParamHolder)).append(";\n");
        }
        return sql.toString();
    }

    protected String toSql(Configuration configuration, EzUpdate update, MybatisParamHolder mybatisParamHolder) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.updateToSql(sqlBuilder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, update, mybatisParamHolder);
        sqlBuilder = this.setToSql(sqlBuilder, configuration, update, mybatisParamHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, update, mybatisParamHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, update, mybatisParamHolder);
        return sqlBuilder.toString();
    }

    protected StringBuilder updateToSql(StringBuilder sqlBuilder) {
        sqlBuilder.append("UPDATE ");
        return sqlBuilder;
    }

    protected StringBuilder setToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                     MybatisParamHolder mybatisParamHolder) {
        Update set = update.getSet();
        if (set == null || set.getItems() == null || set.getItems().isEmpty()) {
            throw new IllegalArgumentException("update items can not be null");
        }
        List<UpdateItem> items = set.getItems();
        sqlBuilder.append(" ").append("SET ");
        for (int i = 0; i < items.size(); i++) {
            UpdateItem updateItem = items.get(i);
            Converter<? extends UpdateItem> converter = EzMybatisContent.getConverter(configuration,
                    updateItem.getClass());
            sqlBuilder = converter.toSqlPart(Converter.Type.UPDATE, sqlBuilder, configuration, updateItem,
                    mybatisParamHolder);
            if (i + 1 < items.size()) {
                sqlBuilder.append(", ");
            }
        }
        return sqlBuilder;
    }

    protected StringBuilder fromToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                      MybatisParamHolder mybatisParamHolder) {
        From from = update.getFrom();
        Converter<From> converter = EzMybatisContent.getConverter(configuration, From.class);
        return converter.toSqlPart(Converter.Type.UPDATE, sqlBuilder, configuration, from, mybatisParamHolder);
    }

    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                       MybatisParamHolder mybatisParamHolder) {
        if (update.getJoins() != null) {
            Converter<Join> converter = EzMybatisContent.getConverter(configuration, Join.class);
            for (Join join : update.getJoins()) {
                sqlBuilder = converter.toSqlPart(Converter.Type.UPDATE, sqlBuilder, configuration, join,
                        mybatisParamHolder);
            }
        }
        return sqlBuilder;
    }

    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                       MybatisParamHolder mybatisParamHolder) {
        Where where = update.getWhere();
        Converter<Where> converter = EzMybatisContent.getConverter(configuration, Where.class);
        return converter.toSqlPart(Converter.Type.UPDATE, sqlBuilder, configuration, where, mybatisParamHolder);
    }
}
