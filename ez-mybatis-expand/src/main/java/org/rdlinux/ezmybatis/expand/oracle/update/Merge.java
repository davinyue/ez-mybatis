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
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.expand.mssql.converter.SqlServerMergeConverter;
import org.rdlinux.ezmybatis.expand.oracle.converter.OracleMergeConverter;
import org.rdlinux.ezmybatis.expand.postgre.converter.PostgreMergeConverter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * merge update
 */
@Getter
public class Merge implements SqlExpand {
    //转换器注册
    static {
        EzMybatisContent.addConverter(DbType.ORACLE, Merge.class, OracleMergeConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Merge.class, OracleMergeConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Merge.class, PostgreMergeConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Merge.class, SqlServerMergeConverter.getInstance());
    }

    /**
     * 被更新表
     */
    private Table mergeTable;
    /**
     * 子查询
     */
    private EzQueryTable useTable;
    /**
     * 条件
     */
    private List<Condition> on;
    /**
     * 更新列
     */
    private UpdateSet set;
    private Object notMatchedInsertEntity;

    private Merge() {
    }

    public static MergeBuilder into(Table table) {
        return new MergeBuilder(table);
    }

    public static class MergeBuilder {
        private final Merge merge;

        private MergeBuilder(Table table) {
            this.merge = new Merge();
            this.merge.mergeTable = table;
            this.merge.set = new UpdateSet();
        }

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

        public MergeBuilder using(EzQueryTable table) {
            this.merge.useTable = table;
            return this;
        }

        public MergeOnBuilder<MergeBuilder> on(Table table) {
            if (this.merge.useTable == null) {
                throw new IllegalArgumentException("Please use the 'using' method to initialize the table being used first.");
            }
            if (this.merge.on == null) {
                this.merge.on = new LinkedList<>();
            }
            return new MergeOnBuilder<>(this, this.merge.on, table, this.merge.useTable);
        }

        public MergeOnBuilder<MergeBuilder> on() {
            return this.on(this.merge.mergeTable);
        }

        public MergeBuilder on(Table table, java.util.function.Consumer<MergeOnBuilder<MergeBuilder>> consumer) {
            MergeOnBuilder<MergeBuilder> builder = this.on(table);
            consumer.accept(builder);
            return this;
        }

        public MergeBuilder on(java.util.function.Consumer<MergeOnBuilder<MergeBuilder>> consumer) {
            MergeOnBuilder<MergeBuilder> builder = this.on();
            consumer.accept(builder);
            return this;
        }

        public UpdateSetBuilder<MergeBuilder> set() {
            return new UpdateSetBuilder<>(this, this.merge.mergeTable, this.merge.set);
        }

        public MergeBuilder set(java.util.function.Consumer<UpdateSetBuilder<MergeBuilder>> consumer) {
            UpdateSetBuilder<MergeBuilder> builder = this.set();
            consumer.accept(builder);
            return this;
        }

        public MergeBuilder whenNotMatchedThenInsert(Object entity) {
            this.merge.notMatchedInsertEntity = entity;
            return this;
        }

        public Merge build() {
            this.validate();
            return this.merge;
        }

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

        private static class OnConditionTableUsage {
            private boolean mergeTableUsed;
            private boolean useTableUsed;
        }

        public static class MergeOnBuilder<Builder> extends ConditionBuilder<Builder, MergeOnBuilder<Builder>> {

            private MergeOnBuilder(Builder builder, List<Condition> on, Table mergeTable, EzQueryTable useTable) {
                super(builder, on, mergeTable, useTable);
                this.sonBuilder = this;
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition(boolean sure, AndOr andOr) {
                GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), andOr);
                this.conditions.add(condition);
                return new MergeOnBuilder<>(this, condition.getConditions(), this.table,
                        (EzQueryTable) this.otherTable);
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition(AndOr andOr) {
                return this.groupCondition(true, andOr);
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition(boolean sure) {
                return this.groupCondition(sure, AndOr.AND);
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition() {
                return this.groupCondition(true);
            }
        }
    }
}
