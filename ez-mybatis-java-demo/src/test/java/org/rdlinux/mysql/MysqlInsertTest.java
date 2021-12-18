package org.rdlinux.mysql;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MysqlInsertTest {
    public static SqlSession sqlSession;

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

    @Test
    public void insert() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setName("王二");
        //user.setFirstName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).insert(user);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchInsert() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUpdateTime(new Date());
            user.setCreateTime(new Date());
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + i + 1);
            user.setName("王");
            user.setUserAge(27 + i);
            user.setSex(User.Sex.MAN);
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchInsert(users);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchInsertTUT() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.selectById("1s");
        long start = System.currentTimeMillis();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 500; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("王");
            user.setUserAge(27 + i);
            user.setSex(User.Sex.MAN);
            users.add(user);
        }
        int insert = userMapper.batchInsert(users);
        sqlSession.commit();
        long end = System.currentTimeMillis();
        System.out.println("耗时" + (end - start));
    }
}
