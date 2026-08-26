package org.rdlinux;

import org.apache.ibatis.session.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.PhysicalTableRoute;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql.SqlServerNormalPartitionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql.SqlServerSubPartitionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre.PostgreSqlNormalPartitionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre.PostgreSqlSubPartitionConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态物理表路由方言转换测试。
 */
public class DynamicTableDialectTest {
    /**
     * 清理测试期间注册的 MyBatis 配置。
     */
    @After
    public void tearDown() {
        EzMybatisContent.destroyAll();
    }

    /**
     * 验证所有已支持方言都使用动态 schema 和 tableName。
     */
    @Test
    public void shouldRenderDynamicSchemaAndTableForAllDialects() {
        assertDynamicTable(DbType.MYSQL, "`tenant_schema`.`user_2026`");
        assertDynamicTable(DbType.POSTGRE_SQL, "\"tenant_schema\".\"user_2026\"");
        assertDynamicTable(DbType.SQL_SERVER, "\"tenant_schema\".\"user_2026\"");
        assertDynamicTable(DbType.ORACLE, "\"tenant_schema\".\"user_2026\"");
        assertDynamicTable(DbType.DM, "\"tenant_schema\".\"user_2026\"");
    }

    /**
     * 验证 MySQL 方言使用动态普通分区。
     */
    @Test
    public void shouldRenderDynamicMySqlPartitions() {
        assertDynamicTable(DbType.MYSQL, NormalPartition.of("p_2026", "p_2027"),
                "`tenant_schema`.`user_2026` PARTITION (p_2026, p_2027) ");
    }

    /**
     * 验证 Oracle 和达梦方言使用动态普通分区。
     */
    @Test
    public void shouldRenderDynamicOracleAndDmPartitions() {
        assertDynamicTable(DbType.ORACLE, NormalPartition.of("p_2026"),
                "\"tenant_schema\".\"user_2026\" PARTITION(p_2026) ");
        assertDynamicTable(DbType.DM, NormalPartition.of("p_2026"),
                "\"tenant_schema\".\"user_2026\" PARTITION(p_2026) ");
    }

    /**
     * 验证 PostgreSQL 和 SQL Server 注册了独立的分区转换器。
     */
    @Test
    public void shouldRegisterDedicatedPartitionConverters() {
        assertDedicatedPartitionConverter(DbType.POSTGRE_SQL,
                PostgreSqlNormalPartitionConverter.class, PostgreSqlSubPartitionConverter.class);
        assertDedicatedPartitionConverter(DbType.SQL_SERVER,
                SqlServerNormalPartitionConverter.class, SqlServerSubPartitionConverter.class);
    }

    /**
     * 验证 PostgreSQL 和 SQL Server 不生成错误的表引用分区语法。
     */
    @Test
    public void shouldRejectUnsupportedPartitionTableReference() {
        assertUnsupportedPartition(DbType.POSTGRE_SQL, NormalPartition.of("p_2026"), "PostgreSQL");
        assertUnsupportedPartition(DbType.POSTGRE_SQL, SubPartition.of("sp_2026"), "PostgreSQL");
        assertUnsupportedPartition(DbType.SQL_SERVER, NormalPartition.of("p_2026"), "SQL Server");
        assertUnsupportedPartition(DbType.SQL_SERVER, SubPartition.of("sp_2026"), "SQL Server");
    }

    /**
     * 验证指定方言的动态表名渲染结果。
     *
     * @param dbType       数据库类型
     * @param expectedSql 期望 SQL
     */
    private void assertDynamicTable(DbType dbType, String expectedSql) {
        assertDynamicTable(dbType, null, expectedSql);
    }

    /**
     * 验证指定方言的动态表名和分区渲染结果。
     *
     * @param dbType       数据库类型
     * @param partition    动态分区
     * @param expectedSql 期望 SQL
     */
    private void assertDynamicTable(DbType dbType, NormalPartition partition, String expectedSql) {
        Configuration configuration = new Configuration();
        EzMybatisConfig config = new EzMybatisConfig(configuration);
        config.setDbType(dbType);
        EzMybatisContent.init(config);
        EzMybatisContent.setDynamicTableResolver(config,
                context -> PhysicalTableRoute.of("tenant_schema", "user_2026", partition));

        Map<String, Object> params = new HashMap<>();
        params.put(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION, configuration);
        SqlGenerateContext context = SqlGenerateContext.from(new MybatisParamHolder(configuration, params));
        EzMybatisContent.getConverter(configuration, DbTable.class).buildSql(
                Converter.Type.INSERT, DbTable.of("user_base"), context);

        Assert.assertEquals(expectedSql, context.getSqlBuilder().toString());
    }

    /**
     * 验证指定方言注册的分区转换器类型。
     *
     * @param dbType              数据库类型
     * @param expectedNormalConverter 期望普通分区转换器类型
     * @param expectedSubConverter    期望子分区转换器类型
     */
    private void assertDedicatedPartitionConverter(DbType dbType,
            Class<?> expectedNormalConverter, Class<?> expectedSubConverter) {
        Configuration configuration = new Configuration();
        EzMybatisConfig config = new EzMybatisConfig(configuration);
        config.setDbType(dbType);
        EzMybatisContent.init(config);

        Assert.assertEquals(expectedNormalConverter,
                EzMybatisContent.getConverter(configuration, NormalPartition.class).getClass());
        Assert.assertEquals(expectedSubConverter,
                EzMybatisContent.getConverter(configuration, SubPartition.class).getClass());
        EzMybatisContent.destroy(configuration);
    }

    /**
     * 验证指定方言的分区表引用会快速失败。
     *
     * @param dbType            数据库类型
     * @param partition         分区信息
     * @param databaseName      数据库名称
     */
    private void assertUnsupportedPartition(DbType dbType, NormalPartition partition, String databaseName) {
        Configuration configuration = new Configuration();
        EzMybatisConfig config = new EzMybatisConfig(configuration);
        config.setDbType(dbType);
        EzMybatisContent.init(config);
        EzMybatisContent.setDynamicTableResolver(config,
                context -> PhysicalTableRoute.of("tenant_schema", "user_2026", partition));
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION, configuration);
            SqlGenerateContext context = SqlGenerateContext.from(new MybatisParamHolder(configuration, params));
            EzMybatisContent.getConverter(configuration, DbTable.class).buildSql(
                    Converter.Type.INSERT, DbTable.of("user_base"), context);
            Assert.fail(databaseName + " partition table reference should be rejected");
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(e.getMessage().contains(databaseName));
        } finally {
            EzMybatisContent.destroy(configuration);
        }
    }
}
