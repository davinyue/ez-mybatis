package ink.dvc.mysql;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.Org;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.entity.UserOrg;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;
import org.rdlinux.ezmybatis.utils.StringHashMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Log4j2
public class MysqlSelectTest extends MysqlBaseTest {
    @Test
    public void selectById() {
        User user = MysqlBaseTest.sqlSession.getMapper(UserMapper.class)
                .selectById("01e3ff9339f2427d9a66d3a8799de2c9");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalSelectById() {
        User user = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).selectById(User.class,
                "01e3ff9339f2427d9a66d3a8799de2c9");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    /**
     * 通用mapper结果类型并发测试
     */
    @Test
    public void normalConcurrentRetTypeTest() throws InterruptedException {
        EzMapper mapper = MysqlBaseTest.sqlSession.getMapper(EzMapper.class);
        EzQuery<User> userQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1).build();
        EzQuery<UserOrg> uoQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().page(1, 1).build();
        CountDownLatch downLatch = new CountDownLatch(2);
        CountDownLatch sync = new CountDownLatch(2);
        new Thread(() -> {
            downLatch.countDown();
            try {
                downLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < 1000; i++) {
                List<User> ret = mapper.query(userQuery);
                System.out.println("用户:" + JacksonUtils.toJsonString(ret));
            }
            sync.countDown();
        }).start();
        new Thread(() -> {
            downLatch.countDown();
            try {
                downLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < 1000; i++) {
                List<UserOrg> ret = mapper.query(uoQuery);
                System.out.println("用户机构:" + JacksonUtils.toJsonString(ret));
            }
            sync.countDown();
        }).start();
        sync.await();
    }

    @Test
    public void selectByIds() {
        List<String> ids = new LinkedList<>();
        ids.add("01e3ff9339f2427d9a66d3a8799de2c9");
        ids.add("1");
        List<User> users = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
        users = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).selectByIds(User.class, ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalSelectByIds() {
        List<String> ids = new LinkedList<>();
        ids.add("01e3ff9339f2427d9a66d3a8799de2c9");
        ids.add("1");
        List<User> users = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).selectByIds(User.class, ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQueryJoinTest() {
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select().add("id").done()
                .where().addFieldCondition("name", "张三").done().page(1, 1).build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .join(sonQ).addColumnCompareCondition("id", "id").done().build();
        List<User> ret = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
    }

    @Test
    public void normalEzQueryJoinTest() {
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select().add("id").done()
                .where().addFieldCondition("name", "张三").done().page(1, 1).build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .join(sonQ).addColumnCompareCondition("id", "id").done().build();
        List<User> ret = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
    }

    @Test
    public void ezQueryInTest() {
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select().add("id").done()
                .build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .where().addColumnCondition("id", Operator.in, sonQ).done().build();
        List<User> ret = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
    }

    @Test
    public void normalEzQueryInTest() {
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select().add("id").done()
                .build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .where().addColumnCondition("id", Operator.in, sonQ).done().build();
        List<User> ret = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
    }

    @Test
    public void selectBySql() {
        HashMap<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("id", "1");
        List<User> users = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).selectBySql("select name from ez_user" +
                " WHERE id = #{id}", sqlParam);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalSelectBySql() {
        HashMap<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("id", "1");
        List<Map<String, Object>> users = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).selectMapBySql(
                "select name from ez_user WHERE id = #{id}", sqlParam);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    /**
     * 交替测试, 主要测试ezMapper的返回类型是否正确
     */
    @Test
    public void alternateQueryTest() {
        EntityTable userTable = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .join(EntityTable.of(UserOrg.class))
                .addFieldCompareCondition("id", "userId")
                .joinTableCondition()
                .addFieldCondition("orgId", "2")
                .done()
                .page(1, 2)
                .build();
        //先使用专用mapper查询
        UserMapper userMapper = MysqlBaseTest.sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        MysqlBaseTest.sqlSession.clearCache();
        users = userMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        //使用通用mapper查询
        EzMapper ezMapper = MysqlBaseTest.sqlSession.getMapper(EzMapper.class);
        users = ezMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        EzQuery<UserOrg> uosQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().build();
        List<UserOrg> uos = ezMapper.query(uosQuery);
        System.out.println(JacksonUtils.toJsonString(uos));
    }

    @Test
    public void alternateEzMapperQueryTest() {
        EntityTable userTable = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .page(1, 1)
                .build();
        EzMapper ezMapper = MysqlBaseTest.sqlSession.getMapper(EzMapper.class);
        List<User> users = ezMapper.query(query);
        System.out.println("用户" + JacksonUtils.toJsonString(users));
        EzQuery<UserOrg> uosQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().page(1, 1).build();
        List<UserOrg> uos = ezMapper.query(uosQuery);
        System.out.println("用户机构映射" + JacksonUtils.toJsonString(uos));
        EzQuery<Org> orgQuery = EzQuery.builder(Org.class).from(EntityTable.of(Org.class))
                .select().addAll().done().page(1, 1).build();
        List<Org> orgs = ezMapper.query(orgQuery);
        System.out.println("机构" + JacksonUtils.toJsonString(orgs));
    }

    @Test
    public void dbTableQueryTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(DbTable.of("ez_user"))
                .select().addAll().done()
                .where().addColumnCondition("id", "4").done()
                .having().addColumnCondition("name", "张三").done()
                .build();
        UserMapper userMapper = MysqlBaseTest.sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        log.info(JacksonUtils.toJsonString(users));
    }

    @Test
    public void countTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().add("name").done()
                .build();
        int i = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void normalCountTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().add("name").done()
                //.where().addColumnCondition("name", Operator.gt, 1).done()
                .build();
        int i = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void normalQuery() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        List<User> users = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalQueryMap() {
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        EzMapper ezMapper = MysqlBaseTest.sqlSession.getMapper(EzMapper.class);
        List<StringHashMap> users = ezMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void queryOne() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1)
                .build();
        User user = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalQueryOne() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1)
                .build();
        User user = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalQueryCount() {
        EzQuery<Integer> query = EzQuery.builder(int.class).from(EntityTable.of(User.class))
                .select().addCount("id").done().page(1, 1)
                .build();
        Integer count = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(count));
    }


    @Test
    public void selectOneBySql() {
        User user = MysqlBaseTest.sqlSession.getMapper(UserMapper.class).selectOneBySql("select * from ez_user " +
                "where id = '2c50ee58773f468c82013f73c08e7bc8'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectOneMapBySql() {
        Map<String, Object> user = MysqlBaseTest.sqlSession.getMapper(EzMapper.class).selectOneMapBySql(
                "select * from ez_user where id = '1s'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectMapBySql() {
        List<Map<String, Object>> users = MysqlBaseTest.sqlSession.getMapper(EzMapper.class)
                .selectMapBySql("select * from ez_user", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(users));
    }
}
