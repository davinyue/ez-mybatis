package ink.dvc.mysql;

import org.junit.Test;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.*;

public class MysqlInsertTest extends MysqlBaseTest {
    @Test
    public void insert() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(UserMapper.class).insert(user);
        System.out.println(insert);
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        int insert1 = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class).insert(user);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert1);
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
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(UserMapper.class).batchInsert(users);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void batchInsert1() {
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
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class).batchInsert(users);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void insertBySql() {
        String sql = "INSERT INTO `ez_user` (`id`, `create_time`, `update_time`, `name`, `sex`, `age`) " +
                "VALUES ('09b6a5af501b4a1fb8c22002df5f15af', '2021-12-30 11:58:23', '2021-12-30 11:58:23', " +
                "'王二', 1, 27);\n";
        Integer integer = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class).insertBySql(sql, new HashMap<>());
        System.out.println(integer);
    }
}
