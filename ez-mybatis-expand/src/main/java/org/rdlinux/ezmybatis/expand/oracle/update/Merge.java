package org.rdlinux.ezmybatis.expand.oracle.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateSetBuilder;

import java.util.LinkedList;

/**
 * merge update
 */
@Getter
public class Merge implements SqlStruct {
    static {

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
     * 更新列
     */
    private UpdateSet set;
    /**
     * 条件
     */
    private Where where;

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

        public MergeBuilder use(EzQueryTable table) {
            this.merge.useTable = table;
            return this;
        }

        public Where.WhereBuilder<MergeBuilder> where(Table table) {
            if (this.merge.where == null) {
                this.merge.where = new Where(new LinkedList<>());
            }
            return new Where.WhereBuilder<>(this, this.merge.where, table);
        }

        public Where.WhereBuilder<MergeBuilder> where() {
            return this.where(this.merge.mergeTable);
        }

        public UpdateSetBuilder<MergeBuilder> set() {
            return new UpdateSetBuilder<>(this, this.merge.mergeTable, this.merge.set);
        }

        public Merge build() {
            return this.merge;
        }
    }
}
