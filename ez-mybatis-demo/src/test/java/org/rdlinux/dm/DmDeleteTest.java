package org.rdlinux.dm;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.entity.UserOrg;
import org.rdlinux.ezmybatis.demo.mapper.UserMapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class DmDeleteTest extends DmBaseTest {

    // ==========================================
    // UserMapper (EzBaseMapper) Tests
    // ==========================================

    @Test
    public void delete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int ret = sqlSession.getMapper(UserMapper.class).delete(user);
        log.info("delete deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void deleteByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int ret = sqlSession.getMapper(UserMapper.class).deleteByTable(EntityTable.of(User.class), user);
        log.info("deleteByTable deleted {} records", ret);
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
        int ret = sqlSession.getMapper(UserMapper.class).batchDelete(users);
        sqlSession.commit();
        log.info("batchDelete deleted {} records", ret);
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
        int ret = sqlSession.getMapper(UserMapper.class).batchDeleteByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        log.info("batchDeleteByTable deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void deleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int ret = sqlSession.getMapper(UserMapper.class)
                .deleteById("016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        log.info("deleteById deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void deleteByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int ret = sqlSession.getMapper(UserMapper.class)
                .deleteByTableAndId(EntityTable.of(User.class), "016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        log.info("deleteByTableAndId deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void batchDeleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> userIds = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            userIds.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int ret = sqlSession.getMapper(UserMapper.class).batchDeleteById(userIds);
        sqlSession.commit();
        log.info("batchDeleteById deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void batchDeleteByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> userIds = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            userIds.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int ret = sqlSession.getMapper(UserMapper.class).batchDeleteByTableAndId(EntityTable.of(User.class), userIds);
        sqlSession.commit();
        log.info("batchDeleteByTableAndId deleted {} records", ret);
        sqlSession.close();
    }

    // ==========================================
    // EzMapper Tests
    // ==========================================

    @Test
    public void ezMapperDelete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int ret = sqlSession.getMapper(EzMapper.class).delete(user);
        log.info("ezMapperDelete deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void ezMapperDeleteByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int ret = sqlSession.getMapper(EzMapper.class).deleteByTable(EntityTable.of(User.class), user);
        log.info("ezMapperDeleteByTable deleted {} records", ret);
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
        int ret = sqlSession.getMapper(EzMapper.class).batchDelete(users);
        sqlSession.commit();
        log.info("ezMapperBatchDelete deleted {} records", ret);
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
        int ret = sqlSession.getMapper(EzMapper.class).batchDeleteByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        log.info("ezMapperBatchDeleteByTable deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void ezMapperDeleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int ret = sqlSession.getMapper(EzMapper.class)
                .deleteById(User.class, "016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        log.info("ezMapperDeleteById deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void ezMapperDeleteByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int ret = sqlSession.getMapper(EzMapper.class)
                .deleteByTableAndId(EntityTable.of(User.class), User.class, "016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        log.info("ezMapperDeleteByTableAndId deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchDeleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> userIds = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            userIds.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        userIds.add(null);
        int ret = sqlSession.getMapper(EzMapper.class).batchDeleteById(User.class, userIds);
        sqlSession.commit();
        sqlSession.close();
        log.info("ezMapperBatchDeleteById deleted {} records", ret);
    }

    @Test
    public void ezMapperBatchDeleteByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> userIds = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            userIds.add("016cdcdd76f94879ab3d24850514812b" + i);
        }
        int ret = sqlSession.getMapper(EzMapper.class).batchDeleteByTableAndId(EntityTable.of(User.class),
                User.class, userIds);
        sqlSession.commit();
        sqlSession.close();
        log.info("ezMapperBatchDeleteByTableAndId deleted {} records", ret);
    }

    @Test
    public void ezDelete() {
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
        log.info("Test ezDelete - Deleted {} records", ret);
        sqlSession.close();
    }

    @Test
    public void ezBatchDelete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<EzDelete> deletes = new LinkedList<>();
        EzDelete delete1 = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "56").done()
                .build();
        deletes.add(delete1);
        EzDelete delete2 = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "23").done()
                .build();
        deletes.add(delete2);

        sqlSession.getMapper(EzMapper.class).ezBatchDelete(deletes);
        sqlSession.commit();
        log.info("Test ezBatchDelete completed");
        sqlSession.close();
    }

    @Test
    public void deleteBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        String sql = "DELETE FROM user WHERE id = #{id}";
        Map<String, Object> param = new HashMap<>();
        param.put("id", "016cdcdd76f94879ab3d24850514812b");
        String realSql = "DELETE FROM ez_user WHERE id = #{id}";
        try {
            int ret = sqlSession.getMapper(EzMapper.class).deleteBySql(realSql, param);
            sqlSession.commit();
            log.info("Test deleteBySql result: {}", ret);
        } catch (Exception e) {
            log.error("Test deleteBySql exception", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
