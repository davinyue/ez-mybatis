package ink.dvc.ezmybatis.core.sqlgenerate;

import ink.dvc.ezmybatis.core.sqlgenerate.mysql.MySqlSqlGenerate;
import ink.dvc.ezmybatis.core.sqlgenerate.oracle.OracleSqlGenerate;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.constant.DbType;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;

import java.util.HashMap;
import java.util.Map;

public class SqlGenerateFactory {
    private static final Map<DbType, SqlGenerate> sqlGenerateMap = new HashMap<>();


    static {
        sqlGenerateMap.put(DbType.MYSQL, MySqlSqlGenerate.getInstance());
        sqlGenerateMap.put(DbType.ORACLE, OracleSqlGenerate.getInstance());
    }

    public static SqlGenerate getSqlGenerate(Configuration configuration) {
        return sqlGenerateMap.get(DbTypeUtils.getDbType(configuration));
    }
}
