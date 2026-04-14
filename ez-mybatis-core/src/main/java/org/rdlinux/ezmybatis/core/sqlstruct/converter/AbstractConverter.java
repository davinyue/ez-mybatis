package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

public abstract class AbstractConverter<Obj extends SqlStruct> implements Converter<Obj> {
    /**
     * 实体类型
     */
    private final Class<?> objClass;

    public AbstractConverter() {
        this.objClass = ReflectionUtils.getGenericSuperclass(this.getClass(), 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void buildSql(Type type, Object sp, SqlGenerateContext sqlGenerateContext) {
        Assert.notNull(type, "type can not be null");
        Assert.notNull(sqlGenerateContext, "sqlGenerateContext can not be null");
        Assert.notNull(sqlGenerateContext.getSqlBuilder(), "sqlBuilder can not be null");
        Assert.notNull(sqlGenerateContext.getConfiguration(), "configuration can not be null");
        Assert.notNull(sqlGenerateContext.getMybatisParamHolder(), "mybatisParamHolder can not be null");
        if (sp == null) {
            return;
        }
        if (!this.objClass.isAssignableFrom(sp.getClass())) {
            throw new IllegalArgumentException("Unsupported operation");
        }
        this.doBuildSql(type, (Obj) sp, sqlGenerateContext);
    }

    protected abstract void doBuildSql(Type type, Obj obj, SqlGenerateContext sqlGenerateContext);
}
