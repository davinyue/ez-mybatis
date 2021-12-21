package ink.dvc.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Getter
@Setter
@Table(name = "ez_user_org")
public class UserOrg extends BaseEntity {
    private String userId;
    private String orgId;
}
