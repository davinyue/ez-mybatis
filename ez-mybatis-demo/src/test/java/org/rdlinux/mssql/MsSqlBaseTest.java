package org.rdlinux.mssql;

import org.rdlinux.AbstractBaseTest;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;

public class MsSqlBaseTest extends AbstractBaseTest {
    static {
        AbstractBaseTest.initSqlSessionFactory("mybatis-config-mssql.xml", true,
                MapRetKeyPattern.HUMP, TableNamePattern.LOWER_CASE);
    }
}
