package org.rdlinux.mssql;

import org.rdlinux.AbstractBaseTest;

public class MssqlBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-mssql.xml", true);
    }
}
