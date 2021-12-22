package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.constant.DbType;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class From implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                From.fromSql(sqlBuilder, configuration, (EzParam<?>) ezParam, mybatisParamHolder);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
        CONVERT.put(DbType.DM, defaultConvert);
    }

    private Table table;

    public From(Table table) {
        this.table = table;
    }

    private static StringBuilder fromSql(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> param,
                                         MybatisParamHolder mybatisParamHolder) {
        From from = param.getFrom();
        Table fromTable = from.getTable();
        sqlBuilder.append(" FROM ").append(fromTable.toSqlStruct(configuration));
        return sqlBuilder;
    }

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
    }
}
