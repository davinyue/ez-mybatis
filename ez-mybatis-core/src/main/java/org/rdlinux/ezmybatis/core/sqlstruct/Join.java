package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.CompareColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.CompareFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class Join implements SqlStruct {
    /**
     * 主表
     */
    private EntityTable table;
    /**
     * 关联类型
     */
    private JoinType joinType;
    /**
     * 被join表
     */
    private EntityTable joinTable;
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
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        EntityClassInfo jEtInfo = EzEntityClassInfoFactory.forClass(configuration, this.joinTable.getEtType());
        StringBuilder sonSql;
        if (this.joinType == Join.JoinType.CrossJoin) {
            sonSql = new StringBuilder();
        } else {
            sonSql = Where.conditionsToSqlPart(new StringBuilder(), configuration, mybatisParamHolder,
                    this.getOnConditions());
            if (sonSql.length() == 0) {
                return sqlBuilder;
            }
        }
        if (this.joinType == Join.JoinType.InnerJoin) {
            sqlBuilder.append(" INNER JOIN ").append(keywordQM).append(jEtInfo.getTableName())
                    .append(keywordQM).append(" ").append(this.joinTable.getAlias()).append(" ON ");
        } else if (this.joinType == Join.JoinType.LeftJoin) {
            sqlBuilder.append(" LEFT JOIN ").append(keywordQM).append(jEtInfo.getTableName())
                    .append(keywordQM).append(" ").append(this.joinTable.getAlias()).append(" ON ");
        } else if (this.joinType == Join.JoinType.RightJoin) {
            sqlBuilder.append(" RIGHT JOIN ").append(keywordQM).append(jEtInfo.getTableName())
                    .append(keywordQM).append(" ").append(this.joinTable.getAlias()).append(" ON ");
        } else if (this.joinType == Join.JoinType.FullJoin) {
            sqlBuilder.append(" FULL JOIN ").append(keywordQM).append(jEtInfo.getTableName())
                    .append(keywordQM).append(" ").append(this.joinTable.getAlias()).append(" ON ");
        } else if (this.joinType == Join.JoinType.CrossJoin) {
            sqlBuilder.append(", ").append(keywordQM).append(jEtInfo.getTableName())
                    .append(keywordQM).append(" ").append(this.joinTable.getAlias());
        }
        if (this.getJoins() != null && !this.getJoins().isEmpty()) {
            for (Join join : this.joins) {
                sqlBuilder.append(join.joinToSql(new StringBuilder(), configuration, mybatisParamHolder));
            }
        }
        sqlBuilder.append(sonSql);
        return sqlBuilder;
    }

    /**
     * join类型枚举
     */
    public static enum JoinType {
        LeftJoin, RightJoin, FullJoin, InnerJoin, CrossJoin;
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
            this.join.setJoinType(Join.JoinType.InnerJoin);
            this.join.setJoinTable(joinTable);
        }

        public JoinBuilder<T> type(Join.JoinType type) {
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

    public static class JoinConditionBuilder<Builder> extends Where.WhereConditionBuilder<Builder> {
        private List<Condition> conditions;
        private EntityTable masterTable;
        private EntityTable joinTable;

        public JoinConditionBuilder(Builder builder, List<Condition> conditions, EntityTable masterTable,
                                    EntityTable joinTable) {
            super(builder, conditions, masterTable);
            this.conditions = conditions;
            this.masterTable = masterTable;
            this.joinTable = joinTable;
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
            this.conditions.add(new CompareFieldCondition(loginSymbol, this.masterTable, masterField, operator,
                    this.joinTable, joinField));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> add(boolean sure, Condition.LoginSymbol loginSymbol, String masterField,
                                                 Operator operator, String joinField) {
            if (sure) {
                this.add(loginSymbol, masterField, operator, joinField);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(String masterColumn, String joinColumn) {
            return this.add(masterColumn, Operator.eq, joinColumn);
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(boolean sure, String masterColumn, String joinColumn) {
            if (sure) {
                this.addColumn(masterColumn, joinColumn);
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
                this.addColumn(loginSymbol, masterColumn, joinColumn);
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
                this.addColumn(masterColumn, operator, joinColumn);
            }
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(Condition.LoginSymbol loginSymbol, String masterColumn,
                                                       Operator operator, String joinColumn) {
            this.conditions.add(new CompareColumnCondition(loginSymbol, this.masterTable, masterColumn, operator,
                    this.joinTable, joinColumn));
            return this;
        }

        /**
         * 添加联表条件, 使用主表和被联表的实体属性
         */
        public JoinConditionBuilder<Builder> addColumn(boolean sure, Condition.LoginSymbol loginSymbol,
                                                       String masterColumn, Operator operator, String joinColumn) {
            if (sure) {
                this.addColumn(loginSymbol, masterColumn, operator, joinColumn);
            }
            return this;
        }
    }
}
