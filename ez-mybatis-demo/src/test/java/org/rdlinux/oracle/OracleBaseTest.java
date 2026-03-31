package org.rdlinux.oracle;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;

public class OracleBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-oracle.xml", false,
                MapRetKeyPattern.HUMP, TableNamePattern.UPPER_CASE);
    }
}
