package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;

public class SelectAllItem implements SelectItem {
    @Override
    public String toSqlPart(Configuration configuration) {
        return " * ";
    }
}
