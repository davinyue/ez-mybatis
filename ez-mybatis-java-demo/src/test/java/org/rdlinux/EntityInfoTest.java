package org.rdlinux;

import org.rdlinux.ezmybatis.core.content.EntityInfo;
import org.rdlinux.ezmybatis.java.entity.User;

public class EntityInfoTest {
    public static void main(String[] args) {
        EntityInfo entityInfo = new EntityInfo(User.class);
        System.out.println(entityInfo);
    }
}
