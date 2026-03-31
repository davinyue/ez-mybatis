package org.rdlinux.ezmybatis.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单明细测试实体类，用于嵌套 join 场景测试
 */
@Table(name = "ez_complex_order_item")
@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class ComplexOrderItem extends BaseEntity {
    /**
     * 关联的订单ID
     */
    private String orderId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 购买单价
     */
    private BigDecimal unitPrice;

    /**
     * 此项总计金额
     */
    private BigDecimal itemTotalAmount;
}
