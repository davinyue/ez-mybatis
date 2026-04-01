package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

public class DmEntityInfoBuilder extends OracleEntityInfoBuilder {
    private static volatile DmEntityInfoBuilder instance;

    private DmEntityInfoBuilder() {
    }

    public static DmEntityInfoBuilder getInstance() {
        if (instance == null) {
            synchronized (DmEntityInfoBuilder.class) {
                if (instance == null) {
                    instance = new DmEntityInfoBuilder();
                }
            }
        }
        return instance;
    }

}
