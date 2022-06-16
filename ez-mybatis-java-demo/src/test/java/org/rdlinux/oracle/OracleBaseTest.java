package org.rdlinux.oracle;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;

import java.io.IOException;
import java.io.Reader;

public class OracleBaseTest {
    public static SqlSession sqlSession;

    static {
        String resource = "mybatis-config-oracle.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
        Configuration configuration = parser.parse();
        EzMybatisContent.init(new EzMybatisConfig(configuration));
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        sqlSession = sqlSessionFactory.openSession();
    }
}
