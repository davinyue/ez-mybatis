package org.rdlinux.ezmybatis.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ExtInfo {
    private String hobby;
    private String remark;
}
