package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class DmLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile DmLimitConverter instance;

    protected DmLimitConverter() {
    }

    public static DmLimitConverter getInstance() {
        if (instance == null) {
            synchronized (DmLimitConverter.class) {
                if (instance == null) {
                    instance = new DmLimitConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Limit limit, SqlGenerateContext sqlGenerateContext) {
        if (limit == null) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" FETCH FIRST ").append(limit.getSize()).append(" ROWS ONLY ");
    }

}
