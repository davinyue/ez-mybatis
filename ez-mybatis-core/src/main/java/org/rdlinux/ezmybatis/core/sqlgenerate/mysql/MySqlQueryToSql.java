package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractQueryToSql;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlpart.EzLimit;

public class MySqlQueryToSql extends AbstractQueryToSql {
    private static volatile MySqlQueryToSql instance;

    private MySqlQueryToSql() {
    }

    public static MySqlQueryToSql getInstance() {
        if (instance == null) {
            synchronized ( MySqlQueryToSql.class ) {
                if (instance == null) {
                    instance = new MySqlQueryToSql();
                }
            }
        }
        return instance;
    }

    @Override
    public String getKeywordQM() {
        return "`";
    }

    @Override
    protected StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                       MybatisParamHolder mybatisParamHolder) {
        EzLimit limit = query.getLimit();
        if (limit == null) {
            return sqlBuilder;
        }
        String keywordQM = this.getKeywordQM();
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
