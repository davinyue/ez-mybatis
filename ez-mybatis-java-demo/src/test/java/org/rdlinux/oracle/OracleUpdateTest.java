package org.rdlinux.oracle;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.dao.JdbcUpdateDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.Keywords;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.BaseEntity;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Log4j2
public class OracleUpdateTest extends OracleBaseTest {
    @Test
    public void update() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName(null);
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).update(user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert);
    }

    @Test
    public void updateByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).updateByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert);
    }

    @Test
    public void ezMapperUpdate() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
    public void ezMapperUpdateByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(EzMapper.class).updateByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert);
    }

    @Test
    public void batchUpdate() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
    public void batchUpdateByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
        int insert = sqlSession.getMapper(UserMapper.class).batchUpdateByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchUpdate() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
    public void ezMapperBatchUpdateByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
        int insert = sqlSession.getMapper(EzMapper.class).batchUpdateByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void replace() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(UserMapper.class).replace(user);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void replaceByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(UserMapper.class).replaceByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperReplace() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(EzMapper.class).replace(user);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperReplaceByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(EzMapper.class).replaceByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void batchReplace() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
    public void batchReplaceByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
        int insert = sqlSession.getMapper(UserMapper.class).batchReplaceByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchReplace() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
    public void ezMapperBatchReplaceByTable() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
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
        int insert = sqlSession.getMapper(EzMapper.class).batchReplaceByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void updateByEzParam() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzUpdate ezUpdate = EzUpdate.update(table)
                .set()
                .setField(User.Fields.userAge, CaseWhen.builder(table).when()
                        .addFieldCondition(User.Fields.userAge, 2).then(10).els(20))
                .setColumn("name", "张三")
                .done()
                .where().addFieldCondition("id", "1").done()
                .build();
        int ret = mapper.ezUpdate(ezUpdate);
        sqlSession.commit();
        log.info("更新条数{}", ret);
        sqlSession.close();
    }

    @Test
    public void batchUpdateByEzParam() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        List<EzUpdate> updates = new LinkedList<>();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class))
                .set().setField("name", "张碧澄").done()
                .where().addFieldCondition("id", "1").done()
                .build();
        updates.add(ezUpdate);
        ezUpdate = EzUpdate.update(EntityTable.of(User.class))
                .set().setField("name", "1").done()
                .where().addFieldCondition("id", "2").done()
                .build();
        updates.add(ezUpdate);
        mapper.ezBatchUpdate(updates);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateSetNull() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        try {
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
            EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class))
                    .set().setField("name", null).done()
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

    @Test
    public void caseWhenUpdate() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        try {
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
            EntityTable table = EntityTable.of(User.class);
            CaseWhen caseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四").build();
            EzUpdate ezUpdate = EzUpdate.update(table)
                    .set().setField(User.Fields.name, caseWhen).done()
                    .where().addFieldCondition(BaseEntity.Fields.id, "03512cd707384c8ab1b813077b9ab891").done()
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

    @Test
    public void formulaUpdateTest() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        try {
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
            EntityTable table = EntityTable.of(User.class);
            Formula formula = Formula.builder(table).withField(User.Fields.userAge).addValue(10).done().build();
            EzUpdate ezUpdate = EzUpdate.update(table)
                    .set().setField(User.Fields.userAge, formula).done()
                    .where()
                    .addFieldCondition(BaseEntity.Fields.id, "1").done()
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

    @Test
    public void functionUpdateTest() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        try {
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
            EntityTable table = EntityTable.of(User.class);
            Function function = Function.builder(table).setFunName("GREATEST").addFieldArg(User.Fields.userAge)
                    .addValueArg(100).build();
            EzUpdate ezUpdate = EzUpdate.update(table)
                    .set().setField(User.Fields.userAge, function)
                    .setField(BaseEntity.Fields.updateTime, Keywords.of("SYSDATE"))
                    .done()
                    .where()
                    .addFieldCondition(BaseEntity.Fields.id, "1").done()
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

    @Test
    public void jdbcUpdateTest() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        User user1 = new User();
        user1.setName("王值");
        user1.setId("08649915562c421f858236f60fd652e5");
        users.add(user1);

        User user2 = new User();
        user2.setId("12e68306a3de4a03b0010b446a5ebd8e");
        users.add(user2);
        JdbcUpdateDao jdbcInsertDao = new JdbcUpdateDao(sqlSession);
        int ct = jdbcInsertDao.batchUpdate(users);
        System.out.println("批量更新" + ct + "条");
        User user = new User();
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setId("038f530bad3745d3a75f584296368501");
        user.setName("王芳");
        user.setUserAge(8);
        user.setSex(User.Sex.MAN);
        int sCt = jdbcInsertDao.update(user);
        System.out.println("单条更新" + sCt + "条");
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void jdbcReplaceTest() {
        SqlSession sqlSession = OracleBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        User user1 = new User();
        user1.setName(null);
        user1.setUpdateTime(new Date());
        user1.setCreateTime(new Date());
        user1.setUserAge(12);
        user1.setSex(User.Sex.MAN);
        user1.setId("08649915562c421f858236f60fd652e5");
        users.add(user1);

        User user2 = new User();
        user2.setName("杨玉婷");
        user2.setUpdateTime(new Date());
        user2.setCreateTime(new Date());
        user2.setUserAge(18);
        user2.setSex(User.Sex.WOMAN);
        user2.setId("12e68306a3de4a03b0010b446a5ebd8e");
        users.add(user2);
        JdbcUpdateDao jdbcInsertDao = new JdbcUpdateDao(sqlSession);
        int ct = jdbcInsertDao.batchReplace(users);
        System.out.println("批量更新" + ct + "条");
        User user = new User();
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setId("038f530bad3745d3a75f584296368501");
        user.setName("王芳");
        user.setUserAge(8);
        user.setSex(User.Sex.MAN);
        int sCt = jdbcInsertDao.replace(user);
        System.out.println("单条更新" + sCt + "条");
        sqlSession.commit();
        sqlSession.close();
    }
}
