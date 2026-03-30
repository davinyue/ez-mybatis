package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlEntityTableConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class OracleEntityTableConverter extends MySqlEntityTableConverter implements Converter<EntityTable> {
    private static volatile OracleEntityTableConverter instance;

    protected OracleEntityTableConverter() {
    }

    public static OracleEntityTableConverter getInstance() {
        if (instance == null) {
            synchronized (OracleEntityTableConverter.class) {
                if (instance == null) {
                    instance = new OracleEntityTableConverter();
                }
            }
        }
        return instance;
    }

}
