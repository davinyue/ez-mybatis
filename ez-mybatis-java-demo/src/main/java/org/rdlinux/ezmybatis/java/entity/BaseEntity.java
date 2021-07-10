package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public abstract class BaseEntity {
    @Id
    private String id;
}
