package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方言提供者抽象基类，提供转换器管理的通用实现。
 * <p>
 * 所有具体方言提供者应继承此类，获得转换器注册和获取的能力。
 */
public abstract class AbstractDbDialectProvider implements DbDialectProvider {

    private final Map<Class<?>, Converter<?>> converterMap = new ConcurrentHashMap<>();
    protected EntityInfoBuilder entityInfoBuilder;

    @Override
    public <T extends SqlStruct> void addConverter(Class<T> sqlStruct, Converter<T> converter) {
        this.converterMap.put(sqlStruct, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends SqlStruct> Converter<T> getConverter(Class<T> sqlStruct) {
        Converter<T> converter = (Converter<T>) this.converterMap.get(sqlStruct);
        if (converter == null) {
            throw new RuntimeException(String.format("%s cannot find the converter of %s",
                    this.getDbType().name(), sqlStruct.getSimpleName()));
        }
        return converter;
    }

    @Override
    public EntityInfoBuilder getEntityInfoBuilder() {
        return this.entityInfoBuilder;
    }

    @Override
    public void setEntityInfoBuilder(EntityInfoBuilder entityInfoBuilder) {
        this.entityInfoBuilder = entityInfoBuilder;
    }
}
