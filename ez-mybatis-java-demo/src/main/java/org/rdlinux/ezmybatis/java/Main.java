package org.rdlinux.ezmybatis.java;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.java.entity.User;

import java.io.IOException;
import java.io.Reader;

public class Main {
    private static SqlSession sqlSession;

    static {
        String resource = "mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);
        sqlSession = sqlSessionFactory.openSession();
    }

    public static void main(String[] args) {
//        sqlSession.insert("org.rdlinux.ezmybatis.java.mapper.UserMapper.insert", new User());
//        sqlSession.commit();
        MetaObject metaObject = SystemMetaObject.forObject(new User());
        User user = sqlSession.selectOne("org.rdlinux.ezmybatis.java.mapper.UserMapper.selectByPrimaryKey",
                1);
        System.out.println("test");
    }
}
