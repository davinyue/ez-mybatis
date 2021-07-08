package org.rdlinux.ezmybatis.core.mapper;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

public class EzEntityInsertProvider implements EzProviderSqlGenerate {
    @Override
    public String generate(Object param) {
        return SqlGenerateFactory.getSqlGenerate().getInsertSql(param);
    }
}
