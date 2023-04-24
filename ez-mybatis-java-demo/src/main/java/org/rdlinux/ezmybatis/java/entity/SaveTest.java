package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Table;

@Getter
@Setter
@FieldNameConstants
@Accessors(chain = true)
@Table(name = "save_test")
public class SaveTest {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
}
