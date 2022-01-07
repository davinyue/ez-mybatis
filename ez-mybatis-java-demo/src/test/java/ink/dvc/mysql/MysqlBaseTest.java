package ink.dvc.mysql;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.java.entity.BaseEntity;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;

public class MysqlBaseTest {
    public static SqlSession sqlSession;

    static {
        String resource = "mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
        Configuration configuration = parser.parse();
        EzMybatisContent.init(configuration);
        EzMybatisContent.addInsertListener(configuration, new EzMybatisInsertListener() {
            @Override
            public void onInsert(Object entity) {
                if (entity instanceof BaseEntity) {
                    ((BaseEntity) entity).setUpdateTime(new Date());
                    ((BaseEntity) entity).setCreateTime(new Date());
                }
            }

            @Override
            public void onBatchInsert(List<Object> entitys) {
                entitys.forEach(this::onInsert);
            }
        });
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        sqlSession = sqlSessionFactory.openSession();
    }
}
