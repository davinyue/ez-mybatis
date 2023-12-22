package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.Arg;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.AliasCompareArgCondition;
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
                                                        Operator operator, Arg value) {
            if (sure) {
                if (value == null) {
                    this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, Operator.isNull));
                } else {
                    this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, operator, value));
                }
            }
            return this;
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias, Operator operator,
                                                        Arg value) {
            return this.addAliasCondition(true, logicalOperator, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias, Arg value) {
            return this.addAliasCondition(sure, logicalOperator, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias, Arg value) {
            return this.addAliasCondition(true, logicalOperator, alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator, Arg value) {
            return this.addAliasCondition(sure, LogicalOperator.AND, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, Arg value) {
            return this.addAliasCondition(true, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Arg value) {
            return this.addAliasCondition(alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Operator operator, Object value) {
            if (sure) {
                if (value == null) {
                    this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, Operator.isNull));
                } else if (value instanceof Arg) {
                    this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, operator, (Arg) value));
                } else {
                    if (operator == Operator.in || operator == Operator.notIn) {
                        List<Arg> args = ConditionBuilder.valueToArgList(value);
                        this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, operator, args));
                    } else {
                        this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, operator,
                                ObjArg.of(value)));
                    }
                }
            }
            return this;
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias, Operator operator,
                                                        Object value) {
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
            return this.addAliasCondition(true, alias, operator, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasCondition(String alias, Object value) {
            return this.addAliasCondition(alias, Operator.eq, value);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, LogicalOperator logicalOperator, String alias) {
            if (sure) {
                this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, Operator.isNull));
            }
            return this;
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
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, LogicalOperator logicalOperator, String alias) {
            if (sure) {
                this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, Operator.isNotNull));
            }
            return this;
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
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                          Arg minValue, Arg maxValue) {
            if (sure) {
                this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, Operator.between, minValue,
                        maxValue));
            }
            return this;
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, String alias, Arg minValue, Arg maxValue) {
            return this.addAliasBtCondition(sure, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(LogicalOperator logicalOperator, String alias, Arg minValue,
                                                          Arg maxValue) {
            return this.addAliasBtCondition(true, logicalOperator, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(String alias, Arg minValue, Arg maxValue) {
            return this.addAliasBtCondition(true, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                             Arg minValue, Arg maxValue) {
            if (sure) {
                this.conditions.add(new AliasCompareArgCondition(logicalOperator, alias, Operator.notBetween,
                        minValue, maxValue));
            }
            return this;
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, String alias, Arg minValue, Arg maxValue) {
            return this.addAliasNotBtCondition(sure, LogicalOperator.AND, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(LogicalOperator logicalOperator, String alias, Arg minValue,
                                                             Arg maxValue) {
            return this.addAliasNotBtCondition(true, logicalOperator, alias, minValue, maxValue);
        }

        /**
         * 添加alias条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(String alias, Arg minValue, Arg maxValue) {
            return this.addAliasNotBtCondition(true, LogicalOperator.AND, alias, minValue, maxValue);
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Object value) {
            if (sure) {
                return this.addAliasCondition(alias, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, String otherAlias) {
            if (sure) {
                return this.addAliasCondition(alias, otherAlias);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(LogicalOperator logicalOperator, String alias,
                                                          Object minValue, Object maxValue) {
            return this.addAliasBtCondition(logicalOperator, alias, ObjArg.of(minValue), ObjArg.of(maxValue));
        }

        /**
         * 添加between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                          Object minValue, Object maxValue) {
            if (sure) {
                return this.addAliasBtCondition(logicalOperator, alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(String alias, Object minValue, Object maxValue) {
            return this.addAliasBtCondition(alias, ObjArg.of(minValue), ObjArg.of(maxValue));
        }

        /**
         * 添加between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, String alias, Object minValue,
                                                          Object maxValue) {
            if (sure) {
                return this.addAliasBtCondition(alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(LogicalOperator logicalOperator, String alias,
                                                             Object minValue, Object maxValue) {
            return this.addAliasNotBtCondition(logicalOperator, alias, ObjArg.of(minValue), ObjArg.of(maxValue));
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, LogicalOperator logicalOperator,
                                                             String alias, Object minValue, Object maxValue) {
            if (sure) {
                return this.addAliasNotBtCondition(logicalOperator, alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(String alias, Object minValue, Object maxValue) {
            return this.addAliasNotBtCondition(alias, ObjArg.of(minValue), ObjArg.of(maxValue));
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, String alias, Object minValue,
                                                             Object maxValue) {
            if (sure) {
                return this.addAliasNotBtCondition(alias, minValue, maxValue);
            }
            return this;
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
