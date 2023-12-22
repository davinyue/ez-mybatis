package org.rdlinux.pg;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.java.entity.BaseEntity;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;

public class PgBaseTest {
    protected static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis-config-pg.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
        Configuration configuration = parser.parse();
        EzMybatisConfig ezMybatisConfig = new EzMybatisConfig(configuration);
        EzMybatisContent.init(ezMybatisConfig);
        EzMybatisContent.addInsertListener(ezMybatisConfig, new EzMybatisInsertListener() {
            @Override
            public void onInsert(Object entity) {
                if (entity instanceof BaseEntity) {
                    ((BaseEntity) entity).setUpdateTime(new Date());
                    ((BaseEntity) entity).setCreateTime(new Date());
                }
            }

            @Override
            public void onBatchInsert(Collection<?> models) {
                models.forEach(this::onInsert);
            }
        });
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
