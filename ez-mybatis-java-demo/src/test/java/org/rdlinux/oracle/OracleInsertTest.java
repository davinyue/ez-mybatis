package org.rdlinux.oracle;

import org.junit.Test;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class OracleInsertTest extends OracleBaseTest {
    @Test
    public void insert() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        //user.setFirstName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = OracleBaseTest.sqlSession.getMapper(UserMapper.class).insert(user);
        OracleBaseTest.sqlSession.commit();
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
                user.setName(null);
            } else {
                user.setName("王");
            }
            user.setUserAge(27 + i);
            user.setSex(User.Sex.MAN);
            users.add(user);
        }
        int insert = OracleBaseTest.sqlSession.getMapper(UserMapper.class).batchInsert(users);
        OracleBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchInsertTUT() {
        UserMapper userMapper = OracleBaseTest.sqlSession.getMapper(UserMapper.class);
        userMapper.selectById("1s");
        long start = System.currentTimeMillis();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 500; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + i + 1);
            user.setName("王");
            user.setUserAge(27 + i);
            user.setSex(User.Sex.MAN);
            users.add(user);
        }
        int insert = userMapper.batchInsert(users);
        OracleBaseTest.sqlSession.commit();
        long end = System.currentTimeMillis();
        System.out.println("耗时" + (end - start));
    }
}
