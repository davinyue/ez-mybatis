package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class From implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                From.fromSql(sqlBuilder, configuration, mybatisParamHolder, (EzParam<?>) ezParam);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
        CONVERT.put(DbType.DM, defaultConvert);
    }

    private Table table;

    public From(Table table) {
        this.table = table;
    }

    private static StringBuilder fromSql(StringBuilder sqlBuilder, Configuration configuration,
                                         MybatisParamHolder paramHolder, EzParam<?> param) {
        From from = param.getFrom();
        Table fromTable = from.getTable();
        sqlBuilder.append(" FROM ").append(fromTable.toSqlStruct(configuration, paramHolder));
        return sqlBuilder;
    }

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
    }
}
