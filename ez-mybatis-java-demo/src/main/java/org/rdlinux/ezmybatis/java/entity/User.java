package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ez_user")
@Getter
@Setter
public class User extends BaseEntity {
    private String name;
    private String sex;
    @Column(name = "age")
    private Integer userAge;
    @Transient
    private String ignore;
}
