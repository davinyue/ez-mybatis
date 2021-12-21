package ink.dvc.ezmybatis.core.sqlgenerate.oracle;

import ink.dvc.ezmybatis.core.sqlgenerate.AbstractEzUpdateToSql;
import ink.dvc.ezmybatis.core.sqlstruct.UpdateSet;
import ink.dvc.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzUpdate;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.utils.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OracleEzUpdateToSql extends AbstractEzUpdateToSql {
    private static volatile OracleEzUpdateToSql instance;

    private OracleEzUpdateToSql() {
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
    public String toSql(Configuration configuration, List<EzUpdate> updates, Map<String, Object> mybatisParam) {
        String sql = super.toSql(configuration, updates, mybatisParam);
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
