package ink.dvc.mysql;

import lombok.extern.log4j.Log4j2;
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
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(UserMapper.class).update(user);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void update1() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class).update(user);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void batchUpdate() {
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
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(UserMapper.class).batchUpdate(users);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void batchUpdate1() {
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
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class).batchUpdate(users);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void replace() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(UserMapper.class).replace(user);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void replace1() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class).replace(user);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void batchReplace() {
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
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(UserMapper.class).batchReplace(users);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void batchReplace1() {
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
        int insert = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class).batchReplace(users);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        System.out.println(insert);
    }

    @Test
    public void updateByEzParam() {
        EzMapper mapper = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("userAge", 1)
                .where().addFieldCondition("id", "1").done()
                .build();
        int ret = mapper.ezUpdate(ezUpdate);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
        log.info("更新条数{}", ret);
    }

    @Test
    public void batchUpdateByEzParam() {
        List<EzUpdate> updates = new LinkedList<>();
        EzMapper mapper = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("name", "张碧澄")
                .where().addFieldCondition("id", "1").done()
                .build();
        updates.add(ezUpdate);
        ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("name", "1")
                .where().addFieldCondition("id", "2").done()
                .build();
        updates.add(ezUpdate);
        mapper.batchEzUpdate(updates);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
    }

    @Test
    public void updateSetNull() {
        EzMapper mapper = MysqlBaseTest.sqlSessionFactory.openSession().getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).setField("name", null)
                .where().addFieldCondition("id", "2").done()
                .build();
        mapper.ezUpdate(ezUpdate);
        MysqlBaseTest.sqlSessionFactory.openSession().commit();
    }
}
