package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * jdbc 参数
 */
@Getter
@Setter
public class EzJdbcSqlParam {
    /**
     * 参数值
     */
    private Object value;
    @SuppressWarnings({"rawtypes"})
    private TypeHandler typeHandler;
    /**
     * 指定jdbc类型
     */
    private JdbcType jdbcType;

    public EzJdbcSqlParam(Object value, TypeHandler<?> typeHandler, JdbcType jdbcType) {
        this.value = value;
        this.jdbcType = jdbcType;
        Assert.notNull(typeHandler, "typeHandler can not be null");
        this.typeHandler = typeHandler;
    }
}
