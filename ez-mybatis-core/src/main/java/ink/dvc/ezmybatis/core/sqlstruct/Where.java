package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.constant.DbType;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.GroupCondition;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
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
        CONVERT.put(DbType.DM, defaultConvert);
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

    public static class WhereConditionBuilder<Builder> extends ConditionBuilder<Builder,
            WhereConditionBuilder<Builder>> {
        public WhereConditionBuilder(Builder builder, List<Condition> conditions, Table table) {
            super(builder, conditions, table);
            this.sonBuilder = this;
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
