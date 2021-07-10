package org.rdlinux.ezmybatis.java;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

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
        //sqlSession.selectList("", "");
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById(User.class, 3);
        System.out.println(JacksonUtils.toJsonString(user));
        List<Integer> ids = new LinkedList<>();
        ids.add(1);
        ids.add(3);
        List<User> users = mapper.selectByIds(User.class, ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }
}
