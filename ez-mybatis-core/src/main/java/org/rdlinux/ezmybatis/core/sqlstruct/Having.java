package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Having implements SqlStruct {
    /**
     * 条件
     */
    private List<Condition> conditions;

    public Having(List<Condition> conditions) {
        this.conditions = conditions;
    }


    public static class HavingBuilder<Builder> extends ConditionBuilder<Builder, HavingBuilder<Builder>> {

        public HavingBuilder(Builder builder, Having having, Table table) {
            super(builder, having.getConditions(), table, table);
            this.sonBuilder = this;
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Operator operator, Operand value) {
            return this.addCondition(sure, logicalOperator, Alias.of(alias), operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias,
                                                        Operator operator, Operand value) {
            return this.addAliasCondition(true, logicalOperator, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Operand value) {
            return this.addAliasCondition(sure, logicalOperator, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias, Operand value) {
            return this.addAliasCondition(true, logicalOperator, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator, Operand value) {
            return this.addAliasCondition(sure, LogicalOperator.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, Operand value) {
            return this.addAliasCondition(true, LogicalOperator.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Operand value) {
            return this.addAliasCondition(true, LogicalOperator.AND, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Operator operator, Object value) {
            return this.addCondition(sure, logicalOperator, Alias.of(alias), operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias,
                                                        Operator operator, Object value) {
            return this.addAliasCondition(true, logicalOperator, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Object value) {
            return this.addAliasCondition(sure, logicalOperator, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias, Object value) {
            return this.addAliasCondition(true, logicalOperator, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator, Object value) {
            return this.addAliasCondition(sure, LogicalOperator.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, Object value) {
            return this.addAliasCondition(true, LogicalOperator.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Object value) {
            return this.addAliasCondition(true, LogicalOperator.AND, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Object value) {
            return this.addAliasCondition(sure, LogicalOperator.AND, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, LogicalOperator logicalOperator,
                                                              String alias) {
            return this.addIsNullCondition(sure, logicalOperator, Alias.of(alias));
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, String alias) {
            return this.addAliasIsNullCondition(sure, LogicalOperator.AND, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(LogicalOperator logicalOperator, String alias) {
            return this.addAliasIsNullCondition(true, logicalOperator, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(String alias) {
            return this.addAliasIsNullCondition(true, LogicalOperator.AND, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, LogicalOperator logicalOperator,
                                                                 String alias) {
            return this.addIsNotNullCondition(sure, logicalOperator, Alias.of(alias));
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, String alias) {
            return this.addAliasIsNotNullCondition(sure, LogicalOperator.AND, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(LogicalOperator logicalOperator, String alias) {
            return this.addAliasIsNotNullCondition(true, logicalOperator, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(String alias) {
            return this.addAliasIsNotNullCondition(true, LogicalOperator.AND, alias);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                          Operand minValue, Operand maxValue) {
            return this.addBtCondition(sure, logicalOperator, Alias.of(alias), minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, String alias, Operand minValue, Operand maxValue) {
            return this.addAliasBtCondition(sure, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(LogicalOperator logicalOperator, String alias, Operand minValue,
                                                          Operand maxValue) {
            return this.addAliasBtCondition(true, logicalOperator, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(String alias, Operand minValue, Operand maxValue) {
            return this.addAliasBtCondition(true, LogicalOperator.AND, alias, minValue, maxValue);
        }


        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                          Object minValue, Object maxValue) {
            return this.addBtCondition(sure, logicalOperator, Alias.of(alias),
                    ConditionBuilder.valueToArg(minValue), ConditionBuilder.valueToArg(maxValue));
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(LogicalOperator logicalOperator, String alias,
                                                          Object minValue, Object maxValue) {
            return this.addAliasBtCondition(true, logicalOperator, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(String alias, Object minValue, Object maxValue) {
            return this.addAliasBtCondition(true, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, String alias, Object minValue,
                                                          Object maxValue) {
            return this.addAliasBtCondition(sure, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, LogicalOperator logicalOperator,
                                                             String alias, Operand minValue, Operand maxValue) {
            return this.addNotBtCondition(sure, logicalOperator, Alias.of(alias), minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, String alias, Operand minValue, Operand maxValue) {
            return this.addAliasNotBtCondition(sure, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(LogicalOperator logicalOperator, String alias,
                                                             Operand minValue, Operand maxValue) {
            return this.addAliasNotBtCondition(true, logicalOperator, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(String alias, Operand minValue, Operand maxValue) {
            return this.addAliasNotBtCondition(true, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, LogicalOperator logicalOperator,
                                                             String alias, Object minValue, Object maxValue) {
            return this.addNotBtCondition(sure, logicalOperator, Alias.of(alias),
                    ConditionBuilder.valueToArg(minValue), ConditionBuilder.valueToArg(maxValue));
        }


        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(LogicalOperator logicalOperator, String alias,
                                                             Object minValue, Object maxValue) {
            return this.addAliasNotBtCondition(true, logicalOperator, alias, minValue, maxValue);
        }


        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(String alias, Object minValue, Object maxValue) {
            return this.addAliasNotBtCondition(true, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, String alias, Object minValue,
                                                             Object maxValue) {
            return this.addAliasNotBtCondition(sure, LogicalOperator.AND, alias, minValue, maxValue);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(boolean sure, LogicalOperator logicalOperator) {
            GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), logicalOperator);
            this.conditions.add(condition);
            return new HavingBuilder<>(this, new Having(condition.getConditions()), this.table);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(LogicalOperator logicalOperator) {
            return this.groupCondition(true, logicalOperator);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition() {
            return this.groupCondition(LogicalOperator.AND);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(boolean sure) {
            return this.groupCondition(sure, LogicalOperator.AND);
        }
    }
}
