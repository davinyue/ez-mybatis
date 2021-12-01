package org.rdlinux.ezmybatis.core.sqlpart.selectfield;

import org.apache.ibatis.session.Configuration;

public interface EzSelectField {
    String toSqlPart(Configuration configuration);
}
