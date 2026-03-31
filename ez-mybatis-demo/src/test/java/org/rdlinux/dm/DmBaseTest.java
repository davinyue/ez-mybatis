package org.rdlinux.dm;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;

public class DmBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-dm.xml", false,
                MapRetKeyPattern.HUMP, TableNamePattern.UPPER_CASE);
    }
}
