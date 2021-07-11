package org.rdlinux;

import org.junit.Test;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;

public class DeleteTest extends BaseTest {
    @Test
    public void delete() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = BaseTest.sqlSession.getMapper(UserMapper.class).delete(user);
        System.out.println(delete);
    }

    @Test
    public void batchDelete() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            users.add(user);
        }
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).batchDelete(users);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteById() {
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class)
                .deleteById("016cdcdd76f94879ab3d24850514812b");
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchDeleteById() {
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b");
        }
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).batchDeleteById(users);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }
}
