package org.rdlinux.dm;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyCasePolicy;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;

public class DmBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-dm.xml", Boolean.TRUE,
                MapRetKeyCasePolicy.HUMP, NameCasePolicy.UPPER_CASE, NameCasePolicy.UPPER_CASE);
    }
}
