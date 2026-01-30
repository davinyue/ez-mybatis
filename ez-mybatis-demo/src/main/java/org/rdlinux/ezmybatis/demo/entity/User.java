package org.rdlinux.ezmybatis.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.rdlinux.ezmybatis.annotation.ColumnHandler;
import org.rdlinux.ezmybatis.demo.typehandler.CustomStringTypeHandler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ez_user")
@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class User extends BaseEntity {
    @ColumnHandler(CustomStringTypeHandler.class)
    private String name;
    private Sex sex;
    @Column(name = "age")
    private Integer userAge;
    @Transient
    private String ignore;

    public enum Sex {
        WOMAN,
        MAN
    }
}
