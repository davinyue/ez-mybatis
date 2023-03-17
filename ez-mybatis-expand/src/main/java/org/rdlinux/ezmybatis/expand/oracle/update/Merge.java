package org.rdlinux.ezmybatis.expand.oracle.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlExpand;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateSetBuilder;
import org.rdlinux.ezmybatis.expand.oracle.converter.OracleMergeConverter;

import java.util.LinkedList;
import java.util.List;

/**
 * merge update
 */
@Getter
public class Merge implements SqlExpand {
    //转换器注册
    static {
        EzMybatisContent.addConverter(DbType.ORACLE, Merge.class, OracleMergeConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Merge.class, OracleMergeConverter.getInstance());
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

    private Merge() {
    }

    public static MergeBuilder into(Table table) {
        return new MergeBuilder(table);
    }

    public static class MergeBuilder {
        private Merge merge;

        private MergeBuilder(Table table) {
            this.merge = new Merge();
            this.merge.mergeTable = table;
            this.merge.set = new UpdateSet();
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

        public UpdateSetBuilder<MergeBuilder> set() {
            return new UpdateSetBuilder<>(this, this.merge.mergeTable, this.merge.set);
        }

        public Merge build() {
            return this.merge;
        }

        public static class MergeOnBuilder<Builder> extends ConditionBuilder<Builder, MergeOnBuilder<Builder>> {

            private MergeOnBuilder(Builder builder, List<Condition> on, Table mergeTable, EzQueryTable useTable) {
                super(builder, on, mergeTable, useTable);
                this.sonBuilder = this;
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition(boolean sure, LogicalOperator logicalOperator) {
                GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), logicalOperator);
                this.conditions.add(condition);
                return new MergeOnBuilder<>(this, condition.getConditions(), this.table,
                        (EzQueryTable) this.otherTable);
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition(LogicalOperator logicalOperator) {
                return this.groupCondition(true, logicalOperator);
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition(boolean sure) {
                return this.groupCondition(sure, LogicalOperator.AND);
            }

            public MergeOnBuilder<MergeOnBuilder<Builder>> groupCondition() {
                return this.groupCondition(true);
            }
        }
    }
}
