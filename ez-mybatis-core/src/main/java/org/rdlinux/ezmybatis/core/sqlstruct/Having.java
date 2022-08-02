package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.NotBetweenAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.AliasCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNotNullAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
public class Having implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                Having.defaultHavingToSql(sqlBuilder, configuration, (EzQuery<?>) ezParam, mybatisParamHolder);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
        CONVERT.put(DbType.DM, defaultConvert);
    }

    /**
     * 条件
     */
    private List<Condition> conditions;

    public Having(List<Condition> conditions) {
        this.conditions = conditions;
    }

    private static StringBuilder defaultHavingToSql(StringBuilder sqlBuilder, Configuration configuration,
                                                    EzQuery<?> ezParam, MybatisParamHolder mybatisParamHolder) {
        if (ezParam.getHaving() == null || ezParam.getHaving().getConditions() == null ||
                ezParam.getHaving().getConditions().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" HAVING ");
        Where.conditionsToSqlPart(sqlBuilder, configuration, mybatisParamHolder, ezParam.getHaving().getConditions());
        return sqlBuilder;
    }

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
    }

    public static class HavingBuilder<Builder> extends ConditionBuilder<Builder, HavingBuilder<Builder>> {

        public HavingBuilder(Builder builder, Having having, Table table) {
            super(builder, having.getConditions(), table, table);
            this.sonBuilder = this;
        }

        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias,
                                                        Operator operator, Object value) {
            this.conditions.add(new NormalAliasCondition(logicalOperator, alias, operator, value));
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Operator operator, Object value) {
            if (sure) {
                return this.addAliasCondition(logicalOperator, alias, operator, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias,
                                                        Object value) {
            return this.addAliasCondition(logicalOperator, alias, Operator.eq, value);
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Object value) {
            if (sure) {
                return this.addAliasCondition(logicalOperator, alias, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(String alias, Object value) {
            return this.addAliasCondition(LogicalOperator.AND, alias, Operator.eq, value);
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Object value) {
            if (sure) {
                return this.addAliasCondition(alias, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, Object value) {
            return this.addAliasCondition(LogicalOperator.AND, alias, operator, value);
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator, Object value) {
            if (sure) {
                return this.addAliasCondition(alias, operator, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(LogicalOperator logicalOperator, String alias,
                                                        Operator operator, String otherAlias) {
            this.conditions.add(new AliasCompareCondition(logicalOperator, alias, operator, otherAlias));
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, LogicalOperator logicalOperator, String alias,
                                                        Operator operator, String otherAlias) {
            if (sure) {
                return this.addAliasCondition(logicalOperator, alias, operator, otherAlias);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, String otherAlias) {
            this.conditions.add(new AliasCompareCondition(LogicalOperator.AND, alias, operator, otherAlias));
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator,
                                                        String otherAlias) {
            if (sure) {
                return this.addAliasCondition(alias, operator, otherAlias);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(String alias, String otherAlias) {
            this.conditions.add(new AliasCompareCondition(LogicalOperator.AND, alias, Operator.eq,
                    otherAlias));
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, String otherAlias) {
            if (sure) {
                return this.addAliasCondition(alias, otherAlias);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(LogicalOperator logicalOperator, String alias) {
            this.conditions.add(new IsNullAliasCondition(logicalOperator, alias));
            return this;
        }

        /**
         * 添加is null条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, LogicalOperator logicalOperator,
                                                              String alias) {
            if (sure) {
                return this.addAliasIsNullCondition(logicalOperator, alias);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(String alias) {
            this.conditions.add(new IsNullAliasCondition(LogicalOperator.AND, alias));
            return this;
        }

        /**
         * 添加is null条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, String alias) {
            if (sure) {
                return this.addAliasIsNullCondition(alias);
            }
            return this;
        }

        /**
         * 添加is not null条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(LogicalOperator logicalOperator, String alias) {
            this.conditions.add(new IsNotNullAliasCondition(logicalOperator, alias));
            return this;
        }

        /**
         * 添加is not null条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, LogicalOperator logicalOperator,
                                                                 String alias) {
            if (sure) {
                return this.addAliasIsNotNullCondition(logicalOperator, alias);
            }
            return this;
        }

        /**
         * 添加is not null条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(String alias) {
            this.conditions.add(new IsNotNullAliasCondition(LogicalOperator.AND, alias));
            return this;
        }

        /**
         * 添加is not null条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, String alias) {
            if (sure) {
                return this.addAliasIsNotNullCondition(alias);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(LogicalOperator logicalOperator, String alias,
                                                          Object minValue, Object maxValue) {
            this.conditions.add(new BetweenAliasCondition(logicalOperator, alias, minValue, maxValue));
            return this;
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
            this.conditions.add(new BetweenAliasCondition(LogicalOperator.AND, alias, minValue, maxValue));
            return this;
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
            this.conditions.add(new NotBetweenAliasCondition(logicalOperator, alias, minValue, maxValue));
            return this;
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
            this.conditions.add(new NotBetweenAliasCondition(LogicalOperator.AND, alias, minValue, maxValue));
            return this;
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

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(LogicalOperator logicalOperator) {
            GroupCondition condition = new GroupCondition(new LinkedList<>(), logicalOperator);
            this.conditions.add(condition);
            return new HavingBuilder<>(this, new Having(condition.getConditions()), this.table);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition() {
            return this.groupCondition(LogicalOperator.AND);
        }
    }
}
