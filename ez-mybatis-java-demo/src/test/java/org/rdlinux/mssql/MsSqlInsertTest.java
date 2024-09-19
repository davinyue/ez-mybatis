package org.rdlinux.mssql;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.dao.JdbcInsertDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.SaveTest;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class MsSqlInsertTest extends MsSqlBaseTest {
    @Test
    public void insert() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).insert(user);
        System.out.println(insert);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void ezInsert() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert1 = sqlSession.getMapper(EzMapper.class).insert(user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert1);
    }

    @Test
    public void insertByTable() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).insertByTable(EntityTable.of(User.class), user);
        System.out.println(insert);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void ezInsertByTable() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王二");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert1 = sqlSession.getMapper(EzMapper.class).insertByTable(EntityTable.of(User.class), user);
        sqlSession.commit();
        sqlSession.close();
        System.out.println(insert1);
    }

    @Test
    public void batchInsert() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();

        long start = System.currentTimeMillis();
        for (int h = 0; h < 1; h++) {
            List<User> users = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                User user = new User();
                user.setUpdateTime(new Date());
                user.setCreateTime(new Date());
                user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                user.setName("芳" + (i + 1));
                user.setUserAge(1 + i);
                user.setSex(User.Sex.MAN);
                users.add(user);
            }
            sqlSession.getMapper(UserMapper.class).batchInsert(users);
            sqlSession.commit();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        sqlSession.close();
    }

    @Test
    public void ezBatchInsert() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        long start = System.currentTimeMillis();
        for (int h = 0; h < 1; h++) {
            List<User> users = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                User user = new User();
                user.setUpdateTime(new Date());
                user.setCreateTime(new Date());
                user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                user.setName("芳" + (i + 1));
                user.setUserAge(1 + i);
                user.setSex(User.Sex.MAN);
                users.add(user);
            }
            sqlSession.getMapper(EzMapper.class).batchInsert(users);
            sqlSession.commit();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        sqlSession.close();
    }

    @Test
    public void batchInsertByTable() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();

        long start = System.currentTimeMillis();
        for (int h = 0; h < 1; h++) {
            List<User> users = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                User user = new User();
                user.setUpdateTime(new Date());
                user.setCreateTime(new Date());
                user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                user.setName("芳" + (i + 1));
                user.setUserAge(1 + i);
                user.setSex(User.Sex.MAN);
                users.add(user);
            }
            int i = sqlSession.getMapper(UserMapper.class).batchInsertByTable(EntityTable.of(User.class), users);
            System.out.println(i);
            sqlSession.commit();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        sqlSession.close();
    }

    @Test
    public void ezBatchInsertByTable() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();

        long start = System.currentTimeMillis();
        for (int h = 0; h < 1; h++) {
            List<User> users = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                User user = new User();
                user.setUpdateTime(new Date());
                user.setCreateTime(new Date());
                user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                user.setName("芳" + (i + 1));
                user.setUserAge(1 + i);
                user.setSex(User.Sex.MAN);
                users.add(user);
            }
            int i = sqlSession.getMapper(EzMapper.class).batchInsertByTable(EntityTable.of(User.class), users);
            System.out.println(i);
            sqlSession.commit();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        sqlSession.close();
    }

    @Test
    public void batchInsert1() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUpdateTime(new Date());
            user.setCreateTime(new Date());
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + (i + 1));
            user.setUserAge(27 + i);
            user.setSex(User.Sex.MAN);
            users.add(user);
        }
        int insert = sqlSession.getMapper(EzMapper.class).batchInsert(users);
        sqlSession.commit();
        System.out.println(insert);
        sqlSession.close();
    }

    @Test
    public void jdbcInsertTest() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUpdateTime(new Date());
            user.setCreateTime(new Date());
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("芳" + (i + 1));
            user.setUserAge(27 + i);
            user.setSex(User.Sex.MAN);
            users.add(user);
        }
        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(sqlSession);
        int ct = jdbcInsertDao.batchInsert(users);
        System.out.println("批量插入" + ct + "条");
        User user = new User();
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName("王芳");
        user.setUserAge(8);
        user.setSex(User.Sex.MAN);
        int sCt = jdbcInsertDao.insert(user);
        System.out.println("单条插入" + sCt + "条");
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void insertBySql() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        String sql = "INSERT INTO \"ez_user\" (\"id\", \"create_time\", \"update_time\", \"name\", \"sex\", \"age\") " +
                "VALUES ('#id', '2021-12-30 11:58:23', '2021-12-30 11:58:23', " +
                "'王二', 1, 27);\n";
        sql = sql.replace("#id", UUID.randomUUID().toString().replace("-", ""));
        Integer integer = sqlSession.getMapper(EzMapper.class).insertBySql(sql,
                new HashMap<>());
        System.out.println(integer);
        sqlSession.close();
    }

    /**
     * 预热
     */
    private void preheat(EzMapper ezMapper) {
        for (int i = 0; i < 10; i++) {
            ezMapper.selectMapBySql("select 1", new HashMap<>());
        }
    }

    @Test
    public void loopInsertPerformanceTest() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
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
        //循环插入耗时:13459
        System.out.println("循环插入耗时:" + (end - start));
    }

    @Test
    public void batchInsertPerformanceTest() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
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
        //批量插入耗时:1669， 1578, 7093
        System.out.println("批量插入耗时:" + (end - start));
    }

    @Test
    public void jdbcBatchInsertPerformanceTest() throws SQLException {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        String sql = "INSERT INTO save_test ( \"a\", \"b\", \"c\", \"d\", \"e\", \"f\", \"g\", \"h\", \"i\", \"j\" ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
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
        //批量插入耗时:1669， 1578, 7093
        System.out.println("批量插入耗时:" + (end - start));
    }

    @Test
    public void jdbcBatchInsertTest() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
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
        System.out.println("jdbc批量插入耗时:" + (end - start));
    }

    @Test
    public void insertByQueryTest() {
        SqlSession sqlSession = MsSqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzQuery<SaveTest> query = EzQuery.builder(SaveTest.class)
                .from(EntityTable.of(SaveTest.class))
                .select()
                .addAll()
                .done()
                .page(1, 1)
                .build();
        mapper.insertByQuery(EntityTable.of(SaveTest.class), query);
        sqlSession.commit();
        sqlSession.close();
    }
}
