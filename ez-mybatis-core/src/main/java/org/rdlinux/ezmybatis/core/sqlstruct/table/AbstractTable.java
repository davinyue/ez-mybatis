package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

@Getter
public abstract class AbstractTable implements Table {
    protected String alias;
    protected Partition partition;

    public AbstractTable(String alias) {
        this.alias = alias;
    }

    public AbstractTable(String alias, Partition partition) {
        this.alias = alias;
        this.partition = partition;
    }

    @Override
    public Partition getPartition() {
        return this.partition;
    }

    @Override
    public String toSqlStruct(Configuration configuration, MybatisParamHolder paramHolder) {
        StringBuilder sqlBuilder = new StringBuilder();
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        sqlBuilder.append(" ").append(keywordQM).append(this.getTableName(configuration)).append(keywordQM);
        if (this.partition != null) {
            sqlBuilder.append(this.partition.toSqlStruct(configuration));
        }
        sqlBuilder.append(" ").append(this.alias).append(" ");
        return sqlBuilder.toString();
    }
}
