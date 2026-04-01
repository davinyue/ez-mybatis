package org.rdlinux.mssql;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyCasePolicy;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;

public class MsSqlBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-mssql.xml", true,
                MapRetKeyCasePolicy.HUMP, NameCasePolicy.LOWER_CASE, NameCasePolicy.LOWER_CASE);
    }
}
