package org.rdlinux.oracle;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.java.entity.BaseEntity;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;

public class OracleBaseTest {
    protected static SqlSessionFactory sqlSessionFactory;

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
        EzMybatisConfig ezMybatisConfig = new EzMybatisConfig(configuration);
        ezMybatisConfig.setMapRetKeyPattern(MapRetKeyPattern.HUMP);
        ezMybatisConfig.setEscapeKeyword(false);
        ezMybatisConfig.setEnableOracleOffsetFetchPage(true);
        EzMybatisContent.init(ezMybatisConfig);
        EzMybatisContent.addInsertListener(ezMybatisConfig, new EzMybatisInsertListener() {
            @Override
            public void onInsert(Object model) {
                if (model instanceof BaseEntity) {
                    System.out.println("插入事件");
                    ((BaseEntity) model).setUpdateTime(new Date());
                    ((BaseEntity) model).setCreateTime(new Date());
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
