package org.rdlinux;

import org.apache.ibatis.session.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlDbTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DynamicTableResolver;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.PhysicalTableRoute;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.TableRouteResolver;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态物理表路由测试。
 */
public class DynamicTableRouteTest {
    /**
     * 当前测试使用的 MyBatis 配置。
     */
    private Configuration configuration;

    /**
     * 初始化测试配置。
     */
    @Before
    public void setUp() {
        this.configuration = new Configuration();
        EzMybatisConfig config = new EzMybatisConfig(this.configuration);
        config.setDbType(DbType.MYSQL);
        EzMybatisContent.init(config);
    }

    /**
     * 清理测试配置。
     */
    @After
    public void tearDown() {
        EzMybatisContent.destroyAll();
    }

    /**
     * 验证动态路由可以补充 schema、表名和分区，并且实体显式表名可以被替换。
     */
    @Test
    public void shouldResolveSchemaTableNameAndPartition() {
        final EntityTable table = EntityTable.of(RouteUser.class, "user_custom");
        DynamicTableResolver resolver = context -> {
            Assert.assertSame(table, context.getTable());
            Assert.assertEquals("user_custom", context.getBaseTableName());
            Assert.assertNull(context.getTable().getSchema(context.getConfiguration()));
            Assert.assertSame(RouteUser.class, context.getEntityType());
            return PhysicalTableRoute.of("tenant_01", "user_2026", NormalPartition.of("p_2026"));
        };
        EzMybatisContent.setDynamicTableResolver(new EzMybatisConfig(this.configuration), resolver);

        PhysicalTableRoute route = TableRouteResolver.resolve(this.configuration, table);

        Assert.assertEquals("tenant_01", route.getSchema());
        Assert.assertEquals("user_2026", route.getTableName());
        Assert.assertEquals("p_2026", route.getPartition().getPartitions().get(0));
        Assert.assertNull(table.getSchema());
        Assert.assertEquals("user_custom", table.getTableName(this.configuration));
        Assert.assertNull(table.getPartition());
    }

    /**
     * 验证非空动态路由完整覆盖基础 schema、表名和分区。
     */
    @Test
    public void shouldUseAllDynamicRouteValues() {
        NormalPartition explicitPartition = NormalPartition.of("p_fixed");
        EntityTable table = EntityTable.of("schema_fixed", "user_custom", RouteUser.class, explicitPartition);
        EzMybatisContent.setDynamicTableResolver(new EzMybatisConfig(this.configuration), context -> {
            Assert.assertEquals("schema_fixed", context.getTable().getSchema(context.getConfiguration()));
            Assert.assertNotNull(context.getTable().getPartition());
            return PhysicalTableRoute.of("schema_dynamic", "user_dynamic", NormalPartition.of("p_dynamic"));
        });

        PhysicalTableRoute route = TableRouteResolver.resolve(this.configuration, table);

        Assert.assertEquals("schema_dynamic", route.getSchema());
        Assert.assertEquals("user_dynamic", route.getTableName());
        Assert.assertNotSame(explicitPartition, route.getPartition());
        Assert.assertEquals("p_dynamic", route.getPartition().getPartitions().get(0));
    }

    /**
     * 验证未显式指定表名时，基础表名来自实体元数据解析结果。
     */
    @Test
    public void shouldExposeEntityMetadataBaseTableName() {
        EntityTable table = EntityTable.of(RouteUser.class);
        EzMybatisContent.setDynamicTableResolver(new EzMybatisConfig(this.configuration), context -> {
            Assert.assertEquals("route_user", context.getBaseTableName());
            return null;
        });

        PhysicalTableRoute route = TableRouteResolver.resolve(this.configuration, table);

        Assert.assertEquals("route_user", route.getTableName());
    }

    /**
     * 验证 Resolver 返回 null 时完整沿用原表的 schema、表名和分区。
     */
    @Test
    public void shouldKeepBaseRouteWhenResolverReturnsNull() {
        NormalPartition partition = NormalPartition.of("p_base");
        DbTable table = DbTable.of("schema_base", "user_base", partition);
        EzMybatisContent.setDynamicTableResolver(new EzMybatisConfig(this.configuration), context -> null);

        PhysicalTableRoute route = TableRouteResolver.resolve(this.configuration, table);

        Assert.assertEquals("schema_base", route.getSchema());
        Assert.assertEquals("user_base", route.getTableName());
        Assert.assertSame(partition, route.getPartition());
    }

    /**
     * 验证物理表转换器使用动态路由，且派生表不会触发动态路由。
     */
    @Test
    public void shouldRoutePhysicalTableOnly() {
        final int[] resolverCalls = {0};
        EzMybatisContent.setDynamicTableResolver(new EzMybatisConfig(this.configuration), context -> {
            resolverCalls[0]++;
            return PhysicalTableRoute.of("tenant_01", "user_2026", null);
        });

        SqlGenerateContext context = createSqlGenerateContext();
        MySqlDbTableConverter.getInstance().buildSql(Converter.Type.SELECT, DbTable.of("user"), context);

        Assert.assertTrue(context.getSqlBuilder().toString(),
                context.getSqlBuilder().toString().contains("tenant_01")
                        && context.getSqlBuilder().toString().contains("user_2026"));
        Assert.assertEquals(1, resolverCalls[0]);

        SqlGenerateContext derivedTableContext = createSqlGenerateContext();
        EzMybatisContent.getConverter(this.configuration, SqlTable.class).buildSql(
                Converter.Type.SELECT, SqlTable.of("SELECT 1"), derivedTableContext);
        Assert.assertEquals(1, resolverCalls[0]);
    }

    /**
     * 验证同一个 MyBatis 配置不能注册多个动态路由器。
     */
    @Test
    public void shouldAllowOnlyOneResolverPerConfiguration() {
        DynamicTableResolver first = context -> null;
        DynamicTableResolver second = context -> null;
        EzMybatisConfig config = new EzMybatisConfig(this.configuration);
        EzMybatisContent.setDynamicTableResolver(config, first);

        try {
            EzMybatisContent.setDynamicTableResolver(config, second);
            Assert.fail("Multiple resolvers should be rejected");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Only one DynamicTableResolver"));
        }
    }

    /**
     * 验证动态标识符不允许携带 SQL 片段。
     */
    @Test
    public void shouldRejectInvalidDynamicIdentifier() {
        EzMybatisContent.setDynamicTableResolver(new EzMybatisConfig(this.configuration),
                context -> PhysicalTableRoute.of("tenant_01", "user;drop_table", null));

        try {
            TableRouteResolver.resolve(this.configuration, DbTable.of("user"));
            Assert.fail("Invalid table name should be rejected");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("tableName"));
        }
    }

    /**
     * 验证非空 PhysicalTableRoute 必须提供非空表名。
     */
    @Test
    public void shouldRequireDynamicTableName() {
        EzMybatisContent.setDynamicTableResolver(new EzMybatisConfig(this.configuration),
                context -> PhysicalTableRoute.of(null, "", null));

        try {
            TableRouteResolver.resolve(this.configuration, DbTable.of("user"));
            Assert.fail("Dynamic table route should require table name");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("tableName"));
        }
    }

    /**
     * 创建 SQL 生成上下文。
     *
     * @return SQL 生成上下文
     */
    private SqlGenerateContext createSqlGenerateContext() {
        Map<String, Object> params = new HashMap<>();
        params.put(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION, this.configuration);
        MybatisParamHolder paramHolder = new MybatisParamHolder(this.configuration, params);
        return SqlGenerateContext.from(paramHolder);
    }

    /**
     * 用于验证实体表基础信息的测试实体。
     */
    private static class RouteUser {
        /**
         * 测试主键。
         */
        private Long id;
    }
}
