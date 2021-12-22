package ink.dvc.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;

import java.util.List;

public abstract class AbstractDeleteSqlGenerate implements DeleteSqlGenerate {
    private SelectSqlGenerate selectSqlGenerate;

    public AbstractDeleteSqlGenerate(SelectSqlGenerate selectSqlGenerate) {
        this.selectSqlGenerate = selectSqlGenerate;
    }

    @Override
    public String getDeleteByIdSql(Configuration configuration, Class<?> ntClass, Object id) {
        return this.selectSqlGenerate.getSelectByIdSql(configuration, ntClass, id)
                .replaceAll("SELECT \\*", "DELETE");
    }


    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, Class<?> ntClass, List<?> ids) {
        return this.selectSqlGenerate.getSelectByIdsSql(configuration, ntClass, ids)
                .replaceAll("SELECT \\*", "DELETE");
    }
}
