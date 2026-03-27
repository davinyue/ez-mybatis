package org.rdlinux.pg;

import org.rdlinux.AbstractBaseTest;

public class PgSqlBaseTest extends AbstractBaseTest {
    static {
        initSqlSessionFactory("mybatis-config-pg.xml", false);
    }
}
