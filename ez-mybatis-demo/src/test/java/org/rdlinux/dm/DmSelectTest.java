package org.rdlinux.dm;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.dao.JdbcInsertDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.entity.UserOrg;
import org.rdlinux.ezmybatis.demo.mapper.UserMapper;
import org.rdlinux.ezmybatis.enumeration.Operator;
import org.rdlinux.ezmybatis.enumeration.OrderType;
import org.rdlinux.ezmybatis.utils.StringHashMap;
import org.rdlinux.luava.json.JacksonUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class DmSelectTest extends DmBaseTest {

    // Helper to get a valid ID for testing
    private String getOneUserId(SqlSession sqlSession) {
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        User user = mapper.selectOneObjectBySql(User.class, "SELECT * FROM ez_user LIMIT 1", new HashMap<>());
        return user != null ? user.getId() : "0";
    }

    // Helper to get valid IDs
    private List<String> getUserIds(SqlSession sqlSession, int limit) {
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        List<User> users = mapper.selectObjectBySql(User.class, "SELECT * FROM ez_user LIMIT " + limit, new HashMap<>());
        List<String> ids = new ArrayList<>();
        for (User user : users) {
            ids.add(user.getId());
        }
        return ids;
    }

    // Ensure data exists
    private void ensureData(SqlSession sqlSession) {
        if (this.getUserIds(sqlSession, 1).isEmpty()) {
            JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(sqlSession);
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setName("TestUser");
            user.setUserAge(18);
            user.setSex(User.Sex.MAN);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            jdbcInsertDao.insert(user);
            sqlSession.commit();
        }
    }


    // =================================================================================================================
    // UserMapper Tests (EzBaseMapper Methods)
    // =================================================================================================================

    @Test
    public void userSelectById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        String id = this.getOneUserId(sqlSession);
        User user = sqlSession.getMapper(UserMapper.class).selectById(id);
        log.info("UserMapper.selectById: {}", JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void userSelectByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        String id = this.getOneUserId(sqlSession);
        User user = sqlSession.getMapper(UserMapper.class).selectByTableAndId(EntityTable.of(User.class), id);
        log.info("UserMapper.selectByTableAndId: {}", JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void userSelectByIds() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        List<String> ids = this.getUserIds(sqlSession, 2);
        List<User> users = sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        log.info("UserMapper.selectByIds: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void userSelectByTableAndIds() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        List<String> ids = this.getUserIds(sqlSession, 2);
        List<User> users = sqlSession.getMapper(UserMapper.class).selectByTableAndIds(EntityTable.of(User.class), ids);
        log.info("UserMapper.selectByTableAndIds: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void userSelectOneBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        String id = this.getOneUserId(sqlSession);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        User user = sqlSession.getMapper(UserMapper.class).selectOneBySql("SELECT * FROM ez_user WHERE id = #{id}", params);
        log.info("UserMapper.selectOneBySql: {}", JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void userSelectBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        List<User> users = sqlSession.getMapper(UserMapper.class).selectBySql("SELECT * FROM ez_user LIMIT 2", new HashMap<>());
        log.info("UserMapper.selectBySql: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    // =================================================================================================================
    // EzMapper Tests
    // =================================================================================================================

    @Test
    public void ezSelectById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        String id = this.getOneUserId(sqlSession);
        User user = sqlSession.getMapper(EzMapper.class).selectById(User.class, id);
        log.info("EzMapper.selectById: {}", JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void ezSelectByTableAndId() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        String id = this.getOneUserId(sqlSession);
        User user = sqlSession.getMapper(EzMapper.class).selectByTableAndId(EntityTable.of(User.class), User.class, id);
        log.info("EzMapper.selectByTableAndId: {}", JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void ezSelectByIds() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        List<String> ids = this.getUserIds(sqlSession, 2);
        List<User> users = sqlSession.getMapper(EzMapper.class).selectByIds(User.class, ids);
        log.info("EzMapper.selectByIds: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezSelectByTableAndIds() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        List<String> ids = this.getUserIds(sqlSession, 2);
        List<User> users = sqlSession.getMapper(EzMapper.class).selectByTableAndIds(EntityTable.of(User.class),
                User.class, ids);
        log.info("EzMapper.selectByTableAndIds: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezSelectOneMapBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        Map<String, Object> map = sqlSession.getMapper(EzMapper.class).selectOneMapBySql("SELECT * FROM ez_user LIMIT 1", new HashMap<>());
        log.info("EzMapper.selectOneMapBySql: {}", JacksonUtils.toJsonString(map));
        sqlSession.close();
    }

    @Test
    public void ezSelectMapBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        List<Map<String, Object>> maps = sqlSession.getMapper(EzMapper.class).selectMapBySql("SELECT * FROM ez_user LIMIT 2", new HashMap<>());
        log.info("EzMapper.selectMapBySql: {}", JacksonUtils.toJsonString(maps));
        sqlSession.close();
    }

    @Test
    public void ezSelectOneObjectBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        User user = sqlSession.getMapper(EzMapper.class).selectOneObjectBySql(User.class, "SELECT * FROM ez_user LIMIT 1", new HashMap<>());
        log.info("EzMapper.selectOneObjectBySql: {}", JacksonUtils.toJsonString(user));
        sqlSession.close();
    }

    @Test
    public void ezSelectObjectBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        List<User> users = sqlSession.getMapper(EzMapper.class).selectObjectBySql(User.class, "SELECT * FROM ez_user LIMIT 2", new HashMap<>());
        log.info("EzMapper.selectObjectBySql: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    // =================================================================================================================
    // EzQuery Comprehensive Tests
    // =================================================================================================================

    /**
     * 通用mapper结果类型并发测试
     */
    @Test
    public void ezQueryConcurrentSelectRetTypeTest() throws InterruptedException, java.util.concurrent.ExecutionException {
        this.ensureData(DmBaseTest.sqlSessionFactory.openSession());
        EzQuery<User> userQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1).build();
        EzQuery<UserOrg> uoQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().page(1, 1).build();

        int threadCount = 10;
        int loopCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            futures.add(executorService.submit(() -> {
                try {
                    startLatch.await();
                    SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
                    EzMapper mapper = sqlSession.getMapper(EzMapper.class);
                    try {
                        for (int j = 0; j < loopCount; j++) {
                            if (index % 2 == 0) {
                                List<User> ret = mapper.query(userQuery);
                                Assert.assertNotNull(ret);
                                if (!ret.isEmpty()) {
                                    Assert.assertTrue(ret.get(0) instanceof User);
                                }
                            } else {
                                List<UserOrg> ret = mapper.query(uoQuery);
                                Assert.assertNotNull(ret);
                                if (!ret.isEmpty()) {
                                    Assert.assertTrue(ret.get(0) instanceof UserOrg);
                                }
                            }
                        }
                    } finally {
                        sqlSession.close();
                    }
                    return "OK";
                } catch (Exception e) {
                    log.error("Concurrent test failed", e);
                    return e.getMessage();
                }
            }));
        }

        startLatch.countDown();
        for (java.util.concurrent.Future<String> future : futures) {
            String result = future.get();
            org.junit.Assert.assertEquals("OK", result);
        }
        executorService.shutdown();
    }

    @Test
    public void ezQueryBasicSelect() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        // Select all fields
        EzQuery<User> queryAll = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .page(1, 2)
                .build();
        List<User> usersAll = sqlSession.getMapper(EzMapper.class).query(queryAll);
        log.info("EzQuery Basic Select All: {}", JacksonUtils.toJsonString(usersAll));

        // Select specific fields
        EzQuery<User> querySpecific = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addField(BaseEntity.Fields.id).addField(User.Fields.name).done()
                .page(1, 2)
                .build();
        List<User> usersSpecific = sqlSession.getMapper(EzMapper.class).query(querySpecific);
        log.info("EzQuery Basic Select Specific: {}", JacksonUtils.toJsonString(usersSpecific));
        sqlSession.close();
    }

    @Test
    public void ezQueryConditions() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);

        // 1. EQ (=)
        EzQuery<User> eqQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.sex, Operator.eq, User.Sex.MAN).done()
                .page(1, 1).build();
        log.info("EzQuery EQ: {}", JacksonUtils.toJsonString(mapper.query(eqQuery)));

        // 2. NE (!=)
        EzQuery<User> neQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.ne, -1).done()
                .page(1, 1).build();
        log.info("EzQuery NE: {}", JacksonUtils.toJsonString(mapper.query(neQuery)));

        // 3. GT (>)
        EzQuery<User> gtQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.gt, 10).done()
                .page(1, 1).build();
        log.info("EzQuery GT: {}", JacksonUtils.toJsonString(mapper.query(gtQuery)));

        // 4. GE (>=)
        EzQuery<User> geQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.ge, 18).done()
                .page(1, 1).build();
        log.info("EzQuery GE: {}", JacksonUtils.toJsonString(mapper.query(geQuery)));

        // 5. LT (<)
        EzQuery<User> ltQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.lt, 100).done()
                .page(1, 1).build();
        log.info("EzQuery LT: {}", JacksonUtils.toJsonString(mapper.query(ltQuery)));

        // 6. LE (<=)
        EzQuery<User> leQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.le, 18).done()
                .page(1, 1).build();
        log.info("EzQuery LE: {}", JacksonUtils.toJsonString(mapper.query(leQuery)));

        // 7. IS NULL
        EzQuery<User> isNullQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldIsNullCondition(BaseEntity.Fields.id).done() // Use dedicated method
                .page(1, 1).build();
        log.info("EzQuery IS NULL: {}", JacksonUtils.toJsonString(mapper.query(isNullQuery)));

        // 8. IS NOT NULL
        EzQuery<User> isNotNullQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldIsNotNullCondition(BaseEntity.Fields.id).done() // Use dedicated method
                .page(1, 1).build();
        log.info("EzQuery IS NOT NULL: {}", JacksonUtils.toJsonString(mapper.query(isNotNullQuery)));

        // 9. IN
        EzQuery<User> inQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.in, Arrays.asList(18, 20, 27)).done()
                .page(1, 1).build();
        log.info("EzQuery IN: {}", JacksonUtils.toJsonString(mapper.query(inQuery)));

        // 10. NOT IN
        EzQuery<User> notInQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.notIn, Arrays.asList(-1, -2)).done()
                .page(1, 1).build();
        log.info("EzQuery NOT IN: {}", JacksonUtils.toJsonString(mapper.query(notInQuery)));

        // 11. LIKE
        EzQuery<User> likeQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, Operator.like, "Test%").done()
                .page(1, 1).build();
        log.info("EzQuery LIKE: {}", JacksonUtils.toJsonString(mapper.query(likeQuery)));

        // 12. NOT LIKE (unlike)
        EzQuery<User> unlikeQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, Operator.unlike, "NonExis%").done()
                .page(1, 1).build();
        log.info("EzQuery NOT LIKE: {}", JacksonUtils.toJsonString(mapper.query(unlikeQuery)));

        // 13. BETWEEN
        EzQuery<User> betweenQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldBtCondition(User.Fields.userAge, 10, 30).done() // Use dedicated method
                .page(1, 1).build();
        log.info("EzQuery BETWEEN: {}", JacksonUtils.toJsonString(mapper.query(betweenQuery)));

        // 14. NOT BETWEEN
        EzQuery<User> notBetweenQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldNotBtCondition(User.Fields.userAge, 100, 200).done() // Use dedicated method
                .page(1, 1).build();
        log.info("EzQuery NOT BETWEEN: {}", JacksonUtils.toJsonString(mapper.query(notBetweenQuery)));

        // 15. REGEXP
        EzQuery<User> regexpQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, Operator.regexp, "^Test.*").done()
                .page(1, 1).build();
        log.info("EzQuery REGEXP: {}", JacksonUtils.toJsonString(mapper.query(regexpQuery)));

        sqlSession.close();
    }

    @Test
    public void ezQueryJoin() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EntityTable userTable = EntityTable.of(User.class);
        EntityTable userOrgTable = EntityTable.of(UserOrg.class);

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
                .from(userTable)
                .select().addAll().done()
                .join(userOrgTable)
                .addFieldCompareCondition(BaseEntity.Fields.id, UserOrg.Fields.userId)
                .done()
                .page(1, 2)
                .build();

        List<StringHashMap> result = sqlSession.getMapper(EzMapper.class).query(query);
        log.info("EzQuery Join: {}", JacksonUtils.toJsonString(result));
        sqlSession.close();
    }

    @Test
    public void ezQueryGroupByHaving() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        EntityTable table = EntityTable.of(User.class);

        // Count users by age
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.userAge)
                .addFieldCount(BaseEntity.Fields.id, "count")
                .done()
                .groupBy().addField(User.Fields.userAge).done()
                .having().addCondition(Function.builder(table).setFunName("COUNT").addKeywordsArg("*").build(),
                        Operator.ge, 0).done()
                .build();

        List<StringHashMap> result = sqlSession.getMapper(EzMapper.class).query(query);
        log.info("EzQuery GroupBy Having: {}", JacksonUtils.toJsonString(result));
        sqlSession.close();
    }

    @Test
    public void ezQueryOrderBy() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);

        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .orderBy()
                .addField(User.Fields.userAge, OrderType.ASC)
                .addField(BaseEntity.Fields.createTime, OrderType.DESC)
                .done()
                .page(1, 5)
                .build();
        List<User> users = sqlSession.getMapper(EzMapper.class).query(query);
        log.info("EzQuery OrderBy: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezQueryUnion() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);

        EzQuery<User> q1 = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.sex, User.Sex.MAN).done()
                .build();

        EzQuery<User> q2 = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.sex, User.Sex.WOMAN).done()
                .build();

        EzQuery<User> unionQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, "NonExistent").done() // Main query empty
                .union(q1)
                .unionAll(q2)
                .page(1, 10)
                .build();

        List<User> users = sqlSession.getMapper(EzMapper.class).query(unionQuery);
        log.info("EzQuery Union: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezQuerySubQuery() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);

        // Select where ID in (Select ID from ...)
        EzQuery<String> subInfo = EzQuery.builder(String.class).from(EntityTable.of(User.class))
                .select().addField(BaseEntity.Fields.id).done()
                .build();

        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where()
                .addColumnCondition(BaseEntity.Fields.id, Operator.in, subInfo)
                .done()
                .build();

        List<User> users = sqlSession.getMapper(EzMapper.class).query(query);
        log.info("EzQuery SubQuery: {}", JacksonUtils.toJsonString(users));
        sqlSession.close();
    }

    @Test
    public void ezQueryFunctionsAndFormulas() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        EntityTable table = EntityTable.of(User.class);

        // Formula: age + 1
        Formula agePlusOne = Formula.builder(table).withField(User.Fields.userAge).addValue(1).done().build();

        // Function: CONCAT(name, ' - ', age)
        // DM use CONCAT or ||. Mybatis might handle this, but generic CONCAT usually works if provided by DB.
        // If DM needs specific func, we might see error.
        Function nameDesc = Function.builder(table).setFunName("CONCAT")
                .addFieldArg(User.Fields.name)
                .addValueArg(" - ")
                .addFieldArg(User.Fields.userAge)
                .build();

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addFormula(agePlusOne, "nextYearAge")
                .addFunc(nameDesc, "description")
                .done()
                .limit(2)
                .build();

        List<StringHashMap> maps = sqlSession.getMapper(EzMapper.class).query(query);
        log.info("EzQuery Functions & Formulas: {}", JacksonUtils.toJsonString(maps));
        sqlSession.close();
    }

    @Test
    public void ezQueryCaseWhen() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        EntityTable table = EntityTable.of(User.class);

        CaseWhen ageGroup = CaseWhen.builder(table)
                .when().addFieldCondition(User.Fields.userAge, Operator.lt, 18).then("Young")
                .when().addFieldCondition(User.Fields.userAge, Operator.ge, 18).then("Adult")
                .els("Unknown");

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.name)
                .addCaseWhen(ageGroup, "ageGroup")
                .done()
                .limit(5)
                .build();

        List<StringHashMap> maps = sqlSession.getMapper(EzMapper.class).query(query);
        log.info("EzQuery CaseWhen: {}", JacksonUtils.toJsonString(maps));
        sqlSession.close();
    }

    @Test
    public void ezQueryCount() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().build();
        int count = sqlSession.getMapper(EzMapper.class).queryCount(query);
        log.info("EzQuery Count: {}", count);
        sqlSession.close();
    }

    @Test
    public void ezQueryOne() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        this.ensureData(sqlSession);
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().limit(1).build();
        User user = sqlSession.getMapper(EzMapper.class).queryOne(query);
        log.info("EzQuery One: {}", JacksonUtils.toJsonString(user));
        sqlSession.close();
    }
}
