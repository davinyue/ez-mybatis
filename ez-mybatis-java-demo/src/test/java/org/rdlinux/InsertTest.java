package org.rdlinux;

import org.junit.Test;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class InsertTest extends BaseTest {
    @Test
    public void insert() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        //user.setFirstName("王");
        user.setAge(27);
        user.setSex("女");
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).insert(user);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchInsert() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + i + 1);
            user.setFirstName("王");
            user.setAge(27 + i);
            user.setSex(i % 2 == 0 ? "男" : "女");
            users.add(user);
        }
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).batchInsert(users);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }
}
