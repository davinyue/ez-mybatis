package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

/**
 * SQL 表结构抽象。
 */
public interface Table extends SqlStruct {
    /**
     * 返回表分区信息。
     *
     * @return 分区信息；无分区时返回 {@code null}
     */
    default Partition getPartition() {
        return null;
    }

    /**
     * 返回表别名。
     *
     * @return 表别名
     */
    String getAlias();

    /**
     * 返回当前配置下实际参与 SQL 的表名。
     *
     * @param configuration MyBatis 配置
     * @return 表名或派生表别名
     */
    String getTableName(Configuration configuration);

    /**
     * 获取数据库模式。
     *
     * @param configuration MyBatis 配置
     * @return schema 名称；未指定时返回 {@code null}
     */
    String getSchema(Configuration configuration);

    /**
     * 根据列名创建列表达式对象。
     *
     * @param column 列名
     * @return 列对象
     */
    default TableColumn column(String column) {
        return TableColumn.of(this, column);
    }
}
