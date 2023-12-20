package org.rdlinux.dm;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;

import java.io.IOException;
import java.io.Reader;

public class DmBaseTest {
    public static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis-config-dm.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
        Configuration configuration = parser.parse();
        EzMybatisContent.init(new EzMybatisConfig(configuration));
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
