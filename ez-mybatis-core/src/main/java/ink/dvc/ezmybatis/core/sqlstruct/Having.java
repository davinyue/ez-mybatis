package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.constant.DbType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Having extends Where {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                Having.defaultWhereToSql(sqlBuilder, configuration, (EzQuery<?>) ezParam, mybatisParamHolder);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
    }

    public Having(List<Condition> conditions) {
        super(conditions);
    }

    private static StringBuilder defaultWhereToSql(StringBuilder sqlBuilder, Configuration configuration,
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
}
