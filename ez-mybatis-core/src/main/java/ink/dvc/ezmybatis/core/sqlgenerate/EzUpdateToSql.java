package ink.dvc.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.EzUpdate;

import java.util.List;
import java.util.Map;

public interface EzUpdateToSql {
    /**
     * @param mybatisParam mybatis参数
     */
    String toSql(Configuration configuration, EzUpdate update, Map<String, Object> mybatisParam);

    /**
     * @param mybatisParam mybatis参数
     */
    String toSql(Configuration configuration, List<EzUpdate> updates, Map<String, Object> mybatisParam);
}
