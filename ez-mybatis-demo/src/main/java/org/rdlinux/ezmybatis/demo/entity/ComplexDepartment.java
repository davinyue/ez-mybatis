package org.rdlinux.ezmybatis.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Table;

/**
 * 部门测试实体类，用于多表 join 场景测试
 */
@Table(name = "ez_complex_department")
@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class ComplexDepartment extends BaseEntity {
    /**
     * 部门名称
     */
    private String name;

    /**
     * 上级部门ID
     */
    private String parentId;

    /**
     * 部门描述
     */
    private String description;
}
