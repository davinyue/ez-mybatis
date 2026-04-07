package org.rdlinux.mysql;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyCasePolicy;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;

public class MySqlBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-mysql.xml", Boolean.TRUE,
                MapRetKeyCasePolicy.HUMP, NameCasePolicy.LOWER_CASE, NameCasePolicy.LOWER_CASE);
    }
}
