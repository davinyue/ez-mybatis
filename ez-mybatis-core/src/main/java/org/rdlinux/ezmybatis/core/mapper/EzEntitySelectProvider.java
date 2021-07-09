package org.rdlinux.ezmybatis.core.mapper;

public class EzEntitySelectProvider implements EzProviderSqlGenerate {
    @Override
    public String generate(Object param) {
        return "select * from user where id = 1";
    }
}
