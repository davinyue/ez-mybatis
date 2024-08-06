package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractEzUpdateToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OracleEzUpdateToSql extends AbstractEzUpdateToSql {
    private static volatile OracleEzUpdateToSql instance;

    protected OracleEzUpdateToSql() {
    }

    public static OracleEzUpdateToSql getInstance() {
        if (instance == null) {
            synchronized (OracleEzUpdateToSql.class) {
                if (instance == null) {
                    instance = new OracleEzUpdateToSql();
                }
            }
        }
        return instance;
    }

    @Override
    public String toSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Collection<EzUpdate> updates) {
        String sql = super.toSql(configuration, mybatisParamHolder, updates);
        return "BEGIN \n" + sql + "END;";
    }

    @Override
    protected StringBuilder setToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                     MybatisParamHolder mybatisParamHolder) {
        UpdateSet set = update.getSet();
        if (set != null && set.getItems() != null) {
            List<UpdateItem> items = set.getItems().stream().filter(e -> e.getTable() == update.getTable())
                    .collect(Collectors.toList());
            Assert.notEmpty(items, "Valid update items cannot be empty");
            set.getItems().clear();
            set.getItems().addAll(items);
        }
        return super.setToSql(sqlBuilder, configuration, update, mybatisParamHolder);
    }

    @Override
    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, EzUpdate update,
                                       MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder;
    }
}
