package org.rdlinux.pg;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;

public class PgBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-pg.xml", true,
                MapRetKeyPattern.HUMP, TableNamePattern.LOWER_CASE);
    }
}
