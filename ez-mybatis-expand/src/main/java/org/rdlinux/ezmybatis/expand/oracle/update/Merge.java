package org.rdlinux.ezmybatis.expand.oracle.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateSetBuilder;
import org.rdlinux.ezmybatis.expand.mssql.converter.SqlServerMergeConverter;
import org.rdlinux.ezmybatis.expand.oracle.converter.OracleMergeConverter;
import org.rdlinux.ezmybatis.expand.postgre.converter.PostgreMergeConverter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * MERGE 扩展 SQL 结构。
 *
 * <p>该结构用于描述 Oracle / DM / PostgreSQL / SQL Server 风格的 MERGE 语句，
 * 支持目标表、源子查询、ON 匹配条件、匹配后更新以及未匹配时插入等语义。</p>
 *
 * <p>构建时要求：</p>
 * <p>1. 必须显式指定目标表与 using 子查询。</p>
 * <p>2. ON 条件必须同时引用目标表与 using 表。</p>
 * <p>3. SET 中的更新列必须属于目标表。</p>
 */
@Getter
public class Merge implements SqlExpand {
    /**
     * 注册 MERGE 在各数据库方言下对应的转换器。
     */
    static {
        EzMybatisContent.addConverter(DbType.ORACLE, Merge.class, OracleMergeConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Merge.class, OracleMergeConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Merge.class, PostgreMergeConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Merge.class, SqlServerMergeConverter.getInstance());
    }

    /**
     * MERGE 的目标表。
     */
    private Table mergeTable;
    /**
     * MERGE 的 using 子查询表。
     */
    private EzQueryTable useTable;
    /**
     * MERGE 的 ON 匹配条件列表。
     */
    private List<Condition> on;
    /**
     * 匹配成功时执行的更新集合。
     */
    private UpdateSet set;
    /**
     * 未匹配时用于插入的实体对象。
     */
    private Object notMatchedInsertEntity;

    /**
     * 创建空的 MERGE 结构。
     */
    private Merge() {
    }

    /**
     * 以目标表作为起点创建 MERGE 构造器。
     *
     * @param table MERGE 目标表
     * @return MERGE 构造器
     */
    public static MergeBuilder into(Table table) {
        return new MergeBuilder(table);
    }

    /**
     * {@link Merge} 构造器。
     */
    public static class MergeBuilder {
        /**
         * 当前构建中的 MERGE 结构。
         */
        private final Merge merge;

        /**
         * 使用目标表初始化 MERGE 构造器。
         *
         * @param table MERGE 目标表
         */
        private MergeBuilder(Table table) {
            this.merge = new Merge();
            this.merge.mergeTable = table;
            this.merge.set = new UpdateSet();
        }

        /**
         * 判断两个表结构是否表示同一个逻辑表引用。
         *
         * @param left  左侧表
         * @param right 右侧表
         * @return 两者是否可视为同一表引用
         */
        private static boolean sameTable(Table left, Table right) {
            if (left == right) {
                return true;
            }
            if (left == null || right == null) {
                return false;
            }
            return Objects.equals(left.getAlias(), right.getAlias())
                    && Objects.equals(left.getClass(), right.getClass());
        }

        /**
         * 指定 MERGE 的 using 子查询表。
         *
         * @param table using 子查询表
         * @return 当前构造器
         */
        public MergeBuilder using(EzQueryTable table) {
            this.merge.useTable = table;
            return this;
        }

        /**
         * 构建 MERGE 的 ON 条件。
         *
         * @param consumer ON 条件构建回调
         * @return 当前构造器
         */
        public MergeBuilder on(Consumer<MergeOnBuilder> consumer) {
            if (this.merge.useTable == null) {
                throw new IllegalArgumentException("Please use the 'using' method to initialize the table being used first.");
            }
            if (this.merge.on == null) {
                this.merge.on = new LinkedList<>();
            }
            MergeOnBuilder builder = new MergeOnBuilder(this.merge.on);
            consumer.accept(builder);
            return this;
        }

        /**
         * 获取匹配成功后更新集合的构造器。
         *
         * @return 更新集合构造器
         */
        public UpdateSetBuilder<MergeBuilder> set() {
            return new UpdateSetBuilder<>(this, this.merge.mergeTable, this.merge.set);
        }

        /**
         * 通过回调构建匹配成功后的更新集合。
         *
         * @param consumer 更新集合构建回调
         * @return 当前构造器
         */
        public MergeBuilder set(java.util.function.Consumer<UpdateSetBuilder<MergeBuilder>> consumer) {
            UpdateSetBuilder<MergeBuilder> builder = this.set();
            consumer.accept(builder);
            return this;
        }

        /**
         * 指定未匹配时要插入的实体对象。
         *
         * @param entity 插入实体
         * @return 当前构造器
         */
        public MergeBuilder whenNotMatchedThenInsert(Object entity) {
            this.merge.notMatchedInsertEntity = entity;
            return this;
        }

        /**
         * 完成构建并在返回前执行结构校验。
         *
         * @return 构建完成的 MERGE 结构
         */
        public Merge build() {
            this.validate();
            return this.merge;
        }

        /**
         * 校验当前 MERGE 结构是否满足最基本的构造约束。
         */
        private void validate() {
            if (this.merge.mergeTable == null) {
                throw new IllegalStateException("Merge target table can not be null");
            }
            if (this.merge.useTable == null) {
                throw new IllegalStateException("Merge using table can not be null");
            }
            if (this.merge.on == null || this.merge.on.isEmpty()) {
                throw new IllegalStateException("Merge ON conditions can not be empty");
            }
            if (this.merge.set == null || this.merge.set.getItems() == null || this.merge.set.getItems().isEmpty()) {
                throw new IllegalStateException("Merge SET items can not be empty");
            }
            this.validateSetItems();
            this.validateOnConditions();
            this.validateInsertEntity();
        }

        /**
         * 校验 SET 集合中的更新项是否全部属于目标表。
         */
        private void validateSetItems() {
            for (UpdateItem item : this.merge.set.getItems()) {
                if (item == null) {
                    throw new IllegalStateException("Merge SET item can not be null");
                }
                if (!sameTable(item.getTable(), this.merge.mergeTable)) {
                    throw new IllegalStateException("Merge SET target must belong to merge table");
                }
            }
        }

        /**
         * 校验 ON 条件是否同时引用目标表和 using 表。
         */
        private void validateOnConditions() {
            OnConditionTableUsage usage = new OnConditionTableUsage();
            for (Condition condition : this.merge.on) {
                this.validateCondition(condition, usage);
            }
            if (!usage.mergeTableUsed) {
                throw new IllegalStateException("Merge ON conditions must reference merge table");
            }
            if (!usage.useTableUsed) {
                throw new IllegalStateException("Merge ON conditions must reference using table");
            }
        }

        /**
         * 递归校验单个条件节点中的表使用情况。
         *
         * @param condition 条件节点
         * @param usage     条件中的表使用情况统计
         */
        private void validateCondition(Condition condition, OnConditionTableUsage usage) {
            if (condition == null) {
                return;
            }
            if (condition instanceof GroupCondition) {
                for (Condition child : ((GroupCondition) condition).getConditions()) {
                    this.validateCondition(child, usage);
                }
                return;
            }
            if (condition instanceof ArgCompareArgCondition) {
                ArgCompareArgCondition compare = (ArgCompareArgCondition) condition;
                this.validateOperand(compare.getLeftValue(), usage);
                this.validateOperand(compare.getRightValue(), usage);
                this.validateOperand(compare.getMinValue(), usage);
                this.validateOperand(compare.getMaxValue(), usage);
                if (compare.getRightValues() != null) {
                    for (Operand operand : compare.getRightValues()) {
                        this.validateOperand(operand, usage);
                    }
                }
            }
        }

        /**
         * 校验单个操作数引用的表是否合法。
         *
         * @param operand 操作数
         * @param usage   条件中的表使用情况统计
         */
        private void validateOperand(Operand operand, OnConditionTableUsage usage) {
            if (operand == null) {
                return;
            }
            Table table = null;
            if (operand instanceof EntityField) {
                table = ((EntityField) operand).getTable();
            } else if (operand instanceof TableColumn) {
                table = ((TableColumn) operand).getTable();
            }
            if (table == null) {
                return;
            }
            if (sameTable(table, this.merge.mergeTable)) {
                usage.mergeTableUsed = true;
                return;
            }
            if (sameTable(table, this.merge.useTable)) {
                usage.useTableUsed = true;
                return;
            }
            throw new IllegalStateException("Merge ON conditions can only reference merge table and using table");
        }

        /**
         * 校验未匹配插入实体的类型与结构限制。
         */
        private void validateInsertEntity() {
            Object entity = this.merge.notMatchedInsertEntity;
            if (entity == null) {
                return;
            }
            if (entity instanceof java.util.Collection) {
                throw new IllegalStateException("Merge insert entity can not be a collection");
            }
            if (entity instanceof Map) {
                throw new IllegalStateException("Merge insert entity can not be a map");
            }
            if (entity.getClass().isArray()) {
                throw new IllegalStateException("Merge insert entity can not be an array");
            }
            if (this.merge.mergeTable instanceof EntityTable) {
                Class<?> etType = ((EntityTable) this.merge.mergeTable).getEtType();
                if (etType != null && !etType.isAssignableFrom(entity.getClass())) {
                    throw new IllegalStateException("Merge insert entity type must match merge table entity type");
                }
            }
        }

        /**
         * 记录 ON 条件中目标表与 using 表是否被引用。
         */
        private static class OnConditionTableUsage {
            /**
             * 是否引用了 MERGE 目标表。
             */
            private boolean mergeTableUsed;
            /**
             * 是否引用了 using 子查询表。
             */
            private boolean useTableUsed;
        }

        /**
         * MERGE ON 条件构造器。
         */
        public static class MergeOnBuilder extends ConditionBuilder<MergeOnBuilder> {

            /**
             * 使用底层条件列表初始化 ON 构造器。
             *
             * @param on ON 条件列表
             */
            private MergeOnBuilder(List<Condition> on) {
                super(on);
            }


        }
    }
}
