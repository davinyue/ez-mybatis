package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.apache.ibatis.session.Configuration;

public interface SelectItem {
    String toSqlPart(Configuration configuration);
}
