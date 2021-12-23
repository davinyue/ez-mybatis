package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.GroupCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.condition.compare.ColumnCompareCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.compare.FieldCompareCondition;
import ink.dvc.ezmybatis.core.sqlstruct.join.JoinType;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
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
        if (this.getJoins() != null && !this.getJoins().isEmpty()) {
            for (Join join : this.joins) {
                sqlBuilder.append(join.joinToSql(new StringBuilder(), configuration, mybatisParamHolder));
            }
        }
        sqlBuilder.append(sonSql);
        return sqlBuilder;
    }

    public static class JoinBuilder<T> {
        private T target;
        private Join join;

        public JoinBuilder(T target, Join join, EntityTable table, EntityTable joinTable) {
            this.target = target;
            this.join = join;
            if (join.getOnConditions() == null) {
                this.join.setOnConditions(new LinkedList<>());
            }
            this.join.setTable(table);
            this.join.setJoinType(JoinType.InnerJoin);
            this.join.setJoinTable(joinTable);
        }

        public JoinBuilder<T> type(JoinType type) {
            this.join.setJoinType(type);
            return this;
        }

        public JoinConditionBuilder<JoinBuilder<T>> conditions() {
            return new JoinConditionBuilder<>(this, this.join.getOnConditions(), this.join.getTable(),
                    this.join.getJoinTable());
        }

        public T done() {
            return this.target;
        }
    }

    public static class JoinConditionBuilder<Builder> extends ConditionBuilder<Builder,
            JoinConditionBuilder<Builder>> {
        private Table masterTable;
        private Table joinTable;

        public JoinConditionBuilder(Builder builder, List<Condition> conditions, Table masterTable,
                                    Table joinTable) {
            super(builder, conditions, masterTable);
            this.masterTable = masterTable;
            this.joinTable = joinTable;
            this.sonBuilder = this;
        }

        private void checkAllEntityTable() {
            this.checkLeftEntityTable();
            this.checkRightEntityTable();
        }

        private void checkLeftEntityTable() {
            if (!(this.masterTable instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        private void checkRightEntityTable() {
            if (!(this.joinTable instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(String masterField, String joinField) {
            return this.add(masterField, Operator.eq, joinField);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(boolean sure, String masterField, String joinField) {
            if (sure) {
                this.add(masterField, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(Condition.LoginSymbol loginSymbol, String masterField,
                                                 String joinField) {
            return this.add(loginSymbol, masterField, Operator.eq, joinField);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(boolean sure, Condition.LoginSymbol loginSymbol, String masterField,
                                                 String joinField) {
            if (sure) {
                this.add(loginSymbol, masterField, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(String masterField, Operator operator, String joinField) {
            return this.add(Condition.LoginSymbol.AND, masterField, operator, joinField);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(boolean sure, String masterField, Operator operator, String joinField) {
            if (sure) {
                this.add(masterField, operator, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(Condition.LoginSymbol loginSymbol, String masterField,
                                                 Operator operator, String joinField) {
            this.checkAllEntityTable();
            this.conditions.add(new FieldCompareCondition(loginSymbol, (EntityTable) this.masterTable, masterField,
                    operator, (EntityTable) this.joinTable, joinField));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(boolean sure, Condition.LoginSymbol loginSymbol, String masterField,
                                                 Operator operator, String joinField) {
            if (sure) {
                return this.add(loginSymbol, masterField, operator, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(String masterColumn, String joinColumn) {
            this.conditions.add(new ColumnCompareCondition(this.masterTable, masterColumn, Operator.eq, this.joinTable,
                    joinColumn));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(boolean sure, String masterColumn, String joinColumn) {
            if (sure) {
                return this.addColumn(masterColumn, joinColumn);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(Condition.LoginSymbol loginSymbol, String masterColumn,
                                                       String joinColumn) {
            return this.add(loginSymbol, masterColumn, Operator.eq, joinColumn);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(boolean sure, Condition.LoginSymbol loginSymbol,
                                                       String masterColumn, String joinColumn) {
            if (sure) {
                return this.addColumn(loginSymbol, masterColumn, joinColumn);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(String masterColumn, Operator operator, String joinColumn) {
            return this.add(Condition.LoginSymbol.AND, masterColumn, operator, joinColumn);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(boolean sure, String masterColumn, Operator operator,
                                                       String joinColumn) {
            if (sure) {
                return this.addColumn(masterColumn, operator, joinColumn);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(Condition.LoginSymbol loginSymbol, String masterColumn,
                                                       Operator operator, String joinColumn) {
            this.conditions.add(new ColumnCompareCondition(loginSymbol, this.masterTable, masterColumn, operator,
                    this.joinTable, joinColumn));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(boolean sure, Condition.LoginSymbol loginSymbol,
                                                       String masterColumn, Operator operator, String joinColumn) {
            if (sure) {
                return this.addColumn(loginSymbol, masterColumn, operator, joinColumn);
            }
            return this;
        }

        public JoinConditionBuilder<JoinConditionBuilder<Builder>> groupCondition(Condition.LoginSymbol loginSymbol) {
            LinkedList<Condition> conditions = new LinkedList<>();
            GroupCondition condition = new GroupCondition(conditions, loginSymbol);
            this.conditions.add(condition);
            return new JoinConditionBuilder<>(this, conditions, this.masterTable, this.joinTable);
        }

        public JoinConditionBuilder<JoinConditionBuilder<Builder>> groupCondition() {
            return this.groupCondition(Condition.LoginSymbol.AND);
        }
    }
}
