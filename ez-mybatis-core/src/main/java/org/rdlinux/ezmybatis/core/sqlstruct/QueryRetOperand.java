package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.enumeration.OrderType;

/**
 * 可作为查询结果的 操作数, 左值/右值
 */
public interface QueryRetOperand extends Operand {
    /**
     * 构建针对此操作数的正序（ASC）排序项
     *
     * @return ORDER BY ASC 对象
     */
    default OrderBy.OrderItem asc() {
        return new OrderBy.OrderItem().setValue(this).setOrderType(OrderType.ASC);
    }

    /**
     * 构建针对此操作数的倒序（DESC）排序项
     *
     * @return ORDER BY DESC 对象
     */
    default OrderBy.OrderItem desc() {
        return new OrderBy.OrderItem().setValue(this).setOrderType(OrderType.DESC);
    }

    /**
     * 为当前属性设置查询展示的别名
     *
     * @param alias 别名名称
     * @return 包含别名的选择操作数对象
     */
    default SelectOperand as(String alias) {
        return new SelectOperand(this, alias);
    }
}
