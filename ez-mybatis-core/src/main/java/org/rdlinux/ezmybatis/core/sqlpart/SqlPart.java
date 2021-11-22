package org.rdlinux.ezmybatis.core.sqlpart;

import org.apache.ibatis.session.Configuration;

public interface SqlPart {
    String toSqlPart(Configuration configuration);
}
