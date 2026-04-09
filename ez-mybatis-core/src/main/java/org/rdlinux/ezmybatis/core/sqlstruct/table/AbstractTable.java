package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

/**
 * 表结构抽象基类。
 *
 * <p>统一封装别名、分区、schema 与表名等公共属性，供具体表实现复用。</p>
 */
@Getter
@Setter
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
    /**
     * 表名
     */
    protected String tableName;

    /**
     * 仅指定别名创建表结构。
     *
     * @param alias 表别名
     */
    public AbstractTable(String alias) {
        this.alias = alias;
    }

    /**
     * 指定别名和分区创建表结构。
     *
     * @param alias     表别名
     * @param partition 分区信息
     */
    public AbstractTable(String alias, Partition partition) {
        this.alias = alias;
        this.partition = partition;
    }

    /**
     * 指定表名、别名和分区创建表结构。
     *
     * @param tableName 表名
     * @param alias     表别名
     * @param partition 分区信息
     */
    public AbstractTable(String tableName, String alias, Partition partition) {
        this.tableName = tableName;
        this.alias = alias;
        this.partition = partition;
    }

    /**
     * 指定 schema、表名、别名和分区创建表结构。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @param alias     表别名
     * @param partition 分区信息
     */
    public AbstractTable(String schema, String tableName, String alias, Partition partition) {
        this.schema = schema;
        this.tableName = tableName;
        this.alias = alias;
        this.partition = partition;
    }

    /**
     * 返回表所属 schema。
     *
     * @param configuration MyBatis 配置
     * @return schema 名称
     */
    @Override
    public String getSchema(Configuration configuration) {
        return this.schema;
    }
}
