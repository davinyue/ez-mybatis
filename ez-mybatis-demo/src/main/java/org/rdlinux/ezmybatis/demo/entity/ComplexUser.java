package org.rdlinux.ezmybatis.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.rdlinux.ezmybatis.annotation.TypeHandler;
import org.rdlinux.ezmybatis.demo.enc.DbEncrypt;
import org.rdlinux.ezmybatis.demo.typehandler.ExtInfoJsonTypeHandler;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 全数据类型与自定义转换测试模型
 */
@Table(name = "ez_complex_user")
@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class ComplexUser extends BaseEntity {
    /**
     * 用户名
     */
    private String username;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户类型
     */
    private Short userType;

    /**
     * 积分
     */
    private Long score;

    /**
     * 账户余额
     */
    private Double accountBalance;

    /**
     * 薪资
     */
    private BigDecimal salary;

    /**
     * 是否激活
     */
    private Boolean isActive;

    /**
     * 枚举类型测试字段
     */
    private UserStatus status;

    /**
     * 日期类型测试字段
     */
    private Date birthday;

    /**
     * Blob 二进制数据测试字段
     */
    private byte[] avatar;

    /**
     * 文本大字段测试字段
     */
    private String description;

    /**
     * Transient 注解忽略测试字段
     */
    @Transient
    private String ignoredData;

    /**
     * 自定义 Column name 测试字段
     */
    @Column(name = "custom_column_name")
    private String specificColumn;

    /**
     * 数据加解密测试字段
     * 不使用 TypeHandler，而是预留给事件监听器（如 InsertListener/UpdateListener）进行加解密测试
     */
    @DbEncrypt
    private String secretContent;

    /**
     * 用于测试自定义 TypeHandler 的字段（如 JSON 到实体对象的映射）
     */
    @TypeHandler(ExtInfoJsonTypeHandler.class)
    private ExtInfo extInfo;

    /**
     * 引用的部门ID，用于多表 join 测试
     */
    private String departmentId;

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        NORMAL, DISABLED, DELETED
    }
}
