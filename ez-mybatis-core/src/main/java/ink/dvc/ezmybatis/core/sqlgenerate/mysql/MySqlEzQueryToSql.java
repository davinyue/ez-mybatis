package ink.dvc.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.sqlgenerate.AbstractEzQueryToSql;
import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.Alias;
import ink.dvc.ezmybatis.core.sqlstruct.Limit;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;

import java.util.Map;

public class MySqlEzQueryToSql extends AbstractEzQueryToSql {
    private static volatile MySqlEzQueryToSql instance;

    private MySqlEzQueryToSql() {
    }

    public static MySqlEzQueryToSql getInstance() {
        if (instance == null) {
            synchronized (MySqlEzQueryToSql.class) {
                if (instance == null) {
                    instance = new MySqlEzQueryToSql();
                }
            }
        }
        return instance;
    }

    @Override
    public String toCountSql(Configuration configuration, EzQuery<?> query, Map<String, Object> mybatisParam) {
        String sql = super.toCountSql(configuration, query, mybatisParam);
        if (query.getGroupBy() != null && !query.getGroupBy().getItems().isEmpty()) {
            return "SELECT COUNT(1) FROM ( " + sql + " ) " + Alias.getAlias();
        } else {
            return sql;
        }
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> query,
                                       MybatisParamHolder mybatisParamHolder) {
        Limit limit = query.getLimit();
        if (limit == null) {
            return sqlBuilder;
        }
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        sqlBuilder.append(" LIMIT ").append(limit.getSkip()).append(", ").append(limit.getSize());
//        EzFrom from = query.getFrom();
//        EzTable table = from.getTable();
//        String sql = sqlBuilder.toString();
//        int fromIndex = sql.indexOf(" FROM");
//        String fromSql = sql.substring(fromIndex);
//        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, table.getEtType());
//        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
//        String selectSql = "SELECT " + table.getAlias() + "." + keywordQM + idColumn + keywordQM;
//        String sonSql = selectSql + fromSql;
//        String sonAlias = Alias.getAlias();
//        String masterAlias = Alias.getAlias();
//        sqlBuilder = new StringBuilder("SELECT ").append(masterAlias).append(".* FROM ")
//                .append(entityClassInfo.getTableName()).append(" ").append(masterAlias)
//                .append(" INNER JOIN (").append(sonSql).append(") ").append(sonAlias).append(" ON ")
//                .append(sonAlias).append(".").append(keywordQM).append(idColumn).append(keywordQM).append(" = ")
//                .append(masterAlias).append(".").append(keywordQM).append(idColumn).append(keywordQM);
        return sqlBuilder;
    }
}
