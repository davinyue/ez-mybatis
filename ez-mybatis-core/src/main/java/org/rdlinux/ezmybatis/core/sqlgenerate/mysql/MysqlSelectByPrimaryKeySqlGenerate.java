package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.core.content.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.SelectByPrimaryKeySqlGenerate;
import org.rdlinux.ezmybatis.core.utils.Assert;

public class MysqlSelectByPrimaryKeySqlGenerate implements SelectByPrimaryKeySqlGenerate {
    private static volatile MysqlSelectByPrimaryKeySqlGenerate instance;

    private MysqlSelectByPrimaryKeySqlGenerate() {
    }

    public static MysqlSelectByPrimaryKeySqlGenerate getInstance() {
        if (instance == null) {
            synchronized (MySqlInsertSqlGenerate.class) {
                if (instance == null) {
                    instance = new MysqlSelectByPrimaryKeySqlGenerate();
                }
            }
        }
        return instance;
    }

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
        return String.format("SELECT * FROM `%s` WHERE `%s` = %s{id}", table, idColumn, escape);
    }
}
