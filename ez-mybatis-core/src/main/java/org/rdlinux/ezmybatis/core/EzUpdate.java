package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateSetBuilder;
import org.rdlinux.ezmybatis.enumeration.JoinType;

import java.util.LinkedList;
import java.util.List;

@Getter
public class EzUpdate extends EzParam<Integer> {
    private UpdateSet set;
    private List<Join> joins;
    private Limit limit;

    private EzUpdate() {
        super(Integer.class);
        this.set = new UpdateSet();
    }

    public static EzUpdateBuilder update(Table table) {
        return new EzUpdateBuilder(table);
    }

    public static class EzUpdateBuilder {
        private EzUpdate ezUpdate;

        private EzUpdateBuilder(Table table) {
            this.ezUpdate = new EzUpdate();
            this.ezUpdate.table = table;
            this.ezUpdate.from = new From(table);
            this.ezUpdate.set = new UpdateSet();
        }

        public UpdateSetBuilder<EzUpdateBuilder> set(boolean sure) {
            if (sure) {
                return new UpdateSetBuilder<>(this, this.ezUpdate.table, this.ezUpdate.set);
            } else {
                return new UpdateSetBuilder<>(this, this.ezUpdate.table, new UpdateSet());
            }
        }

        public UpdateSetBuilder<EzUpdateBuilder> set() {
            return this.set(true);
        }

        public Join.JoinBuilder<EzUpdateBuilder> join(boolean sure, JoinType joinType, Table joinTable) {
            if (this.ezUpdate.getJoins() == null) {
                this.ezUpdate.joins = new LinkedList<>();
            }
            Join join = new Join();
            join.setJoinType(joinType);
            join.setTable(this.ezUpdate.table);
            join.setJoinTable(joinTable);
            join.setOnConditions(new LinkedList<>());
            join.setSure(sure);
            this.ezUpdate.joins.add(join);
            return new Join.JoinBuilder<>(this, join);
        }


        public Join.JoinBuilder<EzUpdateBuilder> join(JoinType joinType, Table joinTable) {
            return this.join(true, joinType, joinTable);
        }

        public Join.JoinBuilder<EzUpdateBuilder> join(boolean sure, Table joinTable) {
            return this.join(sure, JoinType.InnerJoin, joinTable);
        }

        public Join.JoinBuilder<EzUpdateBuilder> join(Table joinTable) {
            return this.join(true, JoinType.InnerJoin, joinTable);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where(boolean sure, Table table) {
            Where where = this.ezUpdate.where;
            if (where == null) {
                where = new Where(new LinkedList<>());
                if (sure) {
                    this.ezUpdate.where = where;
                }
            } else {
                if (!sure) {
                    where = new Where(new LinkedList<>());
                }
            }
            return new Where.WhereBuilder<>(this, where, table);
        }


        public Where.WhereBuilder<EzUpdateBuilder> where(Table table) {
            return this.where(true, table);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where(boolean sure) {
            return this.where(sure, this.ezUpdate.table);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where() {
            return this.where(true);
        }

        public EzUpdateBuilder limit(boolean sure, int limit) {
            if (sure) {
                this.ezUpdate.limit = new Limit(limit);
            }
            return this;
        }

        public EzUpdateBuilder limit(int limit) {
            return this.limit(true, limit);
        }

        public EzUpdate build() {
            return this.ezUpdate;
        }
    }
}
