package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Org extends BaseEntity {
    private String name;
    private String code;
    private String pid;
    private String pids;
}
