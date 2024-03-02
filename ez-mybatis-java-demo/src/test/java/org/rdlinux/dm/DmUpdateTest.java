package org.rdlinux.dm;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;

@Log4j2
public class DmUpdateTest extends DmBaseTest {
    @Test
    public void update() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).update(user);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchUpdate() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
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
    }

    @Test
    public void replace() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(UserMapper.class).replace(user);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchReplace() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
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
    }

    @Test
    public void updateByEzParam() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class))
                .set().setField("userAge", 1).done()
                .where().addFieldCondition("id", "1").done()
                .build();
        int ret = mapper.update(ezUpdate);
        sqlSession.commit();
        log.info("更新条数{}", ret);
    }

    @Test
    public void batchUpdateByEzParam() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<EzUpdate> updates = new LinkedList<>();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class))
                .set().setField("userAge", 1).done()
                .where().addFieldCondition("id", "1").done()
                .build();
        updates.add(ezUpdate);
        ezUpdate = EzUpdate.update(EntityTable.of(User.class))
                .set().setField("userAge", 2).done()
                .where().addFieldCondition("id", "2").done()
                .build();
        updates.add(ezUpdate);
        mapper.batchUpdate(updates);
        sqlSession.commit();
    }

    @Test
    public void limitUpdate() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzUpdate ezUpdate = EzUpdate.update(DbTable.of("xxx"))
                .set().setColumn("xxx", "T1PN")
                .done()
//                .where()
//                .addColumnCondition("xxx", "XX")
//                .done()
                .limit(1)
                .build();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        mapper.ezUpdate(ezUpdate);
        sqlSession.rollback();
    }
}
