package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;

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
        public HavingBuilder<Builder> addAliasCondition(boolean sure, AndOr andOr, String alias,
                                                        Operator operator, Operand value) {
            return this.addCondition(sure, andOr, Alias.of(alias), operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(AndOr andOr, String alias,
                                                        Operator operator, Operand value) {
            return this.addAliasCondition(true, andOr, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, AndOr andOr, String alias,
                                                        Operand value) {
            return this.addAliasCondition(sure, andOr, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(AndOr andOr, String alias, Operand value) {
            return this.addAliasCondition(true, andOr, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator, Operand value) {
            return this.addAliasCondition(sure, AndOr.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, Operand value) {
            return this.addAliasCondition(true, AndOr.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Operand value) {
            return this.addAliasCondition(true, AndOr.AND, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, AndOr andOr, String alias,
                                                        Operator operator, Object value) {
            return this.addCondition(sure, andOr, Alias.of(alias), operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(AndOr andOr, String alias,
                                                        Operator operator, Object value) {
            return this.addAliasCondition(true, andOr, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, AndOr andOr, String alias,
                                                        Object value) {
            return this.addAliasCondition(sure, andOr, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(AndOr andOr, String alias, Object value) {
            return this.addAliasCondition(true, andOr, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator, Object value) {
            return this.addAliasCondition(sure, AndOr.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, Object value) {
            return this.addAliasCondition(true, AndOr.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Object value) {
            return this.addAliasCondition(true, AndOr.AND, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Object value) {
            return this.addAliasCondition(sure, AndOr.AND, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, AndOr andOr,
                                                              String alias) {
            return this.addIsNullCondition(sure, andOr, Alias.of(alias));
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, String alias) {
            return this.addAliasIsNullCondition(sure, AndOr.AND, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(AndOr andOr, String alias) {
            return this.addAliasIsNullCondition(true, andOr, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(String alias) {
            return this.addAliasIsNullCondition(true, AndOr.AND, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, AndOr andOr,
                                                                 String alias) {
            return this.addIsNotNullCondition(sure, andOr, Alias.of(alias));
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, String alias) {
            return this.addAliasIsNotNullCondition(sure, AndOr.AND, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(AndOr andOr, String alias) {
            return this.addAliasIsNotNullCondition(true, andOr, alias);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(String alias) {
            return this.addAliasIsNotNullCondition(true, AndOr.AND, alias);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, AndOr andOr, String alias,
                                                          Operand minValue, Operand maxValue) {
            return this.addBtCondition(sure, andOr, Alias.of(alias), minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, String alias, Operand minValue, Operand maxValue) {
            return this.addAliasBtCondition(sure, AndOr.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(AndOr andOr, String alias, Operand minValue,
                                                          Operand maxValue) {
            return this.addAliasBtCondition(true, andOr, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(String alias, Operand minValue, Operand maxValue) {
            return this.addAliasBtCondition(true, AndOr.AND, alias, minValue, maxValue);
        }


        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, AndOr andOr, String alias,
                                                          Object minValue, Object maxValue) {
            return this.addBtCondition(sure, andOr, Alias.of(alias),
                    ConditionBuilder.valueToArg(minValue), ConditionBuilder.valueToArg(maxValue));
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(AndOr andOr, String alias,
                                                          Object minValue, Object maxValue) {
            return this.addAliasBtCondition(true, andOr, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(String alias, Object minValue, Object maxValue) {
            return this.addAliasBtCondition(true, AndOr.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, String alias, Object minValue,
                                                          Object maxValue) {
            return this.addAliasBtCondition(sure, AndOr.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, AndOr andOr,
                                                             String alias, Operand minValue, Operand maxValue) {
            return this.addNotBtCondition(sure, andOr, Alias.of(alias), minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, String alias, Operand minValue, Operand maxValue) {
            return this.addAliasNotBtCondition(sure, AndOr.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(AndOr andOr, String alias,
                                                             Operand minValue, Operand maxValue) {
            return this.addAliasNotBtCondition(true, andOr, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(String alias, Operand minValue, Operand maxValue) {
            return this.addAliasNotBtCondition(true, AndOr.AND, alias, minValue, maxValue);
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, AndOr andOr,
                                                             String alias, Object minValue, Object maxValue) {
            return this.addNotBtCondition(sure, andOr, Alias.of(alias),
                    ConditionBuilder.valueToArg(minValue), ConditionBuilder.valueToArg(maxValue));
        }


        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(AndOr andOr, String alias,
                                                             Object minValue, Object maxValue) {
            return this.addAliasNotBtCondition(true, andOr, alias, minValue, maxValue);
        }


        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(String alias, Object minValue, Object maxValue) {
            return this.addAliasNotBtCondition(true, AndOr.AND, alias, minValue, maxValue);
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, String alias, Object minValue,
                                                             Object maxValue) {
            return this.addAliasNotBtCondition(sure, AndOr.AND, alias, minValue, maxValue);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(boolean sure, AndOr andOr) {
            GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), andOr);
            this.conditions.add(condition);
            return new HavingBuilder<>(this, new Having(condition.getConditions()), this.table);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(AndOr andOr) {
            return this.groupCondition(true, andOr);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition() {
            return this.groupCondition(AndOr.AND);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(boolean sure) {
            return this.groupCondition(sure, AndOr.AND);
        }
    }
}
