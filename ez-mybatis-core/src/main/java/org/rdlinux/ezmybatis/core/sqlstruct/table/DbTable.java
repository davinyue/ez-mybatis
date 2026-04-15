package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;
import org.rdlinux.ezmybatis.utils.AliasGenerate;

/**
 * 物理数据库表结构。
 *
 * <p>适用于直接通过表名、schema 和分区信息描述真实数据库表。</p>
 */
@Getter
public class DbTable extends AbstractTable {

    /**
     * 仅使用表名初始化数据库表对象。
     *
     * @param tableName 表名
     */
    private DbTable(String tableName) {
        this(null, tableName);
    }

    /**
     * 使用 schema 和表名初始化数据库表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     */
    private DbTable(String schema, String tableName) {
        this(schema, tableName, null);
    }

    /**
     * 使用表名和分区初始化数据库表对象。
     *
     * @param tableName 表名
     * @param partition 分区信息
     */
    private DbTable(String tableName, Partition partition) {
        this(null, tableName, partition);
    }

    /**
     * 使用 schema、表名和分区初始化数据库表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @param partition 分区信息
     */
    protected DbTable(String schema, String tableName, Partition partition) {
        super(schema, tableName, AliasGenerate.getAlias(), partition);
    }


    /**
     * 通过表名创建数据库表对象。
     *
     * @param tableName 表名
     * @return 数据库表对象
     */
    public static DbTable of(String tableName) {
        return new DbTable(tableName);
    }

    /**
     * 通过表名和分区创建数据库表对象。
     *
     * @param tableName 表名
     * @param partition 分区信息
     * @return 数据库表对象
     */
    public static DbTable of(String tableName, Partition partition) {
        return new DbTable(tableName, partition);
    }

    /**
     * 通过 schema 和表名创建数据库表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @return 数据库表对象
     */
    public static DbTable of(String schema, String tableName) {
        return new DbTable(schema, tableName);
    }

    /**
     * 通过 schema、表名和分区创建数据库表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @param partition 分区信息
     * @return 数据库表对象
     */
    public static DbTable of(String schema, String tableName, Partition partition) {
        return new DbTable(schema, tableName, partition);
    }

    /**
     * 返回物理表名。
     *
     * @param configuration MyBatis 配置
     * @return 表名
     */
    @Override
    public String getTableName(Configuration configuration) {
        EzContentConfig contentConfig = EzMybatisContent.getContentConfig(configuration);
        EzMybatisConfig ezMybatisConfig = contentConfig.getEzMybatisConfig();
        if (ezMybatisConfig.getTableNameCasePolicy() == NameCasePolicy.UPPER_CASE) {
            return this.tableName.toUpperCase();
        } else if (ezMybatisConfig.getTableNameCasePolicy() == NameCasePolicy.LOWER_CASE) {
            return this.tableName.toLowerCase();
        } else {
            return this.tableName;
        }
    }
}
