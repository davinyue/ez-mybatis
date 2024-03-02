package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.join.JoinType;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateSetBuilder;

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

        public UpdateSetBuilder<EzUpdateBuilder> set() {
            return new UpdateSetBuilder<>(this, this.ezUpdate.table, this.ezUpdate.set);
        }


        public Join.JoinBuilder<EzUpdateBuilder> join(JoinType joinType, Table joinTable) {
            if (this.ezUpdate.getJoins() == null) {
                this.ezUpdate.joins = new LinkedList<>();
            }
            Join join = new Join();
            join.setJoinType(joinType);
            join.setTable(this.ezUpdate.table);
            join.setJoinTable(joinTable);
            join.setOnConditions(new LinkedList<>());
            this.ezUpdate.joins.add(join);
            return new Join.JoinBuilder<>(this, join);
        }

        public Join.JoinBuilder<EzUpdateBuilder> join(Table joinTable) {
            return this.join(JoinType.InnerJoin, joinTable);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where(Table table) {
            if (this.ezUpdate.where == null) {
                this.ezUpdate.where = new Where(new LinkedList<>());
            }
            return new Where.WhereBuilder<>(this, this.ezUpdate.where, table);
        }

        public Where.WhereBuilder<EzUpdateBuilder> where() {
            return this.where(this.ezUpdate.table);
        }

        public EzUpdateBuilder limit(int limit) {
            this.ezUpdate.limit = new Limit(limit);
            return this;
        }

        public EzUpdate build() {
            return this.ezUpdate;
        }
    }
}
