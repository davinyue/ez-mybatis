package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

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
        CONVERT.put(DbType.DM, defaultConvert);
    }

    /**
     * 条件
     */
    private List<Condition> conditions;

    public Where(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public static StringBuilder conditionsToSqlPart(StringBuilder sqlBuilder, Configuration configuration,
                                                    MybatisParamHolder mybatisParamHolder,
                                                    List<Condition> conditions) {
        boolean lastConditionEmpty = true;
        for (Condition condition : conditions) {
            String sqlPart = condition.toSqlPart(configuration, mybatisParamHolder);
            boolean emptySql = sqlPart.trim().isEmpty();
            if (!lastConditionEmpty && !emptySql) {
                sqlBuilder.append(condition.getLoginSymbol().name()).append(" ");
            }
            if (!emptySql) {
                lastConditionEmpty = false;
                sqlBuilder.append(sqlPart);
            } else {
                lastConditionEmpty = true;
            }
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

    public static class WhereBuilder<Builder> extends ConditionBuilder<Builder,
            WhereBuilder<Builder>> {

        public WhereBuilder(Builder builder, Where where, Table table) {
            super(builder, where.getConditions(), table, table);
            this.sonBuilder = this;
        }

        public WhereBuilder<WhereBuilder<Builder>> groupCondition(Condition.LoginSymbol loginSymbol) {
            GroupCondition condition = new GroupCondition(new LinkedList<>(), loginSymbol);
            this.conditions.add(condition);
            return new WhereBuilder<>(this, new Where(condition.getConditions()), this.table);
        }

        public WhereBuilder<WhereBuilder<Builder>> groupCondition() {
            return this.groupCondition(Condition.LoginSymbol.AND);
        }
    }

}
