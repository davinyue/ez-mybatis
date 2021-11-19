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
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setFirstName(null);
            } else {
                user.setFirstName("王");
            }
            user.setAge(27 + i);
            user.setSex(i % 2 == 0 ? "男" : "女");
            users.add(user);
        }
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).batchInsert(users);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchInsertTUT() {
        UserMapper userMapper = BaseTest.sqlSession.getMapper(UserMapper.class);
        userMapper.selectById("1s");
        long start = System.currentTimeMillis();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 5000; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + i + 1);
            user.setFirstName("王");
            user.setAge(27 + i);
            user.setSex(i % 2 == 0 ? "男" : "女");
            users.add(user);
        }
        int insert = userMapper.batchInsert(users);
        BaseTest.sqlSession.commit();
        long end = System.currentTimeMillis();
        System.out.println("耗时" + (end - start));
    }
}
