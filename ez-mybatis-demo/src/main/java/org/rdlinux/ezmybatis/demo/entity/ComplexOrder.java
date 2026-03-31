package org.rdlinux.ezmybatis.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 复杂订单测试实体类
 */
@Table(name = "ez_complex_order")
@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class ComplexOrder extends BaseEntity {
    /**
     * 下单用户ID
     */
    private String userId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态枚举
     */
    private OrderStatus status;

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING, PAID, SHIPPED, COMPLETED
    }
}
