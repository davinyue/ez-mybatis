package ink.dvc.ezmybatis.core.sqlstruct.table;

import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

@Getter
public abstract class AbstractTable implements Table {
    protected String alias;
    protected String partition;

    public AbstractTable(String alias) {
        this.alias = alias;
    }

    public AbstractTable(String alias, String partition) {
        this.alias = alias;
        this.partition = partition;
    }

    @Override
    public String getPartition() {
        return this.partition;
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        StringBuilder sqlBuilder = new StringBuilder();
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        sqlBuilder.append(" ").append(keywordQM).append(this.getTableName(configuration)).append(keywordQM);
        if (this.partition != null && !this.partition.isEmpty()) {
            sqlBuilder.append(" ").append("PARTITION( ").append(this.partition).append(" ) ");
        }
        sqlBuilder.append(" ").append(this.alias).append(" ");
        return sqlBuilder.toString();
    }
}
