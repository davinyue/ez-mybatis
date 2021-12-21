package ink.dvc.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzDelete;

import java.util.List;
import java.util.Map;

public interface DeleteSqlGenerate {
    String getDeleteByIdSql(Configuration configuration, Class<?> ntClass, Object id);

    String getBatchDeleteByIdSql(Configuration configuration, Class<?> ntClass, List<?> ids);

    String getDeleteSql(Configuration configuration, EzDelete delete, Map<String, Object> mybatisParam);

    String getDeleteSql(Configuration configuration, List<EzDelete> deletes, Map<String, Object> mybatisParam);
}
