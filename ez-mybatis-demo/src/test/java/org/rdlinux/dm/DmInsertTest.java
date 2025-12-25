package org.rdlinux.dm;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
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
public class DmInsertTest extends DmBaseTest {

    private static final com.github.javafaker.Faker faker = new com.github.javafaker.Faker(java.util.Locale.CHINA);

    private User buildUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName(faker.name().fullName());
        user.setUserAge(new Random().nextInt(100));
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
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = this.buildUser();
        int ret = sqlSession.getMapper(UserMapper.class).insert(user);
        sqlSession.commit();
        log.info("UserMapper.insert ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void insertByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = this.buildUser();
        int ret = sqlSession.getMapper(UserMapper.class).insertByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        log.info("UserMapper.insertByTable ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void batchInsert() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = this.buildUsers(2);
        int ret = sqlSession.getMapper(UserMapper.class).batchInsert(users);
        sqlSession.commit();
        log.info("UserMapper.batchInsert ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void batchInsertByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = this.buildUsers(2);
        int ret = sqlSession.getMapper(UserMapper.class).batchInsertByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        log.info("UserMapper.batchInsertByTable ret: {}", ret);
        sqlSession.close();
    }

    // =================================================================================================================
    // EzMapper Tests
    // =================================================================================================================

    @Test
    public void ezMapperInsert() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = this.buildUser();
        int ret = sqlSession.getMapper(EzMapper.class).insert(user);
        sqlSession.commit();
        log.info("EzMapper.insert ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void ezMapperInsertByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = this.buildUser();
        int ret = sqlSession.getMapper(EzMapper.class).insertByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        log.info("EzMapper.insertByTable ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchInsert() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = this.buildUsers(2);
        int ret = sqlSession.getMapper(EzMapper.class).batchInsert(users);
        sqlSession.commit();
        log.info("EzMapper.batchInsert ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void ezMapperBatchInsertByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = this.buildUsers(2);
        int ret = sqlSession.getMapper(EzMapper.class).batchInsertByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        log.info("EzMapper.batchInsertByTable ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void insertBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        String sql = "INSERT INTO ez_user (id, create_time, update_time, name, sex, age) " +
                "VALUES ('#id', '2021-12-30 11:58:23', '2021-12-30 11:58:23', " +
                "'王二', 1, 27)";
        sql = sql.replace("#id", UUID.randomUUID().toString().replace("-", ""));
        int ret = sqlSession.getMapper(EzMapper.class).insertBySql(sql, new HashMap<>());
        sqlSession.commit();
        log.info("EzMapper.insertBySql ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void insertByQuery() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        // 先插入一条，确保Query有数据
        mapper.insert(this.buildUser());

        EzQuery<User> query = EzQuery.builder(User.class)
                .from(EntityTable.of(User.class))
                .select()
                .addAll()
                .done()
                .page(1, 1)
                .build();

        // 使用SaveTest作为目标表以避免主键冲突（演示目的）
        EzQuery<SaveTest> saveTestQuery = EzQuery.builder(SaveTest.class)
                .from(EntityTable.of(SaveTest.class))
                .select()
                .addAll()
                .done()
                .page(1, 1)
                .build();
        int ret = mapper.insertByQuery(EntityTable.of(SaveTest.class), saveTestQuery);
        sqlSession.commit();
        log.info("EzMapper.insertByQuery ret: {}", ret);
        sqlSession.close();
    }


    // =================================================================================================================
    // JdbcInsertDao Tests
    // =================================================================================================================

    @Test
    public void jdbcInsert() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(sqlSession);
        User user = this.buildUser();
        int ret = jdbcInsertDao.insert(user);
        sqlSession.commit();
        log.info("JdbcInsertDao.insert ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void jdbcInsertByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(sqlSession);
        User user = this.buildUser();
        int ret = jdbcInsertDao.insertByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        log.info("JdbcInsertDao.insertByTable ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void jdbcBatchInsert() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(sqlSession);
        List<User> users = this.buildUsers(5);
        int ret = jdbcInsertDao.batchInsert(users);
        sqlSession.commit();
        log.info("JdbcInsertDao.batchInsert ret: {}", ret);
        sqlSession.close();
    }

    @Test
    public void jdbcBatchInsertByTable() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(sqlSession);
        List<User> users = this.buildUsers(5);
        int ret = jdbcInsertDao.batchInsertByTable(EntityTable.of(User.class), users);
        sqlSession.commit();
        log.info("JdbcInsertDao.batchInsertByTable ret: {}", ret);
        sqlSession.close();
    }


    // =================================================================================================================
    // Performance Tests (Retained but logging updated)
    // =================================================================================================================

    @Test
    public void loopInsertPerformanceTest() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
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
        sqlSession.commit();
        sqlSession.close();
        log.info("loopInsertPerformanceTest cost: {}ms", end - start);
    }

    @Test
    public void batchInsertPerformanceTest() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
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
        sqlSession.commit();
        sqlSession.close();
        log.info("batchInsertPerformanceTest cost: {}ms", end - start);
    }

    @Test
    public void jdbcBatchInsertPerformanceTest() throws SQLException {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        String sql = "INSERT INTO save_test ( a, b, c, d, e, f, g, h, i, j ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        long start = System.currentTimeMillis();
        for (int h = 0; h < 1; h++) {
            PreparedStatement statement = connection.prepareStatement(sql);
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
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
        sqlSession.commit();
        sqlSession.close();
        log.info("jdbcBatchInsertPerformanceTest cost: {}ms", end - start);
    }

    @Test
    public void jdbcBatchInsertPerformanceTest2() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        this.preheat(mapper);
        long start = System.currentTimeMillis();
        JdbcInsertDao jdbcBatchInsertDao = new JdbcInsertDao(sqlSession);
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
        sqlSession.commit();
        sqlSession.close();
        long end = System.currentTimeMillis();
        log.info("jdbcBatchInsertPerformanceTest2 cost: {}ms", end - start);
    }
}
