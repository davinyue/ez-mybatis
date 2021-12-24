package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.constant.DbType;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.GroupCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.condition.between.BetweenAliasCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.between.NotBetweenAliasCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.compare.AliasCompareCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.nil.IsNotNullAliasCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.nil.IsNullAliasCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.normal.NormalAliasCondition;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

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
        private Having having;

        public HavingBuilder(Builder builder, Having having, Table table) {
            super(builder, having.getConditions(), table, table);
            this.sonBuilder = this;
            this.having = having;
        }

        public HavingBuilder<Builder> addAliasCondition(Condition.LoginSymbol loginSymbol, String alias,
                                                        Operator operator, Object value) {
            this.conditions.add(new NormalAliasCondition(loginSymbol, alias, operator, value));
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                        Operator operator, Object value) {
            if (sure) {
                return this.addAliasCondition(loginSymbol, alias, operator, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(Condition.LoginSymbol loginSymbol, String alias,
                                                        Object value) {
            return this.addAliasCondition(loginSymbol, alias, Operator.eq, value);
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                        Object value) {
            if (sure) {
                return this.addAliasCondition(loginSymbol, alias, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(String alias, Object value) {
            return this.addAliasCondition(Condition.LoginSymbol.AND, alias, Operator.eq, value);
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Object value) {
            if (sure) {
                return this.addAliasCondition(alias, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, Object value) {
            return this.addAliasCondition(Condition.LoginSymbol.AND, alias, operator, value);
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, String alias, Operator operator, Object value) {
            if (sure) {
                return this.addAliasCondition(alias, operator, value);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(Condition.LoginSymbol loginSymbol, String alias,
                                                        Operator operator, String otherAlias) {
            this.conditions.add(new AliasCompareCondition(loginSymbol, alias, operator, otherAlias));
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                        Operator operator, String otherAlias) {
            if (sure) {
                return this.addAliasCondition(loginSymbol, alias, operator, otherAlias);
            }
            return this;
        }

        public HavingBuilder<Builder> addAliasCondition(String alias, Operator operator, String otherAlias) {
            this.conditions.add(new AliasCompareCondition(Condition.LoginSymbol.AND, alias, operator, otherAlias));
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
            this.conditions.add(new AliasCompareCondition(Condition.LoginSymbol.AND, alias, Operator.eq,
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
        public HavingBuilder<Builder> addAliasIsNullCondition(Condition.LoginSymbol loginSymbol, String alias) {
            this.conditions.add(new IsNullAliasCondition(loginSymbol, alias));
            return this;
        }

        /**
         * 添加is null条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                                              String alias) {
            if (sure) {
                return this.addAliasIsNullCondition(loginSymbol, alias);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public HavingBuilder<Builder> addAliasIsNullCondition(String alias) {
            this.conditions.add(new IsNullAliasCondition(Condition.LoginSymbol.AND, alias));
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
        public HavingBuilder<Builder> addAliasIsNotNullCondition(Condition.LoginSymbol loginSymbol, String alias) {
            this.conditions.add(new IsNotNullAliasCondition(loginSymbol, alias));
            return this;
        }

        /**
         * 添加is not null条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                                                 String alias) {
            if (sure) {
                return this.addAliasIsNotNullCondition(loginSymbol, alias);
            }
            return this;
        }

        /**
         * 添加is not null条件
         */
        public HavingBuilder<Builder> addAliasIsNotNullCondition(String alias) {
            this.conditions.add(new IsNotNullAliasCondition(Condition.LoginSymbol.AND, alias));
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
        public HavingBuilder<Builder> addAliasBtCondition(Condition.LoginSymbol loginSymbol, String alias,
                                                          Object minValue, Object maxValue) {
            this.conditions.add(new BetweenAliasCondition(loginSymbol, alias, minValue, maxValue));
            return this;
        }

        /**
         * 添加between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                          Object minValue, Object maxValue) {
            if (sure) {
                return this.addAliasBtCondition(loginSymbol, alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public HavingBuilder<Builder> addAliasBtCondition(String alias, Object minValue, Object maxValue) {
            this.conditions.add(new BetweenAliasCondition(Condition.LoginSymbol.AND, alias, minValue, maxValue));
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
        public HavingBuilder<Builder> addAliasNotBtCondition(Condition.LoginSymbol loginSymbol, String alias,
                                                             Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenAliasCondition(loginSymbol, alias, minValue, maxValue));
            return this;
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(boolean sure, Condition.LoginSymbol loginSymbol,
                                                             String alias, Object minValue, Object maxValue) {
            if (sure) {
                return this.addAliasNotBtCondition(loginSymbol, alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public HavingBuilder<Builder> addAliasNotBtCondition(String alias, Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenAliasCondition(Condition.LoginSymbol.AND, alias, minValue, maxValue));
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

        public HavingBuilder<HavingBuilder<Builder>> groupCondition(Condition.LoginSymbol loginSymbol) {
            LinkedList<Condition> conditions = new LinkedList<>();
            GroupCondition condition = new GroupCondition(conditions, loginSymbol);
            this.conditions.add(condition);
            return new HavingBuilder<>(this, this.having, this.table);
        }

        public HavingBuilder<HavingBuilder<Builder>> groupCondition() {
            return this.groupCondition(Condition.LoginSymbol.AND);
        }
    }
}
