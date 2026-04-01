package org.rdlinux.oracle;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyCasePolicy;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;

public class OracleBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-oracle.xml", false,
                MapRetKeyCasePolicy.HUMP, NameCasePolicy.UPPER_CASE, NameCasePolicy.UPPER_CASE);
    }
}
