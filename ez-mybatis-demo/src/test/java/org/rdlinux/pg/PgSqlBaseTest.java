package org.rdlinux.pg;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;

public class PgSqlBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-pg.xml", false,
                MapRetKeyPattern.HUMP, TableNamePattern.LOWER_CASE);
    }
}
