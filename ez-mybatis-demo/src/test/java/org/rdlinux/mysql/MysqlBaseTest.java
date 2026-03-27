package org.rdlinux.mysql;

import org.rdlinux.AbstractBaseTest;

public class MysqlBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-mysql.xml", true);
    }
}
