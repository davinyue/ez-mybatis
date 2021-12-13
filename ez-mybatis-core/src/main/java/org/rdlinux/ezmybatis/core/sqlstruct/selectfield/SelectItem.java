package org.rdlinux.ezmybatis.core.sqlstruct.selectfield;

import org.apache.ibatis.session.Configuration;

public interface SelectItem {
    String toSqlPart(Configuration configuration);
}
