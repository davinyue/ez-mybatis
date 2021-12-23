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

    public static class JoinBuilder<Builder> extends ConditionBuilder<Builder,
            JoinBuilder<Builder>> {
        private Join join;

        public JoinBuilder(Builder builder, Join join) {
            super(builder, join.getOnConditions(), join.getTable());
            this.sonBuilder = this;
            this.join = join;
        }

        private void checkAllEntityTable() {
            this.checkLeftEntityTable();
            this.checkRightEntityTable();
        }

        private void checkLeftEntityTable() {
            if (!(this.join.getTable() instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        private void checkRightEntityTable() {
            if (!(this.join.getJoinTable() instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(String masterField, String joinField) {
            return this.addFieldCondition(masterField, Operator.eq, joinField);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(boolean sure, String masterField, String joinField) {
            if (sure) {
                this.addFieldCondition(masterField, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(Condition.LoginSymbol loginSymbol, String masterField,
                                                      String joinField) {
            return this.addFieldCondition(loginSymbol, masterField, Operator.eq, joinField);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(boolean sure, Condition.LoginSymbol loginSymbol, String masterField,
                                                      String joinField) {
            if (sure) {
                this.addFieldCondition(loginSymbol, masterField, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(String masterField, Operator operator, String joinField) {
            return this.addFieldCondition(Condition.LoginSymbol.AND, masterField, operator, joinField);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(boolean sure, String masterField, Operator operator, String joinField) {
            if (sure) {
                this.addFieldCondition(masterField, operator, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(Condition.LoginSymbol loginSymbol, String masterField,
                                                      Operator operator, String joinField) {
            this.checkAllEntityTable();
            this.conditions.add(new FieldCompareCondition(loginSymbol, (EntityTable) this.join.getTable(), masterField,
                    operator, (EntityTable) this.join.getJoinTable(), joinField));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addFieldCondition(boolean sure, Condition.LoginSymbol loginSymbol, String masterField,
                                                      Operator operator, String joinField) {
            if (sure) {
                return this.addFieldCondition(loginSymbol, masterField, operator, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(String masterColumn, String joinColumn) {
            this.conditions.add(new ColumnCompareCondition(this.join.getTable(), masterColumn, Operator.eq,
                    this.join.getJoinTable(), joinColumn));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(boolean sure, String masterColumn, String joinColumn) {
            if (sure) {
                return this.addColumnCondition(masterColumn, joinColumn);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(Condition.LoginSymbol loginSymbol, String masterColumn,
                                                       String joinColumn) {
            return this.addFieldCondition(loginSymbol, masterColumn, Operator.eq, joinColumn);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                                       String masterColumn, String joinColumn) {
            if (sure) {
                return this.addColumnCondition(loginSymbol, masterColumn, joinColumn);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(String masterColumn, Operator operator, String joinColumn) {
            return this.addFieldCondition(Condition.LoginSymbol.AND, masterColumn, operator, joinColumn);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(boolean sure, String masterColumn, Operator operator,
                                                       String joinColumn) {
            if (sure) {
                return this.addColumnCondition(masterColumn, operator, joinColumn);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(Condition.LoginSymbol loginSymbol, String masterColumn,
                                                       Operator operator, String joinColumn) {
            this.conditions.add(new ColumnCompareCondition(loginSymbol, this.join.getTable(), masterColumn, operator,
                    this.join.getJoinTable(), joinColumn));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinBuilder<Builder> addColumnCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                                       String masterColumn, Operator operator, String joinColumn) {
            if (sure) {
                return this.addColumnCondition(loginSymbol, masterColumn, operator, joinColumn);
            }
            return this;
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(Condition.LoginSymbol loginSymbol) {
            LinkedList<Condition> conditions = new LinkedList<>();
            GroupCondition condition = new GroupCondition(conditions, loginSymbol);
            this.conditions.add(condition);
            return new JoinBuilder<>(this, this.join);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition() {
            return this.groupCondition(Condition.LoginSymbol.AND);
        }
    }
}
