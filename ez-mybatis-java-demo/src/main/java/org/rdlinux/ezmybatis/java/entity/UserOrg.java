package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Table;

@Getter
@Setter
@FieldNameConstants
@Table(name = "ez_user_org")
public class UserOrg extends BaseEntity {
    private String userId;
    private String orgId;
}
