package org.rdlinux.mysql;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.java.entity.BaseEntity;
import org.rdlinux.ezmybatis.java.entity.Org;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.entity.UserOrg;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;
import org.rdlinux.ezmybatis.utils.StringHashMap;

import java.util.*;
import java.util.concurrent.CountDownLatch;

@Log4j2
public class MysqlSelectTest extends MysqlBaseTest {
    @Test
    public void selectObjectBySqlTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        List<User> users = mapper.selectObjectBySql(User.class, "select * from ez_user limit 0,5", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void selectOneObjectBySqlTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        User user = mapper.selectOneObjectBySql(User.class, "select * from ez_user limit 0,1", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void selectById() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(UserMapper.class)
                .selectById("04b7abcf2c454e56b1bc85f6599e19a5");
        System.out.println(JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void ezSelectById() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(EzMapper.class)
                .selectById(User.class, "04b7abcf2c454e56b1bc85f6599e19a5");
        System.out.println(JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void selectByTableAndId() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(UserMapper.class)
                .selectByTableAndId(EntityTable.of(User.class), "04b7abcf2c454e56b1bc85f6599e19a5");
        System.out.println(JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void ezSelectByTableAndId() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(EzMapper.class)
                .selectByTableAndId(EntityTable.of(User.class), User.class, "04b7abcf2c454e56b1bc85f6599e19a5");
        System.out.println(JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void selectByIds() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<String> ids = new LinkedList<>();
        ids.add("04b7abcf2c454e56b1bc85f6599e19a5");
        ids.add("085491774b2240688edb1b31772ff629");
        List<User> users = sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
        users = sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void selectByTableAndIds() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<String> ids = new LinkedList<>();
        ids.add("04b7abcf2c454e56b1bc85f6599e19a5");
        ids.add("085491774b2240688edb1b31772ff629");
        List<User> users = sqlSession.getMapper(UserMapper.class).selectByTableAndIds(
                EntityTable.of(User.class), ids);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezSelectByIds() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<String> ids = new LinkedList<>();
        ids.add("01e3ff9339f2427d9a66d3a8799de2c9");
        ids.add("1");
        List<User> users = sqlSession.getMapper(EzMapper.class).selectByIds(User.class, ids);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezSelectByTableAndIds() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<String> ids = new LinkedList<>();
        ids.add("01e3ff9339f2427d9a66d3a8799de2c9");
        ids.add("1");
        List<User> users = sqlSession.getMapper(EzMapper.class).selectByTableAndIds(EntityTable.of(User.class),
                User.class, ids);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ordeByTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addKeywords("age", "agg").done()
                .orderBy()
                .addField(User.Fields.userAge)
                .addField(User.Fields.name, OrderType.DESC)
                .done()
                .page(1, 5)
                .build();
        List<User> users = sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void groupByTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EntityTable table = EntityTable.of(User.class);
        Function countFunc = Function.builder(table).setFunName("COUNT").addKeywordsArg("*").build();
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.userAge)
                .addField(User.Fields.name)
                .addFunc(countFunc, "ct")
                .done()
                .groupBy()
                .addField(User.Fields.userAge)
                .addField(User.Fields.name)
                .done()
                .having()
                .addFuncCompareValueCondition(countFunc, Operator.gt, 1)
                .done()
                .build();
        List<StringHashMap> users = sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezSelectTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EntityTable userOrgTable = EntityTable.of(UserOrg.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .select(userOrgTable).addField(UserOrg.Fields.orgId)
                .done()
                .join(userOrgTable)
                .addFieldCompareCondition(BaseEntity.Fields.id, UserOrg.Fields.userId)
                .done()
                .orderBy()
                .addField(User.Fields.userAge)
                .addField(User.Fields.name, OrderType.DESC)
                .done()
                .page(1, 5)
                .build();
        List<User> users = sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }


    /**
     * 通用mapper结果类型并发测试
     */
    @Test
    public void normalConcurrentRetTypeTest() throws InterruptedException {
        EzQuery<User> userQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1).build();
        EzQuery<UserOrg> uoQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().page(1, 1).build();
        CountDownLatch downLatch = new CountDownLatch(2);
        CountDownLatch sync = new CountDownLatch(2);
        new Thread(() -> {
            SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
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
            sqlSession.close();
            sync.countDown();
        }).start();
        new Thread(() -> {
            SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
            EzMapper mapper = sqlSession.getMapper(EzMapper.class);
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
            sqlSession.close();
            sync.countDown();
        }).start();
        sync.await();
    }

    @Test
    public void ezQueryJoinTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select().addField("id").done()
                .where().addFieldCondition("name", "张三").done().page(1, 1).build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .join(EzQueryTable.of(sonQ)).addColumnCompareCondition("id", "id").done().build();
        List<User> ret = sqlSession.getMapper(UserMapper.class).query(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(ret));
    }

    @Test
    public void normalEzQueryJoinTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select().addField("id").done()
                .where().addFieldCondition("name", "张三").done().page(1, 1).build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .join(EzQueryTable.of(sonQ)).addColumnCompareCondition("id", "id").done().build();
        List<User> ret = sqlSession.getMapper(EzMapper.class).query(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(ret));
    }

    @Test
    public void ezQueryInTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select().addField("id")
                .done().page(1, 1)
                .build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .where().addColumnCondition("id", Operator.in, sonQ).done().build();
        List<User> ret = sqlSession.getMapper(UserMapper.class).query(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(ret));
    }

    @Test
    public void normalEzQueryInTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<String> sonQ = EzQuery.builder(String.class).from(EntityTable.of(User.class)).select()
                .addField("id").done().page(1, 5)
                .build();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class)).select().addAll().done()
                .where().addColumnCondition("id", Operator.in, sonQ).done().build();
        List<User> ret = sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
        sqlSession.close();
    }

    @Test
    public void selectBySql() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        HashMap<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("id", "1");
        List<User> users = sqlSession.getMapper(UserMapper.class).selectBySql("select name from ez_user" +
                " WHERE id = #{id}", sqlParam);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void normalSelectBySql() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        HashMap<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("id", "1e4f4409fe714135b8aa20112d913637");
        List<Map<String, Object>> users = sqlSession.getMapper(EzMapper.class).selectMapBySql(
                "select name from ez_user WHERE id = #{id}", sqlParam);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    /**
     * 交替测试, 主要测试ezMapper的返回类型是否正确
     */
    @Test
    public void alternateQueryTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
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
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.clearCache();
        users = userMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        //使用通用mapper查询
        EzMapper ezMapper = sqlSession.getMapper(EzMapper.class);
        users = ezMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        EzQuery<UserOrg> uosQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().build();
        List<UserOrg> uos = ezMapper.query(uosQuery);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(uos));
    }

    @Test
    public void alternateEzMapperQueryTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EntityTable userTable = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .page(1, 1)
                .build();
        EzMapper ezMapper = sqlSession.getMapper(EzMapper.class);
        List<User> users = ezMapper.query(query);
        System.out.println("用户" + JacksonUtils.toJsonString(users));
        EzQuery<UserOrg> uosQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().page(1, 1).build();
        List<UserOrg> uos = ezMapper.query(uosQuery);
        System.out.println("用户机构映射" + JacksonUtils.toJsonString(uos));
        EzQuery<Org> orgQuery = EzQuery.builder(Org.class).from(EntityTable.of(Org.class))
                .select().addAll().done().page(1, 1).build();
        List<Org> orgs = ezMapper.query(orgQuery);
        sqlSession.close();
        System.out.println("机构" + JacksonUtils.toJsonString(orgs));
    }

    @Test
    public void dbTableQueryTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(DbTable.of("ez_user"))
                .select().addAll().done()
                .where().addColumnCondition("id", "4")
                .done()
                .having().addColumnCondition("name", "张三").done()
                .build();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        sqlSession.close();
        log.info(JacksonUtils.toJsonString(users));
    }

    @Test
    public void countTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addField("name").done()
                .build();
        int i = sqlSession.getMapper(UserMapper.class).queryCount(query);
        sqlSession.close();
        System.out.println("总数" + i);
    }

    @Test
    public void normalCountTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addField("name").done()
                //.where().addColumnCondition("name", Operator.gt, 1).done()
                .build();
        int i = sqlSession.getMapper(EzMapper.class).queryCount(query);
        sqlSession.close();
        System.out.println("总数" + i);
    }

    @Test
    public void normalQuery() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        List<User> users = sqlSession.getMapper(EzMapper.class).query(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalQueryMap() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        EzMapper ezMapper = sqlSession.getMapper(EzMapper.class);
        List<StringHashMap> users = ezMapper.query(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void queryOne() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1)
                .build();
        User user = sqlSession.getMapper(UserMapper.class).queryOne(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalQueryOne() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1)
                .build();
        User user = sqlSession.getMapper(EzMapper.class).queryOne(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalQueryCount() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzQuery<Integer> query = EzQuery.builder(int.class).from(EntityTable.of(User.class))
                .select().addFieldCount("id", "idc").done().page(1, 1)
                .build();
        Integer count = sqlSession.getMapper(EzMapper.class).queryOne(query);
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(count));
    }


    @Test
    public void selectOneBySql() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(UserMapper.class).selectOneBySql("select * from ez_user " +
                "where id = '2c50ee58773f468c82013f73c08e7bc8'", new HashMap<>());
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectOneMapBySql() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        Map<String, Object> user = sqlSession.getMapper(EzMapper.class).selectOneMapBySql(
                "select * from ez_user where id = '1s'", new HashMap<>());
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectMapBySql() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        List<Map<String, Object>> users = sqlSession.getMapper(EzMapper.class)
                .selectMapBySql("select * from ez_user", new HashMap<>());
        sqlSession.close();
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void schemaTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);

        EntityTable table1 = EntityTable.of("ez_mybatis", User.class);
        EzQuery<User> query1 = EzQuery.builder(User.class).from(table1).select().addAll().done()
                .page(1, 1).build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query1)));

        EntityTable table2 = EntityTable.of(User.class);
        EzQuery<User> query2 = EzQuery.builder(User.class).from(table2).select().addAll().done()
                .page(1, 1).build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query2)));

        DbTable table3 = DbTable.of("ez_mybatis", "ez_user");
        EzQuery<User> query3 = EzQuery.builder(User.class).from(table3).select().addAll().done()
                .page(1, 1).build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query3)));
        sqlSession.close();
//        EntityTable table4 = EntityTable.of("test", User.class);
//        EzQuery<User> query4 = EzQuery.builder(User.class).from(table4).select().addAll().done()
//                .page(1, 1).build();
//        System.out.println(JacksonUtils.toJsonString(mapper.query(query4)));
    }

    @Test
    public void inSelectTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        String[] nameArray = new String[]{"牛儿", "网友", null};
        EzQuery<User> query = EzQuery.builder(User.class).from(table).select().addAll().done()
                .where()
                .addFieldCondition(User.Fields.name, Operator.in, "1")
                .addFieldCondition(User.Fields.name, Operator.in, Collections.singletonList("张三"))
                .addFieldCondition(User.Fields.name, Operator.in, Arrays.asList("李四", "王二"))
                .addFieldCondition(User.Fields.name, Operator.in, nameArray)
                .done().build();
        System.out.println(mapper.query(query));
        sqlSession.close();
    }

    @Test
    public void notInSelectTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        String[] nameArray = new String[]{"牛儿", "网友"};
        EzQuery<User> query = EzQuery.builder(User.class).from(table).select().addAll().done()
                .where()
                .addFieldCondition(User.Fields.name, Operator.notIn, "1")
                .addFieldCondition(User.Fields.name, Operator.notIn, Collections.singletonList("张三"))
                .addFieldCondition(User.Fields.name, Operator.notIn, Arrays.asList("李四", "王二"))
                .addFieldCondition(User.Fields.name, Operator.notIn, nameArray)
                .done()
                .page(1, 5)
                .build();
        System.out.println(mapper.query(query));
        sqlSession.close();
    }

    @Test
    public void btSelectTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(table).select().addAll().done()
                .where()
                .addFieldBtCondition(User.Fields.userAge, 1, 100)
                .done().build();
        System.out.println(mapper.query(query));
        sqlSession.close();
    }

    @Test
    public void noBtSelectTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(table).select().addAll().done()
                .where()
                .addFieldNotBtCondition(User.Fields.userAge, 1, 100)
                .done().build();
        System.out.println(mapper.query(query));
        sqlSession.close();
    }

    @Test
    public void distinctTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(table).select().distinct().addAll().done()
                .build();
        System.out.println(mapper.query(query));
        sqlSession.close();
    }

    @Test
    public void unionQuery() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<User> liSiQuery = EzQuery.builder(User.class).from(table).select()
                .addAll().done()
                .where()
                .addFieldCondition(User.Fields.name, "李四")
                .done()
                .build();

        EzQuery<User> wangErSongQuery = EzQuery.builder(User.class).from(table).select()
                .addAll().done()
                .where()
                .addFieldCondition(User.Fields.name, "王小二")
                .done()
                .build();

        EzQuery<User> wangErQuery = EzQuery.builder(User.class).from(table).select()
                .addAll().done()
                .where()
                .addFieldCondition(User.Fields.name, "王二")
                .done()
                .union(wangErSongQuery)
                .build();

        EzQuery<User> query = EzQuery.builder(User.class).from(table).select()
                .addAll()
                .done()
                .where()
                .addFieldCondition(User.Fields.name, "张三")
                .done()
                .union(liSiQuery)
                .unionAll(wangErQuery)
                .build();
        System.out.println(mapper.query(query));
        System.out.println(mapper.queryCount(query));
        sqlSession.close();
    }

    @Test
    public void functionQueryTest() {
        EntityTable table = EntityTable.of(User.class);
        Formula formula = Formula.builder(table).withField(User.Fields.userAge).addField(User.Fields.userAge)
                .done().build();
        Function sonFun = Function.builder(table).setFunName("CONCAT").addFieldArg(User.Fields.name)
                .addFieldArg(User.Fields.userAge).addValueArg("-").addFormulaArg(formula).build();
        Function function = Function.builder(table).setFunName("CONCAT").addValueArg("test-")
                .addFunArg(sonFun).build();
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addFunc(function, "nameAge")
                .done()
                .page(1, 2)
                .build();
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        List<StringHashMap> ret = mapper.query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
        sqlSession.close();
    }

    @Test
    public void formulaQueryTest() {
        EntityTable table = EntityTable.of(User.class);
        Function function = Function.builder(table).setFunName("GREATEST").addValueArg(1).addValueArg(2).build();

        Formula sonFormula = Formula.builder(table).withField(User.Fields.userAge).subtractField(User.Fields.userAge)
                .done().build();

        Formula formula = Formula.builder(table).withValue(100).subtractFun(function).addFormula(sonFormula)
                .addGroup()
                .withValue(100).multiplyValue(4).divideValue(2)
                .multiplyGroup()
                .withValue(1).addValue(2)
                .done()
                .done()
                .done().build();
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addFormula(formula, "nameAge")
                .done()
                .page(1, 2)
                .build();
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        List<StringHashMap> ret = mapper.query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
        sqlSession.close();
    }

    @Test
    public void functionDistinctTest() {
        EntityTable table = EntityTable.of(User.class);
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
                .from(table)
                .select()
                .addFunc(Function.builder(table).setFunName("COUNT").addDistinctFieldArg(User.Fields.name).build(),
                        "nameCount")
                .done()
                .page(1, 10)
                .build();
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        List<StringHashMap> ret = mapper.query(query);
        System.out.println(JacksonUtils.toJsonString(ret));
        sqlSession.close();
    }

    @Test
    public void caseWhenSelectTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        CaseWhen caseWhen = CaseWhen.builder(table).when().addFieldCondition(User.Fields.name, "张三")
                .then("1").els("2");
        EzQuery<User> query = EzQuery.builder(User.class).from(table)
                .select()
                .addCaseWhen(caseWhen, User.Fields.name).done()
                .page(1, 2)
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));
        sqlSession.close();
    }

    @Test
    public void groupConditionSelectTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(table).select().addAll().done()
                .where()
                .addFieldCondition(User.Fields.userAge, 1)
                .groupCondition(true, LogicalOperator.AND)
                .addFieldCondition(User.Fields.name, "1")
                .done()
                .addFieldCondition(User.Fields.name, Operator.notIn, Arrays.asList("李四", "王二"))
                .done()
                .page(1, 1)
                .build();
        System.out.println(mapper.query(query));
        sqlSession.close();
    }

    @Test
    public void groupTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addFormula(Formula.builder(table).withField(User.Fields.userAge)
                        .subtractField(User.Fields.sex).done().build(), "us")
                .addFormula(Formula.builder(table).withField(User.Fields.userAge)
                        .subtractField(User.Fields.sex).done().build(), "uc")
                .done()
                .groupBy()
                .addAlias("us")
                .addFormula(Formula.builder(table).withField(User.Fields.userAge)
                        .subtractField(User.Fields.sex).done().build())
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));

        query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.userAge)
                .done()
                .groupBy()
                .addField(User.Fields.userAge)
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));

        query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.userAge)
                .done()
                .groupBy()
                .addColumn("age")
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));

        query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.userAge)
                .done()
                .groupBy()
                .addField(User.Fields.userAge)
                .done()
                .having()
                .addFuncCompareValueCondition(
                        Function.builder(table).setFunName("COUNT").addKeywordsArg("*").build(),
                        Operator.ge, 1)
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));
        sqlSession.close();
    }

    @Test
    public void orderTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addAll()
                .done()
                .orderBy()
                .addField(User.Fields.name)
                .addFormula(Formula.builder(table).withField(User.Fields.userAge)
                        .subtractField(User.Fields.sex).done().build())
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));

        query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addAll()
                .done()
                .orderBy()
                .addField(User.Fields.userAge)
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));

        query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addAll()
                .done()
                .orderBy()
                .addColumn("age", OrderType.DESC)
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));
        sqlSession.close();
    }

    @Test
    public void btQueryTest() {
        SqlSession sqlSession = MysqlBaseTest.sqlSessionFactory.openSession();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EntityTable table = EntityTable.of(User.class);
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addAll()
                .done()
                .where()
                .addFieldBtCondition(User.Fields.userAge, 1, 7)
                .addFieldNotBtCondition(User.Fields.userAge, 3, 4)
                .addColumnBtCondition(User.Fields.name, "1", "7")
                .addColumnNotBtCondition(User.Fields.name, "3", "4")
                .done()
                .build();
        System.out.println(JacksonUtils.toJsonString(mapper.query(query)));
        sqlSession.close();
    }
}
