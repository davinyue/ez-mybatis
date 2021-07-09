package org.rdlinux.ezmybatis.core.content;

import java.util.Map;

public class ResultClassInfo {
    private Class<?> metaClass;
    private Map<String, String> columnMapProperty;

    public ResultClassInfo(Class<?> metaClass, Map<String, String> columnMapProperty) {
        this.metaClass = metaClass;
        this.columnMapProperty = columnMapProperty;
    }

    public String getPropertyByColumn(String column) {
        if (this.columnMapProperty == null) {
            return null;
        } else {
            return this.columnMapProperty.get(column);
        }
    }
}
