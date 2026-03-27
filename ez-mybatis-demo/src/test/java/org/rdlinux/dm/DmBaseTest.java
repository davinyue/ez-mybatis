package org.rdlinux.dm;

import org.rdlinux.AbstractBaseTest;

public class DmBaseTest extends AbstractBaseTest {
    static {
        initSqlSessionFactory("mybatis-config-dm.xml", false);
    }
}
