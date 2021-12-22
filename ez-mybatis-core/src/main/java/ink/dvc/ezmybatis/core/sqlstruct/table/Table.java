package ink.dvc.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;

public interface Table {
    default String getPartition() {
        return null;
    }

    String getAlias();

    String getTableName(Configuration configuration);
}
