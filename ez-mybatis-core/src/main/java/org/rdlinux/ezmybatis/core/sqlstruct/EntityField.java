package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 实体类属性参数，代表数据库表对应的实体类的一个字段。
 * 可以将其用于构建查询条件（如比较、模糊查询等）、查询列定义或统计函数。
 */
@Getter
public class EntityField implements QueryRetOperand {
    /**
     * 属性归属的实体表对象
     */
    private final EntityTable table;
    /**
     * 实体类的属性名
     */
    private final String field;

    /**
     * 构建实体类属性参数
     *
     * @param table 实体表对象
     * @param field 实体类的属性名
     */
    private EntityField(EntityTable table, String field) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        this.table = table;
        this.field = field;
    }

    /**
     * 创建实体类属性参数的静态工厂方法
     *
     * @param table 实体表对象
     * @param field 实体类的属性名
     * @return 实体属性构建对象
     */
    public static EntityField of(EntityTable table, String field) {
        return new EntityField(table, field);
    }

    /**
     * 构建 EzUpdate SET 子句中的更新项，将当前属性更新为指定的操作数值。
     * 操作数可以是普通值（通过 {@link ObjArg#of} 包装）、另一个字段、函数、公式等。
     *
     * @param operand 要设置的新值操作数
     * @return 更新项对象，可直接添加到 EzUpdate 的 SET 子句中
     */
    public UpdateItem set(Operand operand) {
        return new UpdateFieldItem(this.table, this.field, operand);
    }

    /**
     * 构建 EzUpdate SET 子句中的更新项，将当前属性更新为指定的操作数值。
     * 操作数可以是普通值、另一个字段、函数、公式等。
     *
     * @param value 要设置的新值操作数
     * @return 更新项对象，可直接添加到 EzUpdate 的 SET 子句中
     */
    public UpdateItem set(Object value) {
        if (value == null) {
            return this.setToNull();
        }
        return new UpdateFieldItem(this.table, this.field, Operand.objToOperand(value));
    }

    /**
     * 构建 EzUpdate SET 子句中的更新项，将当前属性更新为 NULL。
     *
     * @return 将属性设置为 NULL 的更新项对象
     */
    public UpdateItem setToNull() {
        return new UpdateFieldItem(this.table, this.field, null);
    }
}
