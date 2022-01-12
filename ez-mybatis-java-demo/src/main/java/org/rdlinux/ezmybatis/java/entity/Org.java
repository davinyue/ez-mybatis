package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Getter
@Setter
@Table(name = "ez_org")
public class Org extends BaseEntity {
    private String name;
    private String code;
    private String pid;
    private String pids;
}
