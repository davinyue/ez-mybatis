package org.rdlinux.mysql;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;

public class MySqlBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-mysql.xml", true,
                MapRetKeyPattern.HUMP, TableNamePattern.LOWER_CASE);
    }
}
