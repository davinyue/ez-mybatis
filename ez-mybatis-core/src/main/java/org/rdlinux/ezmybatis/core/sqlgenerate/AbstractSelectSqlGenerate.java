package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.utils.Assert;

import java.util.List;

public abstract class AbstractSelectSqlGenerate implements SelectSqlGenerate {
    /**
     * 获取关键字引号
     */
    protected abstract String getKeywordQM();

    @Override
    public String getSelectByIdSql(Configuration configuration, Class<?> ntClass, Object id) {
        Assert.notNull(id, "id cannot be null");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String table = entityClassInfo.getTableName();
        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        String escape = MybatisParamEscape.getEscapeChar(id);
        String kwQM = this.getKeywordQM();
        return "SELECT * FROM " + kwQM + table + kwQM + " WHERE " + kwQM + idColumn + kwQM + " = " + escape
                + "{" + EzMybatisConstant.MAPPER_PARAM_ID + "}";
    }

    @Override
    public String getSelectByIdsSql(Configuration configuration, Class<?> ntClass, List<?> ids) {
        Assert.notEmpty(ids, "ids cannot be null");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String table = entityClassInfo.getTableName();
        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        String kwQM = this.getKeywordQM();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM " + kwQM + table + kwQM + " WHERE " + kwQM +
                idColumn + kwQM + " IN ( ");
        for (int i = 0; i < ids.size(); i++) {
            Object id = ids.get(i);
            Assert.notNull(id, String.format("ids[%d] can not be null", i));
            String escape = MybatisParamEscape.getEscapeChar(id);
            sqlBuilder.append(escape).append("{").append(EzMybatisConstant.MAPPER_PARAM_IDS).append("[").append(i)
                    .append("]}");
            if (i + 1 != ids.size()) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }
}
