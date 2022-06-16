package ink.dvc.mysql;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.*;

public class MysqlInsertTest extends MysqlBaseTest {
    @Test
    public void insert() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).insert(user);
        System.out.println(insert);
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        int insert1 = sqlSession.getMapper(EzMapper.class).insert(user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert1);
    }

    @Test
    public void batchInsert() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();

        long start = System.currentTimeMillis();
        for (int h = 0; h < 1; h++) {
            List<User> users = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                User user = new User();
                user.setUpdateTime(new Date());
                user.setCreateTime(new Date());
                user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                user.setName("芳" + (i + 1));
                user.setUserAge(1 + i);
                user.setSex(User.Sex.MAN);
                users.add(user);
            }
            sqlSession.getMapper(UserMapper.class).batchInsert(users);
            sqlSession.commit();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        sqlSession.close();
    }

    @Test
    public void batchInsert1() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUpdateTime(new Date());
            user.setCreateTime(new Date());
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + (i + 1));
            user.setUserAge(27 + i);
            user.setSex(User.Sex.MAN);
            users.add(user);
        }
        int insert = sqlSession.getMapper(EzMapper.class).batchInsert(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void insertBySql() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        String sql = "INSERT INTO `ez_user` (`id`, `create_time`, `update_time`, `name`, `sex`, `age`) " +
                "VALUES ('#id', '2021-12-30 11:58:23', '2021-12-30 11:58:23', " +
                "'王二', 1, 27);\n";
        sql = sql.replace("#id", UUID.randomUUID().toString().replace("-", ""));
        Integer integer = sqlSession.getMapper(EzMapper.class).insertBySql(sql,
                new HashMap<>());
        System.out.println(integer);
        sqlSession.close();
    }
}
