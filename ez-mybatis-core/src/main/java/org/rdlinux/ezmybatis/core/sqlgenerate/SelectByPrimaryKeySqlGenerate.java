package org.rdlinux.ezmybatis.core.sqlgenerate;

public interface SelectByPrimaryKeySqlGenerate {
    String getSelectByPrimaryKeySql(Class<?> ntClass, Object id);
}
