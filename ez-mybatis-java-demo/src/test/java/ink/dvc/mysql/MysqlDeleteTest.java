package ink.dvc.mysql;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.entity.UserOrg;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;

@Log4j2
public class MysqlDeleteTest extends MysqlBaseTest {
    @Test
    public void delete() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).delete(user);
        System.out.println(delete);
    }

    @Test
    public void delete1() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).delete(user);
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
        int insert = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).batchDelete(users);
        MysqlBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchDelete1() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            users.add(user);
        }
        int insert = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).batchDelete(users);
        MysqlBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteById() {
        int insert = MysqlBaseTest.sqlSession.getMapper(UserMapper.class)
                .deleteById("016cdcdd76f94879ab3d24850514812b");
        MysqlBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteById1() {
        int insert = MysqlBaseTest.sqlSession.getMapper(EzMapper.class)
                .deleteById(User.class, "016cdcdd76f94879ab3d24850514812b");
        MysqlBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchDeleteById() {
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int insert = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).batchDeleteById(users);
        MysqlBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchDeleteById1() {
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int insert = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).batchDeleteById(User.class, users);
        MysqlBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteByParam() {
        EntityTable userTable = EntityTable.of(User.class);
        EntityTable uoTable = EntityTable.of(UserOrg.class);
        EzDelete delete = EzDelete.delete(userTable).delete(uoTable)
                .join(uoTable)
                .addFieldCompareCondition("id", "userId")
                .done()
                .where().addFieldCondition("id", "56").done()
                .build();
        int ret = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).ezDelete(delete);
        MysqlBaseTest.sqlSession.commit();
        log.info("删除{}条", ret);
    }

    @Test
    public void batchDeleteByParam() {
        List<EzDelete> deletes = new LinkedList<>();
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "56").done()
                .build();
        deletes.add(delete);
        delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "23").done()
                .build();
        deletes.add(delete);
        MysqlBaseTest.sqlSession.getMapper(EzMapper.class).ezBatchDelete(deletes);
        MysqlBaseTest.sqlSession.commit();
    }
}
