package ink.dvc.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;

import java.util.List;

public interface InsertSqlGenerate {
    String getInsertSql(Configuration configuration, Object entity);

    String getBatchInsertSql(Configuration configuration, List<Object> entitys);
}
