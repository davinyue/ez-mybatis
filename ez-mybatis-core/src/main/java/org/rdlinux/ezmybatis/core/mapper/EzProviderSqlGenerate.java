package org.rdlinux.ezmybatis.core.mapper;

/**
 * mybatis provider sql生成
 */
public interface EzProviderSqlGenerate {
    /**
     * 生成sql
     */
    String generate(Object param);
}
