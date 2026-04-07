package org.rdlinux.pg;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyCasePolicy;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;

public class PgBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-pg.xml", Boolean.TRUE,
                MapRetKeyCasePolicy.HUMP, NameCasePolicy.LOWER_CASE, NameCasePolicy.LOWER_CASE);
    }
}
