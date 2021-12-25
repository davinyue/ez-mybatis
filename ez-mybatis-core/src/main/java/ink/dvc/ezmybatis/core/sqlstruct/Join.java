package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.GroupCondition;
import ink.dvc.ezmybatis.core.sqlstruct.join.JoinType;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class Join implements SqlStruct {

    /**
     * 主表
     */
    private Table table;
    /**
     * 关联类型
     */
    private JoinType joinType;
    /**
     * 被join表
     */
    private Table joinTable;
    /**
     * 链表条件
     */
    private List<Condition> onConditions;
    /**
     * 关联表
     */
    private List<Join> joins;

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return this.joinToSql(sqlBuilder, configuration, mybatisParamHolder);
    }

    protected StringBuilder joinToSql(StringBuilder sqlBuilder, Configuration configuration,
                                      MybatisParamHolder mybatisParamHolder) {
        StringBuilder sonSql;
        if (this.joinType == JoinType.CrossJoin) {
            sonSql = new StringBuilder();
        } else {
            sonSql = Where.conditionsToSqlPart(new StringBuilder(), configuration, mybatisParamHolder,
                    this.getOnConditions());
            if (sonSql.length() == 0) {
                return sqlBuilder;
            }
        }
        sqlBuilder.append(this.joinType.toSqlStruct()).append(this.joinTable.toSqlStruct(configuration));
        if (this.joinType != JoinType.CrossJoin) {
            sqlBuilder.append(" ON ");
        }
        sqlBuilder.append(sonSql);
        if (this.getJoins() != null && !this.getJoins().isEmpty()) {
            for (Join join : this.joins) {
                sqlBuilder.append(join.joinToSql(new StringBuilder(), configuration, mybatisParamHolder));
            }
        }
        return sqlBuilder;
    }

    public static class JoinBuilder<Builder> extends ConditionBuilder<Builder,
            JoinBuilder<Builder>> {
        private Join join;

        public JoinBuilder(Builder builder, Join join) {
            super(builder, join.getOnConditions(), join.getTable(), join.getJoinTable());
            this.sonBuilder = this;
            this.join = join;
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(Condition.LoginSymbol loginSymbol) {
            GroupCondition condition = new GroupCondition(new LinkedList<>(), loginSymbol);
            this.conditions.add(condition);
            Join newJoin = new Join();
            newJoin.setTable(this.join.getTable());
            newJoin.setJoinTable(this.join.getJoinTable());
            newJoin.setOnConditions(condition.getConditions());
            return new JoinBuilder<>(this, newJoin);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition() {
            return this.groupCondition(Condition.LoginSymbol.AND);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(JoinType joinType, Table joinTable) {
            if (this.join.getJoins() == null) {
                this.join.joins = new LinkedList<>();
            }
            Join newJoin = new Join();
            newJoin.setJoinType(joinType);
            newJoin.setTable(this.join.getJoinTable());
            newJoin.setJoinTable(joinTable);
            newJoin.setOnConditions(new LinkedList<>());
            this.join.joins.add(newJoin);
            return new Join.JoinBuilder<>(this, newJoin);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(Table joinTable) {
            return this.join(JoinType.InnerJoin, joinTable);
        }
    }
}
