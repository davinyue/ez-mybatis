package ink.dvc.ezmybatis.core;

import ink.dvc.ezmybatis.core.sqlstruct.From;
import ink.dvc.ezmybatis.core.sqlstruct.Join;
import ink.dvc.ezmybatis.core.sqlstruct.Where;
import ink.dvc.ezmybatis.core.sqlstruct.join.JoinType;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import lombok.Getter;

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

        public Join.JoinBuilder<EzDeleteBuilder> join(JoinType joinType, Table joinTable) {
            if (this.delete.getJoins() == null) {
                this.delete.joins = new LinkedList<>();
            }
            Join join = new Join();
            join.setJoinType(joinType);
            join.setTable(this.delete.table);
            join.setJoinTable(joinTable);
            this.delete.joins.add(join);
            return new Join.JoinBuilder<>(this, join);
        }

        public Join.JoinBuilder<EzDeleteBuilder> join(Table joinTable) {
            return this.join(JoinType.InnerJoin, joinTable);
        }

        public Where.WhereBuilder<EzDeleteBuilder> where(Table table) {
            if (this.delete.where == null) {
                this.delete.where = new Where(new LinkedList<>());
            }
            return new Where.WhereBuilder<>(this, this.delete.where, table);
        }

        public Where.WhereBuilder<EzDeleteBuilder> where() {
            return this.where(this.delete.table);
        }

        public EzDelete build() {
            return this.delete;
        }
    }
}
