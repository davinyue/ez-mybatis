package org.rdlinux.oracle;

import org.rdlinux.AbstractBaseTest;

public class OracleBaseTest extends AbstractBaseTest {
    static {
        initSqlSessionFactory("mybatis-config-oracle.xml", false);
    }
}
