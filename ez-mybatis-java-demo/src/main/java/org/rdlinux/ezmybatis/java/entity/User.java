package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.apache.ibatis.type.StringTypeHandler;
import org.rdlinux.ezmybatis.annotation.ColumnHandler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ez_user", schema = "ez_mybatis")
@Getter
@Setter
@FieldNameConstants
public class User extends BaseEntity {
    @ColumnHandler(StringTypeHandler.class)
    private String name;
    private Sex sex;
    @Column(name = "age")
    private Integer userAge;
    @Transient
    private String ignore;

    public static enum Sex {
        WOMAN,
        MAN
    }
}
