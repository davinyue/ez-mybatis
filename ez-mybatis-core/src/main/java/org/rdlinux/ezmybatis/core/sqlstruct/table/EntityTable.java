package org.rdlinux.ezmybatis.core.sqlstruct.table;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

/**
 * 实体映射表结构。
 *
 * <p>可根据实体类元数据动态解析 schema 与表名，也支持显式覆盖。</p>
 */
public class EntityTable extends DbTable {
    /**
     * 实体类型。
     */
    @Getter
    private final Class<?> etType;
    /**
     * 显式指定的表名，未指定时会回退到实体元数据。
     */
    private final String tableName;

    /**
     * 仅使用实体类型初始化实体表对象。
     *
     * @param etType 实体类型
     */
    private EntityTable(Class<?> etType) {
        this(null, etType, null);
    }

    /**
     * 使用实体类型和分区初始化实体表对象。
     *
     * @param etType    实体类型
     * @param partition 分区信息
     */
    private EntityTable(Class<?> etType, Partition partition) {
        this(null, etType, partition);
    }

    /**
     * 使用 schema 和实体类型初始化实体表对象。
     *
     * @param schema 数据库模式
     * @param etType 实体类型
     */
    private EntityTable(String schema, Class<?> etType) {
        this(schema, null, etType, null);
    }

    /**
     * 使用 schema、表名和实体类型初始化实体表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @param etType    实体类型
     */
    private EntityTable(String schema, String tableName, Class<?> etType) {
        this(schema, tableName, etType, null);
    }

    /**
     * 使用 schema、实体类型和分区初始化实体表对象。
     *
     * @param schema    数据库模式
     * @param etType    实体类型
     * @param partition 分区信息
     */
    private EntityTable(String schema, Class<?> etType, Partition partition) {
        this(schema, null, etType, partition);
    }

    /**
     * 使用 schema、表名、实体类型和分区初始化实体表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @param etType    实体类型
     * @param partition 分区信息
     */
    private EntityTable(String schema, String tableName, Class<?> etType, Partition partition) {
        super(schema, tableName, partition);
        this.etType = etType;
        this.schema = schema;
        this.tableName = tableName;
    }

    /**
     * 根据实体类型创建表对象。
     *
     * @param etType 实体类型
     * @return 实体表对象
     */
    public static EntityTable of(Class<?> etType) {
        return new EntityTable(etType);
    }

    /**
     * 根据实体类型和显式表名创建表对象。
     *
     * @param etType    实体类型
     * @param tableName 表名
     * @return 实体表对象
     */
    public static EntityTable of(Class<?> etType, String tableName) {
        return new EntityTable(null, tableName, etType);
    }

    /**
     * 根据实体类型和分区创建表对象。
     *
     * @param etType    实体类型
     * @param partition 分区信息
     * @return 实体表对象
     */
    public static EntityTable of(Class<?> etType, Partition partition) {
        return new EntityTable(etType, partition);
    }

    /**
     * 根据实体类型、表名和分区创建表对象。
     *
     * @param etType    实体类型
     * @param tableName 表名
     * @param partition 分区信息
     * @return 实体表对象
     */
    public static EntityTable of(Class<?> etType, String tableName, Partition partition) {
        return new EntityTable(null, tableName, etType, partition);
    }

    /**
     * 根据 schema 和实体类型创建表对象。
     *
     * @param schema 数据库模式
     * @param etType 实体类型
     * @return 实体表对象
     */
    public static EntityTable of(String schema, Class<?> etType) {
        return new EntityTable(schema, etType);
    }

    /**
     * 根据 schema、表名和实体类型创建表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @param etType    实体类型
     * @return 实体表对象
     */
    public static EntityTable of(String schema, String tableName, Class<?> etType) {
        return new EntityTable(schema, tableName, etType);
    }

    /**
     * 根据 schema、实体类型和分区创建表对象。
     *
     * @param schema    数据库模式
     * @param etType    实体类型
     * @param partition 分区信息
     * @return 实体表对象
     */
    public static EntityTable of(String schema, Class<?> etType, Partition partition) {
        return new EntityTable(schema, etType, partition);
    }

    /**
     * 根据 schema、表名、实体类型和分区创建表对象。
     *
     * @param schema    数据库模式
     * @param tableName 表名
     * @param etType    实体类型
     * @param partition 分区信息
     * @return 实体表对象
     */
    public static EntityTable of(String schema, String tableName, Class<?> etType, Partition partition) {
        return new EntityTable(schema, tableName, etType, partition);
    }

    /**
     * 返回实体表名。
     *
     * @param configuration MyBatis 配置
     * @return 表名
     */
    @Override
    public String getTableName(Configuration configuration) {
        if (this.tableName != null && !this.tableName.isEmpty()) {
            return this.tableName;
        }
        return EzEntityClassInfoFactory.forClass(configuration, this.etType).getTableName();
    }

    /**
     * 返回实体所属 schema。
     *
     * @param configuration MyBatis 配置
     * @return schema 名称
     */
    @Override
    public String getSchema(Configuration configuration) {
        String schema = super.getSchema(configuration);
        if (schema != null && !schema.isEmpty()) {
            return schema;
        }
        return EzEntityClassInfoFactory.forClass(configuration, this.etType).getSchema();
    }

    /**
     * 根据实体属性名创建字段对象。
     *
     * @param field 实体属性名
     * @return 实体字段对象
     */
    public EntityField field(String field) {
        return EntityField.of(this, field);
    }
}
