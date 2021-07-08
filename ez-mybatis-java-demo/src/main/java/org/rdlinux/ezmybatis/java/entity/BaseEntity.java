package org.rdlinux.ezmybatis.java.entity;

import javax.persistence.Id;

public abstract class BaseEntity {
    @Id
    private int id;
}
