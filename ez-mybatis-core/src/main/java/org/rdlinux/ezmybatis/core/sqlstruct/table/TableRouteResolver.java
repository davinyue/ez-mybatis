package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * 物理表路由解析器入口。
 *
 * <p>该类负责读取当前配置绑定的单个动态路由器、合并基础表结构与动态结果，
 * 并校验动态标识符。解析过程不会修改传入的 {@link DbTable}。</p>
 */
public final class TableRouteResolver {
    /**
     * 合法数据库标识符的基础格式。
     */
    private static final String IDENTIFIER_PATTERN = "[A-Za-z_][A-Za-z0-9_$#]*";

    private TableRouteResolver() {
    }

    /**
     * 解析当前配置下物理表的最终路由结果。
     *
     * @param configuration MyBatis 配置
     * @param table         当前物理表
     * @return 合并后的物理表路由结果
     */
    public static PhysicalTableRoute resolve(Configuration configuration, DbTable table) {
        TableRouteContext context = new TableRouteContext(configuration, table);
        DynamicTableResolver resolver = EzMybatisContent.getDynamicTableResolver(configuration);
        PhysicalTableRoute dynamicRoute = resolver == null ? null : resolver.resolve(context);
        if (dynamicRoute == null) {
            return new PhysicalTableRoute(table.getSchema(configuration), context.getBaseTableName(),
                    table.getPartition());
        }
        validateDynamicRoute(dynamicRoute);
        return new PhysicalTableRoute(dynamicRoute.getSchema(), dynamicRoute.getTableName(),
                dynamicRoute.getPartition());
    }

    /**
     * 校验动态路由结果中的标识符。
     *
     * @param route 动态路由结果
     */
    private static void validateDynamicRoute(PhysicalTableRoute route) {
        if (route == null) {
            return;
        }
        Assert.isTrue(hasText(route.getTableName()), "Dynamic table route tableName can not be empty");
        validateIdentifier(route.getSchema(), "schema");
        validateIdentifier(route.getTableName(), "tableName");
        Partition partition = route.getPartition();
        if (partition != null) {
            List<String> partitions = partition.getPartitions();
            if (partitions != null) {
                for (String partitionName : partitions) {
                    validateIdentifier(partitionName, "partition");
                }
            }
        }
    }

    /**
     * 校验单个标识符。
     *
     * @param value 标识符
     * @param field 字段名称
     */
    private static void validateIdentifier(String value, String field) {
        if (value == null || value.isEmpty()) {
            return;
        }
        Assert.isTrue(value.matches(IDENTIFIER_PATTERN),
                String.format("Dynamic %s contains an invalid database identifier: %s", field, value));
    }

    /**
     * 判断字符串是否有有效内容。
     *
     * @param value 待判断字符串
     * @return 有内容时返回 {@code true}
     */
    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
