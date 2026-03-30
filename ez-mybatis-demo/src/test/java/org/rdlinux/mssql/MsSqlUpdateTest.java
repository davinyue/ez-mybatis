package org.rdlinux.mssql;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.dao.JdbcUpdateDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.mapper.UserMapper;

import java.util.*;

@Slf4j
public class MsSqlUpdateTest extends MsSqlBaseTest {
    @Test
    public void userMapperUpdateTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setName("王二");
        user.setName(null);
        user.setAge(27);
        user.setSex(User.Sex.MAN);
        int insert = this.sqlSession.getMapper(UserMapper.class).update(user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperUpdateTest result: {}", insert);
    }

    @Test
    public void userMapperUpdateByTableTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setAge(27);
        user.setSex(User.Sex.MAN);
        int insert = this.sqlSession.getMapper(UserMapper.class).updateByTable(EntityTable.of(User.class), user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperUpdateByTableTest result: {}", insert);
    }

    @Test
    public void ezMapperUpdateTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setAge(27);
        user.setSex(User.Sex.MAN);
        int insert = this.sqlSession.getMapper(EzMapper.class).update(user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperUpdateTest result: {}", insert);
    }

    @Test
    public void ezMapperUpdateByTableTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setAge(27);
        user.setSex(User.Sex.MAN);
        int insert = this.sqlSession.getMapper(EzMapper.class).updateByTable(EntityTable.of(User.class), user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperUpdateByTableTest result: {}", insert);
    }

    @Test
    public void userMapperBatchUpdateTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(UserMapper.class).batchUpdate(users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperBatchUpdateTest result: {}", insert);
    }

    @Test
    public void userMapperBatchUpdateByTableTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(UserMapper.class).batchUpdateByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperBatchUpdateByTableTest result: {}", insert);
    }

    @Test
    public void ezMapperBatchUpdateTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(EzMapper.class).batchUpdate(users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperBatchUpdateTest result: {}", insert);
    }

    @Test
    public void ezMapperBatchUpdateByTableTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(EzMapper.class).batchUpdateByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperBatchUpdateByTableTest result: {}", insert);
    }

    @Test
    public void userMapperReplaceTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setName("王二");
        int insert = this.sqlSession.getMapper(UserMapper.class).replace(user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperReplaceTest result: {}", insert);
    }

    @Test
    public void userMapperReplaceByTableTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setName("王二");
        int insert = this.sqlSession.getMapper(UserMapper.class).replaceByTable(EntityTable.of(User.class), user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperReplaceByTableTest result: {}", insert);
    }

    @Test
    public void ezMapperReplaceTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setName("王二");
        int insert = this.sqlSession.getMapper(EzMapper.class).replace(user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperReplaceTest result: {}", insert);
    }

    @Test
    public void ezMapperReplaceByTableTest() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        user.setName("王二");
        int insert = this.sqlSession.getMapper(EzMapper.class).replaceByTable(EntityTable.of(User.class), user);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperReplaceByTableTest result: {}", insert);
    }

    @Test
    public void userMapperBatchReplaceTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(UserMapper.class).batchReplace(users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperBatchReplaceTest result: {}", insert);
    }

    @Test
    public void userMapperBatchReplaceByTableTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(UserMapper.class).batchReplaceByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("userMapperBatchReplaceByTableTest result: {}", insert);
    }

    @Test
    public void ezMapperBatchReplaceTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(EzMapper.class).batchReplace(users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperBatchReplaceTest result: {}", insert);
    }

    @Test
    public void ezMapperBatchReplaceByTableTest() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            String id = this.getOneUserId();
            User user = new User();
            user.setId(id);
            user.setName("芳" + i + 1);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setAge(i);
            }
            users.add(user);
        }
        int insert = this.sqlSession.getMapper(EzMapper.class).batchReplaceByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperBatchReplaceByTableTest result: {}", insert);
    }

    @Test
    public void ezMapperUpdateByEzParamTest() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzUpdate ezUpdate = EzUpdate.update(table)
                .set()
                .add(table.field(User.Fields.age).set(1))
                .add(table.column("name").set("张三"))
                .done()
                .where().addFieldCondition("id", this.getOneUserId()).done()
                .build();
        int insert = mapper.ezUpdate(ezUpdate);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);
        log.info("ezMapperUpdateByEzParamTest result: {}", insert);
        Formula userAgeFm = Formula.builder(table.field(User.Fields.age)).add(1).done().build();
        ezUpdate = EzUpdate.update(table)
                .set()
                .add(table.field(User.Fields.age).set(userAgeFm))
                .add(table.column("name").set("张三"))
                .done()
                .where().addFieldCondition("id", this.getOneUserId()).done()
                .build();
        insert = mapper.ezUpdate(ezUpdate);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);
        log.info("ezMapperUpdateByEzParamTest result: {}", insert);

        ezUpdate = EzUpdate.update(table)
                .set()
                .add(table.field(User.Fields.age).set(CaseWhen.builder(table).when()
                        .addFieldCondition(User.Fields.age, 2).then(10).els(20)))
                .add(table.column("name").set("张三"))
                .done()
                .where().addFieldCondition("id", this.getOneUserId()).done()
                .build();
        insert = mapper.ezUpdate(ezUpdate);
        this.sqlSession.commit();
        Assert.assertTrue(insert > 0);

        log.info("ezMapperUpdateByEzParamTest result: {}", insert);
    }

    @Test
    public void ezMapperBatchUpdateByEzParamTest() {
        List<EzUpdate> updates = new LinkedList<>();
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzUpdate ezUpdate = EzUpdate.update(table)
                .set().add(table.field(User.Fields.name).set("张碧澄")).done()
                .where().addFieldCondition("id", this.getOneUserId()).done()
                .build();
        updates.add(ezUpdate);
        ezUpdate = EzUpdate.update(table)
                .set().add(table.field(User.Fields.name).set("1")).done()
                .where().addFieldCondition("id", "2").done()
                .build();
        updates.add(ezUpdate);
        mapper.ezBatchUpdate(updates);
        this.sqlSession.commit();
        log.info("ezMapperBatchUpdateByEzParamTest executed");
    }

    @Test
    public void ezMapperUpdateSetNullTest() {
        try {
            EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
            EntityTable table = EntityTable.of(User.class);
            EzUpdate ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.name).setToNull()).done()
                    .where().addFieldCondition("id", this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);
            this.sqlSession.commit();
        } catch (Exception e) {
            this.sqlSession.rollback();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ezMapperCaseWhenUpdateTest() {
        try {
            EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
            EntityTable table = EntityTable.of(User.class);

            Function function = Function.builder("CONCAT").addArg(table.field(User.Fields.name))
                    .addArg("-2").build();

            CaseWhen sonCaseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四")
                    .els("王二1");

            CaseWhen caseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四")
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then(function)

                    .when()
                    .addFieldCondition(User.Fields.name, "王二1").then(sonCaseWhen)
                    .els("王二1");

            EzUpdate ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.name).set(caseWhen)).done()
                    .where().addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);

            caseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四")
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then(function)
                    .when()
                    .addFieldCondition(User.Fields.name, "王二1").then(sonCaseWhen)
                    .els(sonCaseWhen);
            ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.name).set(caseWhen)).done()
                    .where().addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);

            caseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四")
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then(function)
                    .when()
                    .addFieldCondition(User.Fields.name, "王二1").then(sonCaseWhen)
                    .build();
            ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.name).set(caseWhen)).done()
                    .where().addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);

            caseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四")
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then(function)
                    .when()
                    .addFieldCondition(User.Fields.name, "王二1").then(sonCaseWhen)
                    .els(function);
            ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.name).set(caseWhen)).done()
                    .where().addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);

            caseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四")
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then(function)
                    .when()
                    .addFieldCondition(User.Fields.name, "王二1").then(sonCaseWhen)
                    .els(TableColumn.of(table, "name"));
            ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.name).set(caseWhen)).done()
                    .where().addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);

            caseWhen = CaseWhen.builder(table)
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then("李四")
                    .when()
                    .addFieldCondition(User.Fields.name, "张三1").then(function)
                    .when()
                    .addFieldCondition(User.Fields.name, "王二1").then(sonCaseWhen)
                    .els(EntityField.of(table, User.Fields.name));
            ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.name).set(caseWhen)).done()
                    .where().addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);

            this.sqlSession.commit();
        } catch (Exception e) {
            this.sqlSession.rollback();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ezMapperFormulaUpdateTest() {
        try {
            EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
            EntityTable table = EntityTable.of(User.class);
            Formula formula = Formula.builder(table.field(User.Fields.age)).add(10).done().build();
            EzUpdate ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.age).set(formula)).done()
                    .where()
                    .addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);
            this.sqlSession.commit();
        } catch (Exception e) {
            this.sqlSession.rollback();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ezMapperFunctionUpdateTest() {
        try {
            EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
            EntityTable table = EntityTable.of(User.class);
            Function function = Function.builder("GREATEST").addArg(EntityField.of(table, User.Fields.age))
                    .addArg(100).build();
            EzUpdate ezUpdate = EzUpdate.update(table)
                    .set().add(table.field(User.Fields.age).set(function))
                    .done()
                    .where()
                    .addFieldCondition(BaseEntity.Fields.id, this.getOneUserId()).done()
                    .build();
            mapper.ezUpdate(ezUpdate);
            this.sqlSession.commit();
        } catch (Exception e) {
            this.sqlSession.rollback();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void jdbcUpdateDaoUpdateTest() {
        List<User> users = new LinkedList<>();
        User user1 = new User();
        user1.setName("王值");
        user1.setAge(null);
        user1.setId(this.getOneUserId());
        users.add(user1);

        User user2 = new User();
        user2.setId(this.getOneUserId());
        user2.setName(null);
        user2.setAge(19);
        users.add(user2);
        JdbcUpdateDao jdbcInsertDao = new JdbcUpdateDao(this.sqlSession);
        int ct = jdbcInsertDao.batchUpdate(users);
        log.info("jdbcUpdateDaoUpdateTest batch result: {}", ct);
        User user = new User();
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setId(this.getOneUserId());
        user.setName("王芳");
        user.setAge(8);
        user.setSex(User.Sex.MAN);
        int sCt = jdbcInsertDao.update(user);
        Assert.assertTrue(sCt > 0);
        log.info("jdbcUpdateDaoUpdateTest single result: {}", sCt);
        this.sqlSession.commit();
    }

    @Test
    public void jdbcUpdateDaoPartialUpdateTest() {
        JdbcUpdateDao jdbcInsertDao = new JdbcUpdateDao(this.sqlSession);
        User user = new User();
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setId(this.getOneUserId());
        user.setName("王芳");
        user.setAge(8);
        user.setSex(User.Sex.MAN);
        int sCt = jdbcInsertDao.update(user, Arrays.asList(User.Fields.name, User.Fields.age));
        Assert.assertTrue(sCt > 0);
        log.info("jdbcUpdateDaoPartialUpdateTest single result: {}", sCt);

        List<User> users = new LinkedList<>();
        User user1 = new User();
        user1.setName("王值");
        user1.setAge(20);
        user1.setSex(User.Sex.MAN);
        user1.setId(this.getOneUserId());
        users.add(user1);

        User user2 = new User();
        user2.setId(this.getOneUserId());
        user2.setName("王值1");
        user2.setAge(19);
        users.add(user2);
        int ct = jdbcInsertDao.batchUpdate(users, Arrays.asList(User.Fields.name, User.Fields.age));
        log.info("jdbcUpdateDaoPartialUpdateTest batch result: {}", ct);


        this.sqlSession.commit();
    }

    @Test
    public void jdbcUpdateDaoReplaceTest() {
        List<User> users = new LinkedList<>();
        User user1 = new User();
        user1.setName(null);
        user1.setUpdateTime(new Date());
        user1.setCreateTime(new Date());
        user1.setAge(12);
        user1.setSex(User.Sex.MAN);
        user1.setId(this.getOneUserId());
        users.add(user1);

        User user2 = new User();
        user2.setName("杨玉婷");
        user2.setUpdateTime(new Date());
        user2.setCreateTime(new Date());
        user2.setAge(18);
        user2.setSex(User.Sex.WOMAN);
        user2.setId(this.getOneUserId());
        users.add(user2);
        JdbcUpdateDao jdbcInsertDao = new JdbcUpdateDao(this.sqlSession);
        int ct = jdbcInsertDao.batchReplace(users);
        log.info("jdbcUpdateDaoReplaceTest batch result: {}", ct);
        User user = new User();
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setId(this.getOneUserId());
        user.setName("王芳");
        user.setAge(8);
        user.setSex(User.Sex.MAN);
        int sCt = jdbcInsertDao.replace(user);
        Assert.assertTrue(sCt > 0);
        log.info("jdbcUpdateDaoReplaceTest single result: {}", sCt);
        this.sqlSession.commit();
    }


    @Test
    public void ezMapperUpdateBySqlTest() {
        try {
            EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
            String sql = "UPDATE ez_user SET name = #{name} WHERE id = #{id}";
            Map<String, Object> param = new HashMap<>();
            param.put("name", "SQL Update");
            param.put("id", this.getOneUserId());
            Integer result = mapper.updateBySql(sql, param);
            Assert.assertNotNull(result);
            Assert.assertTrue(result > 0);
            log.info("ezMapperUpdateBySqlTest result: {}", result);
            this.sqlSession.commit();
        } catch (Exception e) {
            this.sqlSession.rollback();
            throw new RuntimeException(e);
        }
    }
}
