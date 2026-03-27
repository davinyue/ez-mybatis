package org.rdlinux.mssql;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.WindowFunction;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.entity.UserOrg;
import org.rdlinux.ezmybatis.demo.mapper.UserMapper;
import org.rdlinux.ezmybatis.enumeration.AndOr;
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
public class MssqlSelectTest extends MssqlBaseTest {

    // Helper to get a valid ID for testing

    // Helper to get valid IDs

    // Ensure data exists


    // =================================================================================================================
    // UserMapper Tests (EzBaseMapper Methods)
    // =================================================================================================================

    @Test
    public void userSelectById() {
        String id = this.getOneUserId();
        User user = this.sqlSession.getMapper(UserMapper.class).selectById(id);
        Assert.assertNotNull(user);
        log.info("UserMapper.selectById: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void userSelectByTableAndId() {
        String id = this.getOneUserId();
        User user = this.sqlSession.getMapper(UserMapper.class).selectByTableAndId(EntityTable.of(User.class), id);
        Assert.assertNotNull(user);
        log.info("UserMapper.selectByTableAndId: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void userSelectByIds() {
        List<String> ids = this.getUserIds(2);
        List<User> users = this.sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        Assert.assertNotNull(users);
        log.info("UserMapper.selectByIds: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void userSelectByTableAndIds() {
        List<String> ids = this.getUserIds(2);
        List<User> users = this.sqlSession.getMapper(UserMapper.class).selectByTableAndIds(EntityTable.of(User.class), ids);
        Assert.assertNotNull(users);
        log.info("UserMapper.selectByTableAndIds: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void userSelectOneBySql() {
        String id = this.getOneUserId();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        User user = this.sqlSession.getMapper(UserMapper.class).selectOneBySql("SELECT * FROM ez_user WHERE id = #{id}", params);
        Assert.assertNotNull(user);
        log.info("UserMapper.selectOneBySql: {}", JacksonUtils.toJsonString(user));
    }

    // =================================================================================================================
    // EzMapper Tests
    // =================================================================================================================

    @Test
    public void ezSelectById() {
        String id = this.getOneUserId();
        User user = this.sqlSession.getMapper(EzMapper.class).selectById(User.class, id);
        Assert.assertNotNull(user);
        log.info("EzMapper.selectById: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezSelectByTableAndId() {
        String id = this.getOneUserId();
        User user = this.sqlSession.getMapper(EzMapper.class).selectByTableAndId(EntityTable.of(User.class), User.class, id);
        Assert.assertNotNull(user);
        log.info("EzMapper.selectByTableAndId: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezSelectByIds() {
        List<String> ids = this.getUserIds(2);
        List<User> users = this.sqlSession.getMapper(EzMapper.class).selectByIds(User.class, ids);
        Assert.assertNotNull(users);
        log.info("EzMapper.selectByIds: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezSelectByTableAndIds() {
        List<String> ids = this.getUserIds(2);
        List<User> users = this.sqlSession.getMapper(EzMapper.class).selectByTableAndIds(EntityTable.of(User.class),
                User.class, ids);
        Assert.assertNotNull(users);
        log.info("EzMapper.selectByTableAndIds: {}", JacksonUtils.toJsonString(users));
    }

    // =================================================================================================================
    // EzQuery Comprehensive Tests
    // =================================================================================================================

    /**
     * 通用mapper结果类型并发测试
     */
    @Test
    public void ezQueryConcurrentSelectRetTypeTest() throws InterruptedException, java.util.concurrent.ExecutionException {
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
                try (SqlSession threadSession = MssqlBaseTest.sqlSessionFactory.openSession()) {
                    startLatch.await();
                    EzMapper mapper = threadSession.getMapper(EzMapper.class);
                    for (int j = 0; j < loopCount; j++) {
                        if (index % 2 == 0) {
                            List<?> ret = mapper.query(userQuery);
                            Assert.assertNotNull(ret);
                            if (!ret.isEmpty()) {
                                Assert.assertNotNull(ret.get(0));
                                Assert.assertTrue(ret.get(0) instanceof User);
                            }
                        } else {
                            List<?> ret = mapper.query(uoQuery);
                            Assert.assertNotNull(ret);
                            if (!ret.isEmpty()) {
                                Assert.assertNotNull(ret.get(0));
                                Assert.assertTrue(ret.get(0) instanceof UserOrg);
                            }
                        }
                    }
                    return "OK";
                } catch (Exception e) {
                    log.error("Concurrent test failed", e);
                    return e.getMessage();
                }
            }));
        }

        startLatch.countDown();
        for (Future<String> future : futures) {
            String result = future.get();
            Assert.assertEquals("OK", result);
        }
        executorService.shutdown();
    }

    @Test
    public void ezQueryBasicSelect() {
        // Select all fields
        EzQuery<User> queryAll = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .page(1, 2)
                .build();
        List<User> usersAll = this.sqlSession.getMapper(EzMapper.class).query(queryAll);
        Assert.assertNotNull(usersAll);
        log.info("EzQuery Basic Select All: {}", JacksonUtils.toJsonString(usersAll));

        // Select specific fields
        EzQuery<User> querySpecific = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addField(BaseEntity.Fields.id).addField(User.Fields.name).done()
                .page(1, 2)
                .build();
        List<User> usersSpecific = this.sqlSession.getMapper(EzMapper.class).query(querySpecific);
        Assert.assertNotNull(usersSpecific);
        log.info("EzQuery Basic Select Specific: {}", JacksonUtils.toJsonString(usersSpecific));
    }

    @Test
    public void ezQueryConditions() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        EntityTable userTable = EntityTable.of(User.class);

        // 1. EQ (=)
        EzQuery<User> eqQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.sex, Operator.eq, User.Sex.MAN).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(eqQuery));
        log.info("EzQuery EQ: {}", JacksonUtils.toJsonString(mapper.query(eqQuery)));

        // 2. NE (!=)
        EzQuery<User> neQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.ne, -1).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(neQuery));
        log.info("EzQuery NE: {}", JacksonUtils.toJsonString(mapper.query(neQuery)));

        // 3. GT (>)
        EzQuery<User> gtQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.gt, 10).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(gtQuery));
        log.info("EzQuery GT: {}", JacksonUtils.toJsonString(mapper.query(gtQuery)));

        // 4. GE (>=)
        EzQuery<User> geQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.ge, 18).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(geQuery));
        log.info("EzQuery GE: {}", JacksonUtils.toJsonString(mapper.query(geQuery)));

        // 5. LT (<)
        EzQuery<User> ltQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.lt, 100).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(ltQuery));
        log.info("EzQuery LT: {}", JacksonUtils.toJsonString(mapper.query(ltQuery)));

        // 6. LE (<=)
        EzQuery<User> leQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.le, 18).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(leQuery));
        log.info("EzQuery LE: {}", JacksonUtils.toJsonString(mapper.query(leQuery)));

        // 7. IS NULL
        EzQuery<User> isNullQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(BaseEntity.Fields.id).isNull()).done() // Use dedicated method
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(isNullQuery));
        log.info("EzQuery IS NULL: {}", JacksonUtils.toJsonString(mapper.query(isNullQuery)));

        // 8. IS NOT NULL
        EzQuery<User> isNotNullQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(BaseEntity.Fields.id).isNotNull()).done() // Use dedicated method
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(isNotNullQuery));
        log.info("EzQuery IS NOT NULL: {}", JacksonUtils.toJsonString(mapper.query(isNotNullQuery)));

        // 9. IN
        EzQuery<User> inQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.in, Arrays.asList(18, 20, 27)).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(inQuery));
        log.info("EzQuery IN: {}", JacksonUtils.toJsonString(mapper.query(inQuery)));

        // 10. NOT IN
        EzQuery<User> notInQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.userAge, Operator.notIn, Arrays.asList(-1, -2)).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(notInQuery));
        log.info("EzQuery NOT IN: {}", JacksonUtils.toJsonString(mapper.query(notInQuery)));

        // 11. LIKE
        EzQuery<User> likeQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, Operator.like, "Test%").done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(likeQuery));
        log.info("EzQuery LIKE: {}", JacksonUtils.toJsonString(mapper.query(likeQuery)));

        // 12. NOT LIKE (unlike)
        EzQuery<User> unlikeQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, Operator.unlike, "NonExis%").done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(unlikeQuery));
        log.info("EzQuery NOT LIKE: {}", JacksonUtils.toJsonString(mapper.query(unlikeQuery)));

        // 13. BETWEEN
        EzQuery<User> betweenQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(User.Fields.userAge).between(10, 30)).done() // Use dedicated method
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(betweenQuery));
        log.info("EzQuery BETWEEN: {}", JacksonUtils.toJsonString(mapper.query(betweenQuery)));

        // 14. NOT BETWEEN
        EzQuery<User> notBetweenQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(User.Fields.userAge).notBetween(100, 200)).done() // Use dedicated method
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(notBetweenQuery));
        log.info("EzQuery NOT BETWEEN: {}", JacksonUtils.toJsonString(mapper.query(notBetweenQuery)));

        // 15. REGEXP
        EzQuery<User> regexpQuery = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, Operator.regexp, "^Test.*").done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(regexpQuery));
        log.info("EzQuery REGEXP: {}", JacksonUtils.toJsonString(mapper.query(regexpQuery)));
    }

    @Test
    public void ezQueryJoin() {
        EntityTable userTable = EntityTable.of(User.class);
        EntityTable userOrgTable = EntityTable.of(UserOrg.class);

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
                .from(userTable)
                .select().addAll().done()
                .join(userOrgTable)
                .addCondition(userTable.field(BaseEntity.Fields.id).eq(userOrgTable.field(UserOrg.Fields.userId)))
                .done()
                .page(1, 10)
                .build();

        List<StringHashMap> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        log.info("EzQuery Join: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryNestedCondition() {
        EntityTable userTable = EntityTable.of(User.class);
        // Test ( userAge < 20 OR name = 'TestUser3' ) AND sex = MAN
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .where()
                .groupCondition()
                .addFieldCondition(User.Fields.userAge, Operator.lt, 20)
                .addFieldCondition(AndOr.OR, User.Fields.name, Operator.eq, "TestUser3")
                .done()
                .addFieldCondition(User.Fields.sex, Operator.eq, User.Sex.MAN)
                .done()
                .build();

        List<User> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        Assert.assertEquals("Expected exactly TestUser1 and TestUser3", 2, result.size());
        log.info("EzQuery NestedCondition: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryGroupByHaving() {
        EntityTable table = EntityTable.of(User.class);

        // Count users by age
        Function countFn = Function.builder("COUNT").addArg(EntityField.of(table, BaseEntity.Fields.id)).build();
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.userAge)
                .add(countFn, "count")
                .done()
                .groupBy().addField(User.Fields.userAge).done()
                .having().addCondition(countFn.ge(0)).done()
                .build();

        List<StringHashMap> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        log.info("EzQuery GroupBy Having: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryOrderBy() {

        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .orderBy()
                .addField(User.Fields.userAge, OrderType.ASC)
                .addField(BaseEntity.Fields.createTime, OrderType.DESC)
                .done()
                .page(1, 5)
                .build();
        List<User> users = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(users);
        log.info("EzQuery OrderBy: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQueryUnion() {

        EzQuery<User> q1 = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.sex, User.Sex.MAN).done()
                .build();

        EzQuery<User> q2 = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.sex, User.Sex.WOMAN).done()
                .build();

        EzQuery<User> q3 = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.sex, User.Sex.WOMAN).done()
                .unionAll(q2)
                .build();

        EzQuery<User> unionQuery = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .where().addFieldCondition(User.Fields.name, "NonExistent").done() // Main query empty
                .union(q1)
                .unionAll(q3)
                .page(1, 10)
                .orderBy()
                .addField(User.Fields.name)
                .done()
                .build();

        List<User> users = this.sqlSession.getMapper(EzMapper.class).query(unionQuery);
        Assert.assertNotNull(users);
        log.info("EzQuery Union: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQuerySubQuery() {

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

        List<User> users = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(users);
        log.info("EzQuery SubQuery: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQueryFunctionsAndFormulas() {
        EntityTable table = EntityTable.of(User.class);

        // Formula: age + 1
        Formula agePlusOne = Formula.builder(table.field(User.Fields.userAge)).add(1).done().build();

        // Function: CONCAT(name, ' - ', age)
        Function nameDesc = Function.builder("CONCAT")
                .addArg(EntityField.of(table, User.Fields.name))
                .addArg(" - ")
                .addArg(EntityField.of(table, User.Fields.userAge))
                .build();

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .add(agePlusOne, "nextYearAge")
                .add(nameDesc, "description")
                .done()
                .limit(2)
                .build();

        List<StringHashMap> maps = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(maps);
        Assert.assertFalse(maps.isEmpty());
        // Verify Formula and Function results for first user
        StringHashMap firstMap = maps.get(0);
        Assert.assertNotNull(firstMap.get("nextYearAge"));
        Assert.assertNotNull(firstMap.get("description"));
        // Assuming first user is TestUser1 (age 18)
        if (String.valueOf(firstMap.get("description")).contains("TestUser1")) {
            Assert.assertEquals(19, Integer.parseInt(firstMap.get("nextYearAge").toString()));
            Assert.assertEquals("TestUser1 - 18", firstMap.get("description").toString());
        }
        log.info("EzQuery Functions & Formulas: {}", JacksonUtils.toJsonString(maps));
    }

    @Test
    public void ezQueryCaseWhen() {
        EntityTable table = EntityTable.of(User.class);

        CaseWhen ageGroup = CaseWhen.builder(table)
                .when().addFieldCondition(table, User.Fields.userAge, Operator.lt, 19).then("Young")
                .when().addFieldCondition(table, User.Fields.userAge, Operator.ge, 19).then("Adult")
                .els("Unknown");

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.name)
                .addField(User.Fields.userAge)
                .add(ageGroup, "ageGroup")
                .done()
                .orderBy().addField(User.Fields.userAge, OrderType.ASC).done()
                .limit(5)
                .build();

        List<StringHashMap> maps = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(maps);
        Assert.assertEquals(3, maps.size());
        // TestUser1 (18) should be Young
        Assert.assertEquals("Young", maps.get(0).get("ageGroup"));
        // TestUser2 (20) should be Adult
        Assert.assertEquals("Adult", maps.get(1).get("ageGroup"));
        // TestUser3 (30) should be Adult
        Assert.assertEquals("Adult", maps.get(2).get("ageGroup"));
        log.info("EzQuery CaseWhen: {}", JacksonUtils.toJsonString(maps));
    }

    @Test
    public void ezQueryCount() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().build();
        int count = this.sqlSession.getMapper(EzMapper.class).queryCount(query);
        Assert.assertTrue(count >= 0);
        log.info("EzQuery Count: {}", count);
    }

    @Test
    public void ezQueryOne() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().limit(1).build();
        User user = this.sqlSession.getMapper(EzMapper.class).queryOne(query);
        Assert.assertNotNull(user);
        log.info("EzQuery One: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezQueryWindowFunction() {
        EntityTable table = EntityTable.of(User.class);

        // 1. 无 partition, 无 order, 无 frame
        Function countFunc1 = Function.builder("COUNT").addArg(EntityField.of(table, BaseEntity.Fields.id)).build();
        WindowFunction wf1 = WindowFunction.builder(countFunc1).build();

        // 2. 多个 partition, 多个 order
        Function rowNumFunc = Function.builder("ROW_NUMBER").build();
        WindowFunction wf2 = WindowFunction.builder(rowNumFunc)
                .partitionBy(EntityField.of(table, User.Fields.sex))
                .partitionBy(EntityField.of(table, User.Fields.userAge))
                .orderBy(EntityField.of(table, User.Fields.name), OrderType.ASC)
                .orderBy(EntityField.of(table, BaseEntity.Fields.createTime), OrderType.DESC)
                .build();

        // 3. ROWS 各种场景
        Function sumAgeFunc = Function.builder("SUM").addArg(EntityField.of(table, User.Fields.userAge)).build();

        // UNBOUNDED PRECEDING to CURRENT ROW
        WindowFunction wf3 = WindowFunction.builder(sumAgeFunc)
                .partitionBy(EntityField.of(table, User.Fields.sex))
                .orderBy(EntityField.of(table, User.Fields.userAge), OrderType.ASC)
                .rowsBetween(WindowFunction.WindowFrameBound.unboundedPreceding(), WindowFunction.WindowFrameBound.currentRow())
                .build();

        // 2 PRECEDING to 2 FOLLOWING
        WindowFunction wf4 = WindowFunction.builder(sumAgeFunc)
                .partitionBy(EntityField.of(table, User.Fields.sex))
                .orderBy(EntityField.of(table, User.Fields.userAge), OrderType.ASC)
                .rowsBetween(WindowFunction.WindowFrameBound.preceding(2), WindowFunction.WindowFrameBound.following(2))
                .build();

        // CURRENT ROW to UNBOUNDED FOLLOWING
        WindowFunction wf5 = WindowFunction.builder(sumAgeFunc)
                .partitionBy(EntityField.of(table, User.Fields.sex))
                .orderBy(EntityField.of(table, User.Fields.userAge), OrderType.ASC)
                .rowsBetween(WindowFunction.WindowFrameBound.currentRow(), WindowFunction.WindowFrameBound.unboundedFollowing())
                .build();

        // UNBOUNDED PRECEDING to UNBOUNDED FOLLOWING
        WindowFunction wf6 = WindowFunction.builder(sumAgeFunc)
                .partitionBy(EntityField.of(table, User.Fields.sex))
                .orderBy(EntityField.of(table, User.Fields.userAge), OrderType.ASC)
                .rowsBetween(WindowFunction.WindowFrameBound.unboundedPreceding(), WindowFunction.WindowFrameBound.unboundedFollowing())
                .build();

        // 4. RANGE 场景
        WindowFunction wf7 = WindowFunction.builder(sumAgeFunc)
                .partitionBy(EntityField.of(table, User.Fields.sex))
                .orderBy(EntityField.of(table, User.Fields.userAge), OrderType.ASC)
                .rangeBetween(WindowFunction.WindowFrameBound.unboundedPreceding(), WindowFunction.WindowFrameBound.currentRow())
                .build();

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select()
                .addField(User.Fields.name)
                .addField(User.Fields.sex)
                .addField(User.Fields.userAge)
                .add(wf1, "totalCount")
                .add(wf2, "rn")
                .add(wf3, "sumAge_unbounded_current")
                .add(wf4, "sumAge_2_preceding_following")
                .add(wf5, "sumAge_current_unbounded")
                .add(wf6, "sumAge_unbounded_unbounded")
                .add(wf7, "sumAge_range")
                .done()
                .limit(10)
                .build();

        List<StringHashMap> maps = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(maps);
        Assert.assertEquals(3, maps.size());
        // Sort behavior varies, but we can check specific entries
        for (StringHashMap map : maps) {
            String name = (String) map.get(User.Fields.name);
            Integer rn = Integer.valueOf(map.get("rn").toString());
            if ("TestUser1".equals(name)) {
                Assert.assertEquals(Integer.valueOf(1), rn);
            } else if ("TestUser2".equals(name)) {
                Assert.assertEquals(Integer.valueOf(1), rn); // Only woman
            } else if ("TestUser3".equals(name)) {
                Assert.assertEquals(Integer.valueOf(1), rn); // Diff age partition
            }
        }
        log.info("EzQuery WindowFunction: {}", JacksonUtils.toJsonString(maps));
    }
}
