package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class EzDelete extends EzParam<Integer> {
    private final List<Table> deletes = new LinkedList<>();
    private List<Join> joins;

    private EzDelete() {
        super(Integer.class);
    }

    public static EzDeleteBuilder delete(Table table) {
        return new EzDeleteBuilder(table);
    }

    public static class EzDeleteBuilder {
        private final EzDelete delete;

        private EzDeleteBuilder(Table table) {
            this.delete = new EzDelete();
            this.delete.deletes.add(table);
            this.delete.table = table;
            this.delete.from = new From(table);
        }

        public EzDeleteBuilder delete(EntityTable table) {
            this.delete.deletes.add(table);
            return this;
        }

        public Join.JoinBuilder<EzDeleteBuilder> join(boolean sure, JoinType joinType, Table joinTable) {
            if (this.delete.getJoins() == null) {
                this.delete.joins = new LinkedList<>();
            }
            Join join = new Join();
            join.setOnConditions(new LinkedList<>());
            join.setJoinType(joinType);
            join.setTable(this.delete.table);
            join.setSure(sure);
            join.setJoinTable(joinTable);
            this.delete.joins.add(join);
            return new Join.JoinBuilder<>(this, join);
        }

        public Join.JoinBuilder<EzDeleteBuilder> join(JoinType joinType, Table joinTable) {
            return this.join(true, joinType, joinTable);
        }

        public Join.JoinBuilder<EzDeleteBuilder> join(boolean sure, Table joinTable) {
            return this.join(sure, JoinType.InnerJoin, joinTable);
        }

        public Join.JoinBuilder<EzDeleteBuilder> join(Table joinTable) {
            return this.join(true, JoinType.InnerJoin, joinTable);
        }

        public EzDeleteBuilder join(Table joinTable, Consumer<Join.JoinBuilder<EzDeleteBuilder>> consumer) {
            Join.JoinBuilder<EzDeleteBuilder> builder = this.join(joinTable);
            consumer.accept(builder);
            return this;
        }

        public EzDeleteBuilder join(JoinType joinType, Table joinTable,
                                    Consumer<Join.JoinBuilder<EzDeleteBuilder>> consumer) {
            Join.JoinBuilder<EzDeleteBuilder> builder = this.join(joinType, joinTable);
            consumer.accept(builder);
            return this;
        }

        public EzDeleteBuilder join(boolean sure, Table joinTable,
                                    Consumer<Join.JoinBuilder<EzDeleteBuilder>> consumer) {
            if (sure) {
                return this.join(joinTable, consumer);
            }
            return this;
        }

        public EzDeleteBuilder join(boolean sure, JoinType joinType, Table joinTable,
                                    Consumer<Join.JoinBuilder<EzDeleteBuilder>> consumer) {
            if (sure) {
                return this.join(joinType, joinTable, consumer);
            }
            return this;
        }

        public Where.WhereBuilder<EzDeleteBuilder> where(boolean sure, Table table) {
            Where where = this.delete.where;
            if (where == null) {
                where = new Where(new LinkedList<>());
                if (sure) {
                    this.delete.where = where;
                }
            } else {
                if (!sure) {
                    where = new Where(new LinkedList<>());
                }
            }
            return new Where.WhereBuilder<>(this, where, table);
        }


        public Where.WhereBuilder<EzDeleteBuilder> where(Table table) {
            return this.where(true, table);
        }

        public Where.WhereBuilder<EzDeleteBuilder> where(boolean sure) {
            return this.where(sure, this.delete.table);
        }

        public Where.WhereBuilder<EzDeleteBuilder> where() {
            return this.where(true);
        }

        public EzDeleteBuilder where(Consumer<Where.WhereBuilder<EzDeleteBuilder>> consumer) {
            Where.WhereBuilder<EzDeleteBuilder> builder = this.where();
            consumer.accept(builder);
            return this;
        }

        public EzDeleteBuilder where(Table table, Consumer<Where.WhereBuilder<EzDeleteBuilder>> consumer) {
            Where.WhereBuilder<EzDeleteBuilder> builder = this.where(table);
            consumer.accept(builder);
            return this;
        }

        public EzDeleteBuilder where(boolean sure, Consumer<Where.WhereBuilder<EzDeleteBuilder>> consumer) {
            if (sure) {
                return this.where(consumer);
            }
            return this;
        }

        public EzDeleteBuilder where(boolean sure, Table table,
                                     Consumer<Where.WhereBuilder<EzDeleteBuilder>> consumer) {
            if (sure) {
                return this.where(table, consumer);
            }
            return this;
        }

        public EzDelete build() {
            return this.delete;
        }
    }
}
