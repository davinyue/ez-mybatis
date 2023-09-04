package org.rdlinux.mysql;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.java.entity.BaseEntity;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;

public class MysqlBaseTest {
    public static SqlSessionFactory sqlSessionFactory;

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
        EzMybatisConfig ezMybatisConfig = new EzMybatisConfig(configuration);
        EzMybatisContent.init(ezMybatisConfig);
        EzMybatisContent.addInsertListener(ezMybatisConfig, new EzMybatisInsertListener() {
            @Override
            public void onInsert(Object entity) {
                if (entity instanceof BaseEntity) {
                    System.out.println("插入事件");
                    ((BaseEntity) entity).setUpdateTime(new Date());
                    ((BaseEntity) entity).setCreateTime(new Date());
                }
            }

            @Override
            public void onBatchInsert(Collection<?> models) {
                models.forEach(this::onInsert);
            }
        });
        EzMybatisContent.addDeleteListener(ezMybatisConfig, new EzMybatisDeleteListener() {

            @Override
            public void onDelete(Object entity) {
                System.out.println("删除事件");
            }

            @Override
            public void onBatchDelete(Collection<Object> models) {
                models.forEach(this::onDelete);
            }

            @Override
            public void onDeleteById(Object id, Class<?> ntClass) {
                System.out.println("删除事件");
            }

            @Override
            public void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass) {
                for (Object id : ids) {
                    this.onDeleteById(id, ntClass);
                }
            }
        });
        EzMybatisContent.addUpdateListener(ezMybatisConfig, new EzMybatisUpdateListener() {
            @Override
            public void onUpdate(Object entity) {
                System.out.println("更新事件");
            }

            @Override
            public void onBatchUpdate(Collection<Object> models) {
                System.out.println("更新事件");
            }

            @Override
            public void onReplace(Object entity) {
                System.out.println("替换事件");
            }

            @Override
            public void onBatchReplace(Collection<Object> models) {
                System.out.println("替换事件");
            }
        });
        EzMybatisContent.addFieldSetListener(ezMybatisConfig, (obj, field, value) -> {
            System.out.println("设置" + obj.getClass().getSimpleName() + "类的" + field.getName() + "属性值为" + value);
            return value;
        });
        EzMybatisContent.addOnBuildSqlGetFieldListener(ezMybatisConfig, (ntType, field, value) -> {
            System.out.println("构建sql时获取" + ntType.getSimpleName() + "类的" + field.getName() + "属性值为" + value);
            return value;
        });
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
