package org.rdlinux.mssql;

import org.rdlinux.AbstractBaseTest;

public class MsSqlBaseTest extends AbstractBaseTest {
    static {
        initSqlSessionFactory("mybatis-config-mssql.xml", false);
    }
}
