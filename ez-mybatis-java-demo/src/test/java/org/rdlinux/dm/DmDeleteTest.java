package org.rdlinux.dm;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
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
public class DmDeleteTest extends DmBaseTest {
    @Test
    public void delete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = sqlSession.getMapper(UserMapper.class).delete(user);
        System.out.println(delete);
        sqlSession.close();
    }

    @Test
    public void deleteByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = sqlSession.getMapper(UserMapper.class).deleteByTable(EntityTable.of(User.class), user);
        System.out.println(delete);
        sqlSession.close();
    }

    @Test
    public void ezMapperDelete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = sqlSession.getMapper(EzMapper.class).delete(user);
        System.out.println(delete);
        sqlSession.close();
    }

    @Test
    public void ezMapperDeleteByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = sqlSession.getMapper(EzMapper.class).deleteByTable(EntityTable.of(User.class), user);
        System.out.println(delete);
        sqlSession.close();
    }

    @Test
    public void batchDelete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b" + i);
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchDelete(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void batchDeleteByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b" + i);
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchDeleteByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchDelete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b" + i);
            users.add(user);
        }
        int insert = sqlSession.getMapper(EzMapper.class).batchDelete(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchDeleteByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b" + i);
            users.add(user);
        }
        int insert = sqlSession.getMapper(EzMapper.class).batchDeleteByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void deleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int insert = sqlSession.getMapper(UserMapper.class)
                .deleteById("016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void deleteByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int insert = sqlSession.getMapper(UserMapper.class)
                .deleteByTableAndId(EntityTable.of(User.class), "016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperDeleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int insert = sqlSession.getMapper(EzMapper.class)
                .deleteById(User.class, "016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperDeleteByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int insert = sqlSession.getMapper(EzMapper.class)
                .deleteByTableAndId(EntityTable.of(User.class), User.class, "016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void batchDeleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchDeleteById(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void batchDeleteByTableId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchDeleteByTableAndId(EntityTable.of(User.class), users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchDeleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        users.add(null);
        int insert = sqlSession.getMapper(EzMapper.class).batchDeleteById(User.class, users);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert);
    }

    @Test
    public void ezMapperBatchDeleteByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int insert = sqlSession.getMapper(EzMapper.class).batchDeleteByTableAndId(EntityTable.of(User.class),
                User.class, users);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert);
    }

    @Test
    public void deleteByParam() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EntityTable userTable = EntityTable.of(User.class);
        EntityTable uoTable = EntityTable.of(UserOrg.class);
        EzDelete delete = EzDelete.delete(userTable).delete(uoTable)
                .join(uoTable)
                .addFieldCompareCondition("id", "userId")
                .done()
                .where()
                .addFieldCondition("id", "56")
                .addFieldCondition("userAge", "56")
                .done()
                .build();
        int ret = sqlSession.getMapper(EzMapper.class).ezDelete(delete);
        sqlSession.commit();
        log.info("删除{}条", ret);
        sqlSession.close();
    }

    @Test
    public void batchDeleteByParam() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<EzDelete> deletes = new LinkedList<>();
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "56").done()
                .build();
        deletes.add(delete);
        delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "23").done()
                .build();
        deletes.add(delete);
        sqlSession.getMapper(EzMapper.class).ezBatchDelete(deletes);
        sqlSession.commit();
        sqlSession.close();
    }
}
