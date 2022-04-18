package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;

import java.util.Collection;

public abstract class AbstractEzDeleteToSql implements EzDeleteToSql {
    @Override
    public String toSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete) {
        return this.toSql(configuration, delete, paramHolder);
    }

    @Override
    public String toSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes) {
        StringBuilder sql = new StringBuilder();
        for (EzDelete delete : deletes) {
            sql.append(this.toSql(configuration, delete, paramHolder)).append(";\n");
        }
        return sql.toString();
    }

    protected String toSql(Configuration configuration, EzDelete delete, MybatisParamHolder mybatisParamHolder) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.deleteToSql(sqlBuilder, delete);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, delete, mybatisParamHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, delete, mybatisParamHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, delete, mybatisParamHolder);
        return sqlBuilder.toString();
    }

    protected StringBuilder deleteToSql(StringBuilder sqlBuilder, EzDelete delete) {
        sqlBuilder.append("DELETE ");
        return sqlBuilder;
    }

    protected StringBuilder fromToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                      MybatisParamHolder mybatisParamHolder) {
        From from = delete.getFrom();
        return from.toSqlPart(sqlBuilder, configuration, delete, mybatisParamHolder);
    }

    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        if (delete.getJoins() != null) {
            for (Join join : delete.getJoins()) {
                sqlBuilder = join.toSqlPart(sqlBuilder, configuration, delete, mybatisParamHolder);
            }
        }
        return sqlBuilder;
    }

    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzDelete delete,
                                       MybatisParamHolder mybatisParamHolder) {
        Where where = delete.getWhere();
        if (where == null || where.getConditions() == null) {
            return sqlBuilder;
        } else {
            return where.toSqlPart(sqlBuilder, configuration, delete, mybatisParamHolder);
        }
    }
}
