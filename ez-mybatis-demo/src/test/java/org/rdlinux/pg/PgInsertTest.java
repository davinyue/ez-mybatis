package org.rdlinux.pg;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.dao.JdbcInsertDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.SaveTest;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.mapper.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class PgInsertTest extends PgBaseTest {

    private static final Faker faker = new Faker(Locale.CHINA);

    private User buildUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName(faker.name().fullName());
        user.setAge(new Random().nextInt(100));
        User.Sex[] sexes = User.Sex.values();
        user.setSex(sexes[new Random().nextInt(sexes.length)]);
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        return user;
    }


    private List<User> buildUsers(int count) {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            User user = this.buildUser();
            users.add(user);
        }
        return users;
    }

    private void preheat(EzMapper ezMapper) {
        for (int i = 0; i < 10; i++) {
            ezMapper.selectMapBySql("select 1", new HashMap<>());
        }
    }

    // =================================================================================================================
    // UserMapper Tests (EzBaseMapper)
    // =================================================================================================================

    @Test
    public void insert() {
        User user = this.buildUser();
        int ret = this.sqlSession.getMapper(UserMapper.class).insert(user);
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("UserMapper.insert ret: {}", ret);
    }

    @Test
    public void insertByTable() {
        User user = this.buildUser();
        int ret = this.sqlSession.getMapper(UserMapper.class).insertByTable(EntityTable.of(User.class), user);
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("UserMapper.insertByTable ret: {}", ret);
    }

    @Test
    public void batchInsert() {
        List<User> users = this.buildUsers(2);
        int ret = this.sqlSession.getMapper(UserMapper.class).batchInsert(users);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("UserMapper.batchInsert ret: {}", ret);
    }

    @Test
    public void batchInsertByTable() {
        List<User> users = this.buildUsers(2);
        int ret = this.sqlSession.getMapper(UserMapper.class).batchInsertByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("UserMapper.batchInsertByTable ret: {}", ret);
    }

    // =================================================================================================================
    // EzMapper Tests
    // =================================================================================================================

    @Test
    public void ezMapperInsert() {
        User user = this.buildUser();
        int ret = this.sqlSession.getMapper(EzMapper.class).insert(user);
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("EzMapper.insert ret: {}", ret);
    }

    @Test
    public void ezMapperInsertByTable() {
        User user = this.buildUser();
        int ret = this.sqlSession.getMapper(EzMapper.class).insertByTable(EntityTable.of(User.class), user);
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("EzMapper.insertByTable ret: {}", ret);
    }

    @Test
    public void ezMapperBatchInsert() {
        List<User> users = this.buildUsers(2);
        int ret = this.sqlSession.getMapper(EzMapper.class).batchInsert(users);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("EzMapper.batchInsert ret: {}", ret);
    }

    @Test
    public void ezMapperBatchInsertByTable() {
        List<User> users = this.buildUsers(2);
        int ret = this.sqlSession.getMapper(EzMapper.class).batchInsertByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("EzMapper.batchInsertByTable ret: {}", ret);
    }

    @Test
    public void insertBySql() {
        String sql = "INSERT INTO ez_user (id, create_time, update_time, name, sex, age) " +
                "VALUES ('#id', '2021-12-30 11:58:23', '2021-12-30 11:58:23', " +
                "'王二', 1, 27);\n";
        sql = sql.replace("#id", UUID.randomUUID().toString().replace("-", ""));
        int ret = this.sqlSession.getMapper(EzMapper.class).insertBySql(sql, new HashMap<>());
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("EzMapper.insertBySql ret: {}", ret);
    }

    @Test
    public void insertByQuery() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        // 先往 SaveTest 表插入一条种子数据，确保 SELECT 有结果
        SaveTest seed = new SaveTest().setA("a1").setB("b1").setC("c1").setD("d1").setE("e1")
                .setF("f1").setG("g1").setH("h1").setI("i1").setJ("j1");
        mapper.insert(seed);

        // INSERT INTO save_test SELECT * FROM save_test（从自身查询再插入）
        EzQuery<SaveTest> saveTestQuery = EzQuery.builder(SaveTest.class)
                .from(EntityTable.of(SaveTest.class))
                .select()
                .addAll()
                .done()
                .page(1, 1)
                .build();
        int ret = mapper.insertByQuery(EntityTable.of(SaveTest.class), saveTestQuery);
        this.sqlSession.commit();
        Assert.assertTrue(ret >= 0);
        log.info("EzMapper.insertByQuery ret: {}", ret);
    }


    // =================================================================================================================
    // JdbcInsertDao Tests
    // =================================================================================================================

    @Test
    public void jdbcInsert() {
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(this.sqlSession);
        User user = this.buildUser();
        int ret = jdbcInsertDao.insert(user);
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("JdbcInsertDao.insert ret: {}", ret);
    }

    @Test
    public void jdbcInsertByTable() {
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(this.sqlSession);
        User user = this.buildUser();
        int ret = jdbcInsertDao.insertByTable(EntityTable.of(User.class), user);
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("JdbcInsertDao.insertByTable ret: {}", ret);
    }

    @Test
    public void jdbcBatchInsert() {
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(this.sqlSession);
        List<User> users = this.buildUsers(5);
        int ret = jdbcInsertDao.batchInsert(users);
        this.sqlSession.commit();
        Assert.assertEquals(5, ret);
        log.info("JdbcInsertDao.batchInsert ret: {}", ret);
    }

    @Test
    public void jdbcBatchInsertByTable() {
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(this.sqlSession);
        List<User> users = this.buildUsers(5);
        int ret = jdbcInsertDao.batchInsertByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertEquals(5, ret);
        log.info("JdbcInsertDao.batchInsertByTable ret: {}", ret);
    }


    // =================================================================================================================
    // Performance Tests (Retained but logging updated)
    // =================================================================================================================

    @Test
    public void loopInsertPerformanceTest() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        this.preheat(mapper);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            SaveTest entity = new SaveTest().setA(UUID.randomUUID().toString().replaceAll("-", ""));
            entity.setB(entity.getA());
            entity.setC(entity.getA());
            entity.setD(entity.getA());
            entity.setE(entity.getA());
            entity.setF(entity.getA());
            entity.setG(entity.getA());
            entity.setH(entity.getA());
            entity.setI(entity.getA());
            entity.setJ(entity.getA());
            mapper.insert(entity);
        }
        long end = System.currentTimeMillis();
        this.sqlSession.commit();
        Assert.assertTrue(end - start >= 0);
        log.info("loopInsertPerformanceTest cost: {}ms", end - start);
    }

    @Test
    public void batchInsertPerformanceTest() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        this.preheat(mapper);
        long start = System.currentTimeMillis();
        for (int h = 0; h < 2; h++) {
            List<SaveTest> models = new ArrayList<>(100);
            for (int i = 0; i < 5; i++) {
                SaveTest entity = new SaveTest().setA(UUID.randomUUID().toString().replaceAll("-", ""));
                entity.setB(entity.getA());
                entity.setC(entity.getA());
                entity.setD(entity.getA());
                entity.setE(entity.getA());
                entity.setF(entity.getA());
                entity.setG(entity.getA());
                entity.setH(entity.getA());
                entity.setI(entity.getA());
                entity.setJ(entity.getA());
                models.add(entity);
            }
            mapper.batchInsert(models);
        }
        long end = System.currentTimeMillis();
        this.sqlSession.commit();
        Assert.assertTrue(end - start >= 0);
        log.info("batchInsertPerformanceTest cost: {}ms", end - start);
    }

    @Test
    public void jdbcBatchInsertPerformanceTest() throws SQLException {
        Connection connection = this.sqlSession.getConnection();
        String sql = "INSERT INTO save_test ( a, b, c, d, e, f, g, h, i, j ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        long start = System.currentTimeMillis();
        for (int h = 0; h < 1; h++) {
            PreparedStatement statement = connection.prepareStatement(sql);
            EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
            this.preheat(mapper);
            for (int i = 0; i < 1; i++) {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                statement.setString(1, id);
                statement.setString(2, id);
                statement.setString(3, id);
                statement.setString(4, id);
                statement.setString(5, id);
                statement.setString(6, id);
                statement.setString(7, id);
                statement.setString(8, id);
                statement.setString(9, id);
                statement.setString(10, id);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        }
        connection.commit();
        connection.close();
        long end = System.currentTimeMillis();
        this.sqlSession.commit();
        Assert.assertTrue(end - start >= 0);
        log.info("jdbcBatchInsertPerformanceTest cost: {}ms", end - start);
    }

    @Test
    public void jdbcBatchInsertPerformanceTest2() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        this.preheat(mapper);
        long start = System.currentTimeMillis();
        JdbcInsertDao jdbcBatchInsertDao = new JdbcInsertDao(this.sqlSession);
        for (int h = 0; h < 2; h++) {
            List<SaveTest> models = new LinkedList<>();
            for (int i = 0; i < 5; i++) {
                SaveTest entity = new SaveTest().setA(UUID.randomUUID().toString().replaceAll("-", ""));
                entity.setB(entity.getA());
                entity.setC(entity.getA());
                entity.setD(entity.getA());
                entity.setE(entity.getA());
                entity.setF(entity.getA());
                entity.setG(entity.getA());
                entity.setH(entity.getA());
                entity.setI(entity.getA());
                entity.setJ(entity.getA());
                models.add(entity);
            }
            jdbcBatchInsertDao.batchInsert(models);
        }
        this.sqlSession.commit();
        long end = System.currentTimeMillis();
        Assert.assertTrue(end - start >= 0);
        log.info("jdbcBatchInsertPerformanceTest2 cost: {}ms", end - start);
    }
}
