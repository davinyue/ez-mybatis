package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 数据库表列参数，代表数据库表中的一列直接关联。
 * 可以将其用于构建查询条件（如比较、模糊查询等）、查询列定义或统计函数。
 */
@Getter
public class TableColumn implements QueryRetOperand {
    /**
     * 列归属的表对象
     */
    private final Table table;
    /**
     * 表中的列名
     */
    private final String column;

    /**
     * 构建数据库表列参数
     *
     * @param table  表对象
     * @param column 表中的列名
     */
    private TableColumn(Table table, String column) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        this.table = table;
        this.column = column;
    }

    /**
     * 创建表列的静态工厂方法
     *
     * @param table  表对象
     * @param column 表中的列名
     * @return 表列构建对象
     */
    public static TableColumn of(Table table, String column) {
        return new TableColumn(table, column);
    }

    /**
     * 构建 EzUpdate SET 子句中的更新项，将当前列更新为指定的操作数值。
     * 操作数可以是普通值（通过 {@link ObjArg#of} 包装）、另一个列、函数、公式等。
     *
     * @param operand 要设置的新值操作数
     * @return 更新项对象，可直接添加到 EzUpdate 的 SET 子句中
     */
    public UpdateItem set(Operand operand) {
        return new UpdateColumnItem(this.table, this.column, operand);
    }

    /**
     * 构建 EzUpdate SET 子句中的更新项，将当前列更新为指定的操作数值。
     * 操作数可以是普通值、另一个字段、函数、公式等。
     *
     * @param value 要设置的新值操作数
     * @return 更新项对象，可直接添加到 EzUpdate 的 SET 子句中
     */
    public UpdateItem set(Object value) {
        if (value == null) {
            return this.setToNull();
        }
        return new UpdateColumnItem(this.table, this.column, Operand.objToOperand(value));
    }

    /**
     * 构建 EzUpdate SET 子句中的更新项，将当前列更新为 NULL。
     *
     * @return 将列设置为 NULL 的更新项对象
     */
    public UpdateItem setToNull() {
        return new UpdateColumnItem(this.table, this.column, null);
    }
}
