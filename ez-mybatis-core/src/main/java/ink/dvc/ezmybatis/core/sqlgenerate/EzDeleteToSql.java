package ink.dvc.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzDelete;

import java.util.List;
import java.util.Map;

public interface EzDeleteToSql {
    /**
     * @param mybatisParam mybatis参数
     */
    String toSql(Configuration configuration, EzDelete delete, Map<String, Object> mybatisParam);

    /**
     * @param mybatisParam mybatis参数
     */
    String toSql(Configuration configuration, List<EzDelete> deletes, Map<String, Object> mybatisParam);
}
