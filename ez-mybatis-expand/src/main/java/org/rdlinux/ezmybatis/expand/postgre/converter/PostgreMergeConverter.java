package org.rdlinux.ezmybatis.expand.postgre.converter;

import org.rdlinux.ezmybatis.expand.oracle.converter.OracleMergeConverter;

public class PostgreMergeConverter extends OracleMergeConverter {
    private static volatile PostgreMergeConverter instance;

    protected PostgreMergeConverter() {
    }

    public static PostgreMergeConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreMergeConverter.class) {
                if (instance == null) {
                    instance = new PostgreMergeConverter();
                }
            }
        }
        return instance;
    }
}
