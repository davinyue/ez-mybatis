package ink.dvc.ezmybatis.core;

import lombok.Getter;
import ink.dvc.ezmybatis.core.sqlstruct.From;
import ink.dvc.ezmybatis.core.sqlstruct.Join;
import ink.dvc.ezmybatis.core.sqlstruct.Where;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

@Getter
public class EzDelete extends EzParam<Integer> {
    private List<Table> deletes = new LinkedList<>();
    private List<Join> joins;

    private EzDelete() {
        super(Integer.class);
    }

    public static EzDeleteBuilder delete(EntityTable table) {
        return new EzDeleteBuilder(table);
    }

    public static class EzDeleteBuilder {
        private EzDelete delete;

        private EzDeleteBuilder(EntityTable table) {
            this.delete = new EzDelete();
            this.delete.deletes.add(table);
            this.delete.table = table;
            this.delete.from = new From(table);
        }

        public EzDeleteBuilder delete(EntityTable table) {
            this.delete.deletes.add(table);
            return this;
        }

        public Join.JoinBuilder<EzDeleteBuilder> join(EntityTable joinTable) {
            if (this.delete.getJoins() == null) {
                this.delete.joins = new LinkedList<>();
            }
            Join join = new Join();
            this.delete.joins.add(join);
            return new Join.JoinBuilder<>(this, join, this.delete.table, joinTable);
        }

        public Where.WhereBuilder<EzDeleteBuilder> where() {
            Where where = new Where(new LinkedList<>());
            this.delete.where = where;
            return new Where.WhereBuilder<>(this, where, this.delete.table);
        }

        public EzDelete build() {
            return this.delete;
        }
    }
}
