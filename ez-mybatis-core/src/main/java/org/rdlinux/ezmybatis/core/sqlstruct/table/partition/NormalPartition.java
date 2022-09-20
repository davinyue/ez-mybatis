package org.rdlinux.ezmybatis.core.sqlstruct.table.partition;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 一般分区
 */
@Getter
public class NormalPartition implements Partition {
    protected List<String> partitions;

    protected NormalPartition(Collection<String> partitions) {
        this.partitions = new LinkedList<>(partitions);
    }

    protected NormalPartition(String... partitions) {
        if (partitions != null && partitions.length > 0) {
            this.partitions = new LinkedList<>();
            this.partitions = Arrays.asList(partitions);
        }
    }

    public static NormalPartition of(Collection<String> partitions) {
        return new NormalPartition(partitions);
    }

    public static NormalPartition of(String... partitions) {
        return new NormalPartition(partitions);
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        DbType dbType = EzMybatisContent.getDbType(configuration);
        if (this.partitions == null || this.partitions.isEmpty()) {
            return "";
        } else if (dbType == DbType.ORACLE || dbType == DbType.DM) {
            return " PARTITION(" + this.partitions.get(0) + ") ";
        } else {
            StringBuilder sql = new StringBuilder(" PARTITION(");
            for (int i = 0; i < this.partitions.size(); i++) {
                String partition = this.partitions.get(i);
                sql.append(partition);
                if (i + 1 < this.partitions.size()) {
                    sql.append(", ");
                }
            }
            sql.append(") ");
            return sql.toString();
        }
    }
}
