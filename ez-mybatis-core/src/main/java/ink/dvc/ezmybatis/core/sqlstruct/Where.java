package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.constant.DbType;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.GroupCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.condition.between.*;
import ink.dvc.ezmybatis.core.sqlstruct.condition.compare.CompareAliasCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.nil.*;
import ink.dvc.ezmybatis.core.sqlstruct.condition.normal.NormalAliasCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.normal.NormalColumnCondition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * where条件
 */
@Getter
@Setter
public class Where implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                Where.defaultWhereToSql(sqlBuilder, configuration, (EzParam<?>) ezParam, mybatisParamHolder);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
    }

    /**
     * 条件
     */
    List<Condition> conditions;

    public Where(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public static StringBuilder conditionsToSqlPart(StringBuilder sqlBuilder, Configuration configuration,
                                                    MybatisParamHolder mybatisParamHolder,
                                                    List<Condition> conditions) {
        for (int i = 0; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            if (i != 0) {
                sqlBuilder.append(condition.getLoginSymbol().name()).append(" ");
            }
            sqlBuilder.append(condition.toSqlPart(configuration, mybatisParamHolder));
        }
        return sqlBuilder;
    }

    private static StringBuilder defaultWhereToSql(StringBuilder sqlBuilder, Configuration configuration,
                                                   EzParam<?> ezParam, MybatisParamHolder mybatisParamHolder) {
        if (ezParam.getWhere() == null || ezParam.getWhere().getConditions() == null ||
                ezParam.getWhere().getConditions().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" WHERE ");
        conditionsToSqlPart(sqlBuilder, configuration, mybatisParamHolder, ezParam.getWhere().getConditions());
        return sqlBuilder;
    }

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
    }

    public static class WhereBuilder<T> {
        private T target;
        private EntityTable table;
        private Where where;

        public WhereBuilder(T target, Where where, EntityTable table) {
            this.target = target;
            this.where = where;
            this.table = table;
        }

        public WhereConditionBuilder<WhereBuilder<T>> conditions() {
            return new WhereConditionBuilder<>(this, this.where.getConditions(), this.table);
        }

        public T done() {
            return this.target;
        }
    }

    public static class WhereConditionBuilder<Builder> {
        private Builder builder;
        private List<Condition> conditions;
        private EntityTable table;

        public WhereConditionBuilder(Builder builder, List<Condition> conditions, EntityTable table) {
            this.builder = builder;
            this.conditions = conditions;
            this.table = table;
        }

        public Builder done() {
            return this.builder;
        }

        public WhereConditionBuilder<Builder> add(Condition.LoginSymbol loginSymbol, EntityTable table,
                                                  String field, Operator operator, Object value) {
            this.conditions.add(new NormalFieldCondition(loginSymbol, table, field, operator, value));
            return this;
        }

        public WhereConditionBuilder<Builder> add(boolean sure, Condition.LoginSymbol loginSymbol, EntityTable table,
                                                  String field, Operator operator, Object value) {
            if (sure) {
                this.add(loginSymbol, table, field, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(Condition.LoginSymbol loginSymbol, EntityTable table,
                                                        String column, Operator operator, Object value) {
            this.conditions.add(new NormalColumnCondition(loginSymbol, table, column, operator, value));
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, Condition.LoginSymbol loginSymbol,
                                                        EntityTable table, String column, Operator operator,
                                                        Object value) {
            if (sure) {
                this.addColumn(loginSymbol, table, column, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(Condition.LoginSymbol loginSymbol, String alias,
                                                       Operator operator, Object value) {
            this.conditions.add(new NormalAliasCondition(loginSymbol, alias, operator, value));
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                       Operator operator, Object value) {
            if (sure) {
                this.addAlias(loginSymbol, alias, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> add(Condition.LoginSymbol loginSymbol, EntityTable table,
                                                  String field, Object value) {
            return this.add(loginSymbol, table, field, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> add(boolean sure, Condition.LoginSymbol loginSymbol, EntityTable table,
                                                  String field, Object value) {
            if (sure) {
                this.add(loginSymbol, table, field, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(Condition.LoginSymbol loginSymbol, EntityTable table,
                                                        String column, Object value) {
            return this.addColumn(loginSymbol, table, column, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, Condition.LoginSymbol loginSymbol,
                                                        EntityTable table, String column, Object value) {
            if (sure) {
                this.addColumn(loginSymbol, table, column, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(Condition.LoginSymbol loginSymbol, String alias,
                                                       Object value) {
            return this.addAlias(loginSymbol, alias, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> addAlias(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                       Object value) {
            if (sure) {
                this.addAlias(loginSymbol, alias, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> add(EntityTable table, String field,
                                                  Operator operator, Object value) {
            return this.add(Condition.LoginSymbol.AND, table, field, operator, value);
        }

        public WhereConditionBuilder<Builder> add(boolean sure, EntityTable table, String field,
                                                  Operator operator, Object value) {
            if (sure) {
                this.add(table, field, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(EntityTable table, String column,
                                                        Operator operator, Object value) {
            return this.addColumn(Condition.LoginSymbol.AND, table, column, operator, value);
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, EntityTable table, String column,
                                                        Operator operator, Object value) {
            if (sure) {
                this.addColumn(table, column, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> add(EntityTable table, String field, Object value) {
            return this.add(Condition.LoginSymbol.AND, table, field, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> add(boolean sure, EntityTable table, String field, Object value) {
            if (sure) {
                this.add(table, field, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(EntityTable table, String column, Object value) {
            return this.addColumn(Condition.LoginSymbol.AND, table, column, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, EntityTable table, String column, Object value) {
            if (sure) {
                this.addColumn(table, column, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(String alias, Object value) {
            return this.addAlias(Condition.LoginSymbol.AND, alias, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> addAlias(boolean sure, String alias, Object value) {
            if (sure) {
                this.addAlias(alias, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> add(Condition.LoginSymbol loginSymbol, String field,
                                                  Operator operator, Object value) {
            return this.add(loginSymbol, this.table, field, operator, value);
        }

        public WhereConditionBuilder<Builder> add(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                                  Operator operator, Object value) {
            if (sure) {
                this.add(loginSymbol, field, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(Condition.LoginSymbol loginSymbol, String column,
                                                        Operator operator, Object value) {
            return this.addColumn(loginSymbol, this.table, column, operator, value);
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, Condition.LoginSymbol loginSymbol, String column,
                                                        Operator operator, Object value) {
            if (sure) {
                this.addColumn(loginSymbol, column, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> add(Condition.LoginSymbol loginSymbol, String field,
                                                  Object value) {
            return this.add(loginSymbol, field, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> add(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                                  Object value) {
            if (sure) {
                this.add(loginSymbol, field, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(Condition.LoginSymbol loginSymbol, String column,
                                                        Object value) {
            return this.addColumn(loginSymbol, column, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, Condition.LoginSymbol loginSymbol, String column,
                                                        Object value) {
            if (sure) {
                this.addColumn(loginSymbol, column, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> add(String field, Operator operator, Object value) {
            return this.add(Condition.LoginSymbol.AND, field, operator, value);
        }

        public WhereConditionBuilder<Builder> add(boolean sure, String field, Operator operator, Object value) {
            if (sure) {
                this.add(field, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(String column, Operator operator, Object value) {
            return this.addColumn(Condition.LoginSymbol.AND, column, operator, value);
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, String column, Operator operator, Object value) {
            if (sure) {
                this.addColumn(column, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(String alias, Operator operator, Object value) {
            return this.addAlias(Condition.LoginSymbol.AND, alias, operator, value);
        }

        public WhereConditionBuilder<Builder> addAlias(boolean sure, String alias, Operator operator, Object value) {
            if (sure) {
                this.addAlias(alias, operator, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> add(String field, Object value) {
            return this.add(Condition.LoginSymbol.AND, field, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> add(boolean sure, String field, Object value) {
            if (sure) {
                this.add(field, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addColumn(String column, Object value) {
            return this.addColumn(Condition.LoginSymbol.AND, column, Operator.eq, value);
        }

        public WhereConditionBuilder<Builder> addColumn(boolean sure, String column, Object value) {
            if (sure) {
                this.addColumn(column, value);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(Condition.LoginSymbol loginSymbol, String alias,
                                                       Operator operator, String otherAlias) {
            this.conditions.add(new CompareAliasCondition(loginSymbol, alias, operator, otherAlias));
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                       Operator operator, String otherAlias) {
            if (sure) {
                this.addAlias(loginSymbol, alias, operator, otherAlias);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(String alias, Operator operator, String otherAlias) {
            this.conditions.add(new CompareAliasCondition(Condition.LoginSymbol.AND, alias, operator, otherAlias));
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(boolean sure, String alias, Operator operator,
                                                       String otherAlias) {
            if (sure) {
                this.addAlias(alias, operator, otherAlias);
            }
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(String alias, String otherAlias) {
            this.conditions.add(new CompareAliasCondition(Condition.LoginSymbol.AND, alias, Operator.eq,
                    otherAlias));
            return this;
        }

        public WhereConditionBuilder<Builder> addAlias(boolean sure, String alias, String otherAlias) {
            if (sure) {
                this.addAlias(alias, otherAlias);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addIsNull(Condition.LoginSymbol loginSymbol, String field) {
            this.conditions.add(new IsNullFieldCondition(loginSymbol, this.table, field));
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addIsNull(boolean sure, Condition.LoginSymbol loginSymbol, String field) {
            if (sure) {
                this.addIsNull(loginSymbol, field);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNull(Condition.LoginSymbol loginSymbol, String column) {
            this.conditions.add(new IsNullColumnCondition(loginSymbol, this.table, column));
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNull(boolean sure, Condition.LoginSymbol loginSymbol,
                                                              String column) {
            if (sure) {
                this.addColumnIsNull(loginSymbol, column);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addAliasIsNull(Condition.LoginSymbol loginSymbol, String alias) {
            this.conditions.add(new IsNullAliasCondition(loginSymbol, alias));
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addAliasIsNull(boolean sure, Condition.LoginSymbol loginSymbol, String alias) {
            if (sure) {
                this.addAliasIsNull(loginSymbol, alias);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addIsNull(String field) {
            this.conditions.add(new IsNullFieldCondition(Condition.LoginSymbol.AND, this.table, field));
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addIsNull(boolean sure, String field) {
            if (sure) {
                this.addIsNull(field);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNull(String column) {
            this.conditions.add(new IsNullColumnCondition(Condition.LoginSymbol.AND, this.table, column));
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNull(boolean sure, String column) {
            if (sure) {
                this.addColumnIsNull(column);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addAliasIsNull(String alias) {
            this.conditions.add(new IsNullAliasCondition(Condition.LoginSymbol.AND, alias));
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addAliasIsNull(boolean sure, String alias) {
            if (sure) {
                this.addAliasIsNull(alias);
            }
            return this;
        }

        /**
         * 添加is not null条件
         */
        public WhereConditionBuilder<Builder> addIsNotNull(Condition.LoginSymbol loginSymbol, String field) {
            this.conditions.add(new IsNotNullFiledCondition(loginSymbol, this.table, field));
            return this;
        }

        /**
         * 添加is not null条件
         */
        public WhereConditionBuilder<Builder> addIsNotNull(boolean sure, Condition.LoginSymbol loginSymbol,
                                                           String field) {
            if (sure) {
                this.addIsNotNull(loginSymbol, field);
            }
            return this;
        }

        /**
         * 添加is not null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNotNull(Condition.LoginSymbol loginSymbol, String column) {
            this.conditions.add(new IsNotNullColumnCondition(loginSymbol, this.table, column));
            return this;
        }

        /**
         * 添加is not null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNotNull(boolean sure, Condition.LoginSymbol loginSymbol,
                                                                 String column) {
            if (sure) {
                this.addColumnIsNotNull(loginSymbol, column);
            }
            return this;
        }

        /**
         * 添加is not null条件
         */
        public WhereConditionBuilder<Builder> addIsNotNull(String field) {
            this.conditions.add(new IsNotNullFiledCondition(Condition.LoginSymbol.AND, this.table, field));
            return this;
        }

        /**
         * 添加is not null条件
         */
        public WhereConditionBuilder<Builder> addIsNotNull(boolean sure, String field) {
            if (sure) {
                this.addIsNotNull(field);
            }
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNotNull(String column) {
            this.conditions.add(new IsNotNullColumnCondition(Condition.LoginSymbol.AND, this.table, column));
            return this;
        }

        /**
         * 添加is null条件
         */
        public WhereConditionBuilder<Builder> addColumnIsNotNull(boolean sure, String column) {
            if (sure) {
                this.addColumnIsNotNull(column);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addBt(Condition.LoginSymbol loginSymbol, String field,
                                                    Object minValue, Object maxValue) {
            this.conditions.add(new BetweenFieldCondition(loginSymbol, this.table, field, minValue,
                    maxValue));
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addBt(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                                    Object minValue, Object maxValue) {
            if (sure) {
                this.addBt(loginSymbol, field, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addColumnBt(Condition.LoginSymbol loginSymbol, String column,
                                                          Object minValue, Object maxValue) {
            this.conditions.add(new BetweenColumnCondition(loginSymbol, this.table, column, minValue,
                    maxValue));
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addColumnBt(boolean sure, Condition.LoginSymbol loginSymbol, String column,
                                                          Object minValue, Object maxValue) {
            if (sure) {
                this.addColumnBt(loginSymbol, column, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addAliasBt(Condition.LoginSymbol loginSymbol, String alias,
                                                         Object minValue, Object maxValue) {
            this.conditions.add(new BetweenAliasCondition(loginSymbol, alias, minValue, maxValue));
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addAliasBt(boolean sure, Condition.LoginSymbol loginSymbol, String alias,
                                                         Object minValue, Object maxValue) {
            if (sure) {
                this.addAliasBt(loginSymbol, alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addBt(String field, Object minValue, Object maxValue) {
            this.conditions.add(new BetweenFieldCondition(Condition.LoginSymbol.AND, this.table, field,
                    minValue, maxValue));
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addBt(boolean sure, String field, Object minValue, Object maxValue) {
            if (sure) {
                this.addBt(field, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addColumnBt(String column, Object minValue, Object maxValue) {
            this.conditions.add(new BetweenColumnCondition(Condition.LoginSymbol.AND, this.table, column,
                    minValue, maxValue));
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addColumnBt(boolean sure, String column, Object minValue,
                                                          Object maxValue) {
            if (sure) {
                this.addColumnBt(column, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addAliasBt(String alias, Object minValue, Object maxValue) {
            this.conditions.add(new BetweenAliasCondition(Condition.LoginSymbol.AND, alias, minValue, maxValue));
            return this;
        }

        /**
         * 添加between on条件
         */
        public WhereConditionBuilder<Builder> addAliasBt(boolean sure, String alias, Object minValue, Object maxValue) {
            if (sure) {
                this.addAliasBt(alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addNotBt(Condition.LoginSymbol loginSymbol, String field,
                                                       Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenFieldCondition(loginSymbol, this.table, field, minValue,
                    maxValue));
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addNotBt(boolean sure, Condition.LoginSymbol loginSymbol, String field,
                                                       Object minValue, Object maxValue) {
            if (sure) {
                this.addNotBt(loginSymbol, field, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addColumnNotBt(Condition.LoginSymbol loginSymbol, String column,
                                                             Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenColumnCondition(loginSymbol, this.table, column, minValue,
                    maxValue));
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addColumnNotBt(boolean sure, Condition.LoginSymbol loginSymbol,
                                                             String column, Object minValue, Object maxValue) {
            if (sure) {
                this.addColumnNotBt(loginSymbol, column, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addAliasNotBt(Condition.LoginSymbol loginSymbol, String alias,
                                                            Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenAliasCondition(loginSymbol, alias, minValue, maxValue));
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addAliasNotBt(boolean sure, Condition.LoginSymbol loginSymbol,
                                                            String alias, Object minValue, Object maxValue) {
            if (sure) {
                this.addAliasNotBt(loginSymbol, alias, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addNotBt(String field, Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenFieldCondition(Condition.LoginSymbol.AND, this.table, field,
                    minValue, maxValue));
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addNotBt(boolean sure, String field, Object minValue, Object maxValue) {
            if (sure) {
                this.addNotBt(field, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addColumnNotBt(String column, Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenColumnCondition(Condition.LoginSymbol.AND, this.table, column,
                    minValue, maxValue));
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addColumnNotBt(boolean sure, String column, Object minValue,
                                                             Object maxValue) {
            if (sure) {
                this.addColumnNotBt(column, minValue, maxValue);
            }
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addAliasNotBt(String alias, Object minValue, Object maxValue) {
            this.conditions.add(new NotBetweenAliasCondition(Condition.LoginSymbol.AND, alias, minValue, maxValue));
            return this;
        }

        /**
         * 添加not between on条件
         */
        public WhereConditionBuilder<Builder> addAliasNotBt(boolean sure, String alias, Object minValue,
                                                            Object maxValue) {
            if (sure) {
                this.addAliasNotBt(alias, minValue, maxValue);
            }
            return this;
        }

        public WhereConditionBuilder<WhereConditionBuilder<Builder>> groupCondition(Condition.LoginSymbol loginSymbol) {
            LinkedList<Condition> conditions = new LinkedList<>();
            GroupCondition condition = new GroupCondition(conditions, loginSymbol);
            this.conditions.add(condition);
            return new WhereConditionBuilder<>(this, conditions, this.table);
        }

        public WhereConditionBuilder<WhereConditionBuilder<Builder>> groupCondition() {
            return this.groupCondition(Condition.LoginSymbol.AND);
        }
    }
}
