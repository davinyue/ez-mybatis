package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;

public class SelectAllDistinct extends AbstractSelectItem {

    @Override
    public String toSqlPart(Configuration configuration) {
        return " DISTINCT * ";
    }
}
