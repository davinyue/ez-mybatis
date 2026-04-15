package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;

/**
 * 转换器, 将sql组成部分转换为sql
 *
 * @param <Sp> 被转换对象类型
 */
public interface Converter<Sp extends SqlStruct> {
    /**
     * 转换为sql
     *
     * @param type               转换类型
     * @param sp                 被转换对象
     * @param sqlGenerateContext sql构建上下文
     */
    void buildSql(Type type, Object sp, SqlGenerateContext sqlGenerateContext);

    enum Type {
        SELECT,
        DELETE,
        INSERT,
        UPDATE;
    }
}
