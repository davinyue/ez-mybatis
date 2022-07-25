package org.rdlinux.mysql;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;

@Log4j2
public class MysqlUpdateTest extends MysqlBaseTest {
    @Test
    public void update() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).update(user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert);
    }

    @Test
    public void update1() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(EzMapper.class).update(user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert);
    }

    @Test
    public void batchUpdate() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchUpdate(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void batchUpdate1() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = sqlSession.getMapper(EzMapper.class).batchUpdate(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void replace() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(UserMapper.class).replace(user);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void replace1() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(EzMapper.class).replace(user);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void batchReplace() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchReplace(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void batchReplace1() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = sqlSession.getMapper(EzMapper.class).batchReplace(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void updateByEzParam() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("userAge", 1)
                .where().addFieldCondition("id", "1").done()
                .build();
        int ret = mapper.ezUpdate(ezUpdate);
        sqlSession.commit();
        log.info("更新条数{}", ret);
        sqlSession.close();
    }

    @Test
    public void batchUpdateByEzParam() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<EzUpdate> updates = new LinkedList<>();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("name", "张碧澄")
                .where().addFieldCondition("id", "1").done()
                .build();
        updates.add(ezUpdate);
        ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("name", "1")
                .where().addFieldCondition("id", "2").done()
                .build();
        updates.add(ezUpdate);
        mapper.ezBatchUpdate(updates);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateSetNull() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        try {
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
            EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("name", null)
                    .where().addFieldCondition("id", "1").done()
                    .build();
            mapper.ezUpdate(ezUpdate);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new RuntimeException(e);
        } finally {
            sqlSession.close();
        }
    }
}
