package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

@Getter
public abstract class AbstractTable implements Table {
    /**
     * 别名
     */
    protected String alias;
    /**
     * 分区
     */
    protected Partition partition;
    /**
     * 数据库模式
     */
    protected String schema;

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
    public String toSqlStruct(Converter.Type type, Configuration configuration, MybatisParamHolder paramHolder) {
        StringBuilder sqlBuilder = new StringBuilder(" ");
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String dbName = this.getSchema(configuration);
        if (dbName != null && !dbName.isEmpty()) {
            sqlBuilder.append(keywordQM).append(dbName).append(keywordQM).append(".");
        }
        sqlBuilder.append(keywordQM).append(this.getTableName(configuration)).append(keywordQM);
        if (this.partition != null) {
            sqlBuilder.append(this.partition.toSqlStruct(configuration));
        }
        if (type == Converter.Type.SELECT || type == Converter.Type.UPDATE || type == Converter.Type.DELETE) {
            sqlBuilder.append(" ").append(this.alias).append(" ");
        }
        return sqlBuilder.toString();
    }

    @Override
    public String getSchema(Configuration configuration) {
        return this.schema;
    }
}
