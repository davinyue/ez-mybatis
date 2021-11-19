package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User extends BaseEntity {
    @Column(name = "LAST_NAME")
    private String name;
    private Integer age;
    @Transient
    private String ignore;

    private String firstName;

    private String sex;
}
