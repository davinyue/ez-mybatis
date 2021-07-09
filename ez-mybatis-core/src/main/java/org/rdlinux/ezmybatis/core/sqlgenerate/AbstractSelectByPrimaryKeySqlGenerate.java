package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.content.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.utils.Assert;

public abstract class AbstractSelectByPrimaryKeySqlGenerate implements SelectByPrimaryKeySqlGenerate {
    /**
     * 获取关键字引号
     */
    protected abstract String getKeywordQM();

    @Override
    public String getSelectByPrimaryKeySql(Class<?> ntClass, Object id) {
        Assert.notNull(id, "id can not be null");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(ntClass, true);
        String table = entityClassInfo.getTableName();
        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        String escape = "$";
        if (id instanceof CharSequence) {
            escape = "#";
        }
        String kwQM = this.getKeywordQM();
        return "SELECT * FROM " + kwQM + table + kwQM + " WHERE " + kwQM + idColumn + kwQM + " = " + escape + "{id}";
    }
}
