package org.rdlinux.mysql;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.*;
import org.rdlinux.ezmybatis.demo.mapper.ComplexUserMapper;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;
import org.rdlinux.ezmybatis.enumeration.OrderType;
import org.rdlinux.ezmybatis.utils.StringHashMap;
import org.rdlinux.luava.json.JacksonUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class MySqlComplexEntitySelectTest extends MySqlBaseTest {

    private static final Faker faker = new Faker(java.util.Locale.CHINA);

    // =================================================================================================================
    // 测试数据建造与填充
    // =================================================================================================================

    private ComplexDepartment insertAndGetDepartment() {
        ComplexDepartment dept = new ComplexDepartment();
        dept.setName(faker.company().name());
        dept.setDescription("测试部门");
        this.sqlSession.getMapper(EzMapper.class).insert(dept);
        this.sqlSession.commit();
        return dept;
    }

    private ComplexUser buildComplexUser(String deptId) {
        ComplexUser user = new ComplexUser();
        // 给特定字段硬编码以便断言
        user.setUsername("被查用户_" + faker.name().fullName());
        user.setAge(new Random().nextInt(50) + 18); // 18-67
        user.setUserType((short) 1);
        user.setScore(faker.number().randomNumber());
        user.setAccountBalance(faker.number().randomDouble(2, 100, 10000));
        user.setSalary(new BigDecimal(faker.number().randomDouble(2, 3000, 50000))
                .setScale(2, RoundingMode.DOWN));
        user.setIsActive(true);

        ComplexUser.UserStatus[] statuses = ComplexUser.UserStatus.values();
        user.setStatus(statuses[new Random().nextInt(statuses.length)]);
        user.setBirthday(faker.date().birthday());
        user.setAvatar("binary_avatar_data".getBytes(StandardCharsets.UTF_8));
        user.setDescription(faker.lorem().paragraph());
        user.setIgnoredData("This should be transient");
        user.setSpecificColumn("SpecificCol_Val");
        user.setSecretContent(faker.internet().password());

        ExtInfo extInfo = new ExtInfo();
        extInfo.setHobby(faker.esports().game());
        extInfo.setRemark("For Select Test");
        user.setExtInfo(extInfo);

        user.setDepartmentId(deptId);
        return user;
    }

    private String insertAndGetComplexUserId(String deptId) {
        ComplexUser user = this.buildComplexUser(deptId);
        this.sqlSession.getMapper(ComplexUserMapper.class).insert(user);
        this.sqlSession.commit();
        return user.getId();
    }

    private List<String> insertAndGetComplexUserIds(int count, String deptId) {
        List<ComplexUser> users = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            users.add(this.buildComplexUser(deptId));
        }
        this.sqlSession.getMapper(ComplexUserMapper.class).batchInsert(users);
        this.sqlSession.commit();

        List<String> ids = new LinkedList<>();
        for (ComplexUser user : users) {
            ids.add(user.getId());
        }
        return ids;
    }

    private void insertOrder(String userId, ComplexOrder.OrderStatus status) {
        ComplexOrder order = new ComplexOrder();
        order.setUserId(userId);
        order.setOrderNo(faker.code().asin());
        order.setTotalAmount(new BigDecimal(faker.number().randomDouble(2, 10, 5000)));
        order.setStatus(status);
        this.sqlSession.getMapper(EzMapper.class).insert(order);
        this.sqlSession.commit();
    }

    // =================================================================================================================
    // ComplexUserMapper Tests (EzBaseMapper Methods)
    // =================================================================================================================

    @Test
    public void ezBaseMapperSelectById() {
        String deptId = this.insertAndGetDepartment().getId();
        String id = this.insertAndGetComplexUserId(deptId);
        ComplexUser user = this.sqlSession.getMapper(ComplexUserMapper.class).selectById(id);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getExtInfo()); // 验证JSON转换
        log.info("ComplexUserMapper.selectById: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezBaseMapperSelectByIds() {
        String deptId = this.insertAndGetDepartment().getId();
        List<String> ids = this.insertAndGetComplexUserIds(2, deptId);
        List<ComplexUser> users = this.sqlSession.getMapper(ComplexUserMapper.class).selectByIds(ids);
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
        log.info("ComplexUserMapper.selectByIds: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezBaseMapperSelectOneBySql() {
        String deptId = this.insertAndGetDepartment().getId();
        String id = this.insertAndGetComplexUserId(deptId);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        ComplexUser user = this.sqlSession.getMapper(ComplexUserMapper.class).selectOneBySql("SELECT * FROM ez_complex_user WHERE id = #{id}", params);
        Assert.assertNotNull(user);
        log.info("ComplexUserMapper.selectOneBySql: {}", JacksonUtils.toJsonString(user));
    }

    // =================================================================================================================
    // EzMapper Tests
    // =================================================================================================================

    @Test
    public void ezSelectByTableAndId() {
        String deptId = this.insertAndGetDepartment().getId();
        String id = this.insertAndGetComplexUserId(deptId);
        ComplexUser user = this.sqlSession.getMapper(EzMapper.class).selectByTableAndId(EntityTable.of(ComplexUser.class), ComplexUser.class, id);
        Assert.assertNotNull(user);
        log.info("EzMapper.selectByTableAndId: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezSelectMapBySql() {
        this.insertAndGetDepartment();
        List<Map<String, Object>> maps = this.sqlSession.getMapper(EzMapper.class).selectMapBySql("SELECT * FROM ez_complex_department LIMIT 2", new HashMap<>());
        Assert.assertNotNull(maps);
        Assert.assertEquals(2, maps.size());
        log.info("EzMapper.selectMapBySql: {}", JacksonUtils.toJsonString(maps));
    }

    // =================================================================================================================
    // EzQuery Comprehensive Tests
    // =================================================================================================================

    @Test
    public void ezQueryBasicSelect() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EntityTable userTable = EntityTable.of(ComplexUser.class);

        // Select all fields
        EzQuery<ComplexUser> queryAll = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .page(1, 2)
                .build();
        List<ComplexUser> usersAll = this.sqlSession.getMapper(EzMapper.class).query(queryAll);
        Assert.assertNotNull(usersAll);
        log.info("EzQuery Basic Select All: {}", JacksonUtils.toJsonString(usersAll));

        // Select specific fields
        EzQuery<ComplexUser> querySpecific = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(s -> s.add(userTable.field(BaseEntity.Fields.id)))
                .select(s -> s.add(userTable.field(ComplexUser.Fields.username)))
                .page(1, 2)
                .build();
        List<ComplexUser> usersSpecific = this.sqlSession.getMapper(EzMapper.class).query(querySpecific);
        Assert.assertNotNull(usersSpecific);
        log.info("EzQuery Basic Select Specific: {}", JacksonUtils.toJsonString(usersSpecific));
    }

    @Test
    public void ezQueryLambdaDslTest() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EntityTable userTable = EntityTable.of(ComplexUser.class);
        boolean includeAge = true;
        boolean hasNameFilter = true;

        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class)
                .from(userTable)
                .select(s -> {
                    s.add(userTable.field(BaseEntity.Fields.id));
                    s.add(userTable.field(ComplexUser.Fields.username));
                    s.add(includeAge, userTable.field(ComplexUser.Fields.age));
                })
                .where(w -> w.add(hasNameFilter, userTable.field(ComplexUser.Fields.username).like("被查用户_%")))
                .orderBy(o -> o.add(userTable.field(ComplexUser.Fields.age), OrderType.ASC))
                .limit(2)
                .build();

        List<ComplexUser> users = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(users);
        Assert.assertFalse(users.isEmpty());
        log.info("EzQuery LambdaDsl: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQueryConditions() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        EntityTable userTable = EntityTable.of(ComplexUser.class);

        ComplexDepartment dept = this.insertAndGetDepartment();
        ComplexUser user1 = this.buildComplexUser(dept.getId());
        user1.setAge(20);
        user1.setAccountBalance(150.50);
        user1.setStatus(ComplexUser.UserStatus.NORMAL);
        this.sqlSession.getMapper(ComplexUserMapper.class).insert(user1);

        ComplexUser user2 = this.buildComplexUser(dept.getId());
        user2.setAge(30);
        user2.setAccountBalance(500.00);
        user2.setStatus(ComplexUser.UserStatus.DISABLED);
        this.sqlSession.getMapper(ComplexUserMapper.class).insert(user2);

        this.insertOrder(user1.getId(), ComplexOrder.OrderStatus.PAID);
        this.sqlSession.commit();

        // 1. EQ (= 枚举匹配)
        EzQuery<ComplexUser> eqQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.status).eq(ComplexUser.UserStatus.NORMAL)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(eqQuery));

        // 2. NE (!=)
        EzQuery<ComplexUser> neQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.age).ne(-1)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(neQuery));

        // 3. GT (>)
        EzQuery<ComplexUser> gtQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.accountBalance).gt(200.0)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(gtQuery));

        // 4. GE (>=)
        EzQuery<ComplexUser> geQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.age).ge(18)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(geQuery));

        // 5. LT (<)
        EzQuery<ComplexUser> ltQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.age).lt(100)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(ltQuery));

        // 6. LE (<=)
        EzQuery<ComplexUser> leQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.age).le(20)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(leQuery));

        // 7. IS NULL
        EzQuery<ComplexUser> isNullQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.description).isNull()))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(isNullQuery));

        // 8. IS NOT NULL
        EzQuery<ComplexUser> isNotNullQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.departmentId).isNotNull()))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(isNotNullQuery));

        // 9. IN
        EzQuery<ComplexUser> inQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> {
                    w.add(userTable.field(ComplexUser.Fields.departmentId).eq(dept.getId()));
                    w.add(userTable.field(ComplexUser.Fields.age).in(Arrays.asList(20, 30)));
                })
                .page(1, 2).build();
        List<ComplexUser> inUsers = mapper.query(inQuery);
        Assert.assertNotNull(inUsers);
        Assert.assertEquals(2, inUsers.size());

        int[] ages = {20, 30};
        EzQuery<ComplexUser> primitiveArrayInQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> {
                    w.add(userTable.field(ComplexUser.Fields.departmentId).eq(dept.getId()));
                    w.add(userTable.field(ComplexUser.Fields.age).in(ages));
                })
                .page(1, 2).build();
        List<ComplexUser> primitiveArrayInUsers = mapper.query(primitiveArrayInQuery);
        Assert.assertNotNull(primitiveArrayInUsers);
        Assert.assertEquals(2, primitiveArrayInUsers.size());

        // 10. 延迟构造条件
        String fieldName = null;
        EzQuery<ComplexUser> lazyConditionQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> {
                    w.add(userTable.field(ComplexUser.Fields.departmentId).eq(dept.getId()));
                    w.add(fieldName != null, ww -> ww.add(userTable.field(fieldName).eq(1)));
                })
                .page(1, 2).build();
        List<ComplexUser> lazyConditionUsers = mapper.query(lazyConditionQuery);
        Assert.assertNotNull(lazyConditionUsers);
        Assert.assertEquals(2, lazyConditionUsers.size());

        // 11. NOT IN
        EzQuery<ComplexUser> notInQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.age).notIn(Arrays.asList(-1, -2))))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(notInQuery));

        // 12. LIKE
        EzQuery<ComplexUser> likeQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.username).like("被查用户_%")))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(likeQuery));

        // 13. NOT LIKE (unlike)
        EzQuery<ComplexUser> unlikeQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.username).unlike("NonExis%")))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(unlikeQuery));

        // 14. BETWEEN
        EzQuery<ComplexUser> betweenQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.age).between(18, 25)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(betweenQuery));

        // 15. NOT BETWEEN
        EzQuery<ComplexUser> notBetweenQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.age).notBetween(100, 200)))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(notBetweenQuery));

        // 16. REGEXP
        EzQuery<ComplexUser> regexpQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(ComplexUser.Fields.username).regexp("^被查.*")))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(regexpQuery));

        // 17. EXISTS (关联查询)
        EntityTable orderTableCondition = EntityTable.of(ComplexOrder.class);
        EzQuery<ComplexOrder> orderSubQuery = EzQuery.builder(ComplexOrder.class)
                .from(orderTableCondition).select(s -> s.add(orderTableCondition.field(BaseEntity.Fields.id)))
                .where(e ->
                        e.add(orderTableCondition.field(ComplexOrder.Fields.userId)
                                .eq(userTable.field(BaseEntity.Fields.id))))
                .build();

        EzQuery<ComplexUser> existsQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(orderSubQuery.exists()).add(orderSubQuery.orExists()))
                .page(1, 10).build();
        List<ComplexUser> existsRet = mapper.query(existsQuery);
        Assert.assertNotNull(existsRet);
        log.info("EzQuery EXISTS: {}", JacksonUtils.toJsonString(existsRet));

        // 18. NOT EXISTS
        EzQuery<ComplexUser> notExistsQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(orderSubQuery.notExists()).add(orderSubQuery.orNotExists()))
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(notExistsQuery));
        log.info("EzQuery NOT EXISTS: {}", JacksonUtils.toJsonString(mapper.query(notExistsQuery)));
    }

    @Test
    public void ezQueryJoin() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EntityTable userTable = EntityTable.of(ComplexUser.class);
        EntityTable deptTable = EntityTable.of(ComplexDepartment.class);

        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class)
                .from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .join(deptTable, j ->
                        j.add(userTable.field(ComplexUser.Fields.departmentId).eq(deptTable.field(BaseEntity.Fields.id))))
                .page(1, 10)
                .build();

        List<ComplexUser> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 2);
        log.info("EzQuery Join: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryNestedJoinLambdaDslTest() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        String uId = this.insertAndGetComplexUserId(dept.getId());
        this.insertOrder(uId, ComplexOrder.OrderStatus.PENDING);

        EntityTable deptTable = EntityTable.of(ComplexDepartment.class);
        EntityTable userTable = EntityTable.of(ComplexUser.class);
        EntityTable orderTable = EntityTable.of(ComplexOrder.class);

        EzQuery<ComplexDepartment> query = EzQuery.builder(ComplexDepartment.class)
                .from(deptTable)
                .select(Select.EzSelectBuilder::addAll)
                .join(userTable, j -> {
                    j.add(deptTable.field(BaseEntity.Fields.id).eq(userTable.field(ComplexUser.Fields.departmentId)));
                    // 用户下继续关联订单，体现真正的嵌套 join：部门 -> 用户 -> 订单
                    j.join(orderTable, jj -> jj.add(
                            userTable.field(BaseEntity.Fields.id).eq(orderTable.field(ComplexOrder.Fields.userId))));
                })
                .limit(10)
                .build();

        List<ComplexDepartment> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        log.info("EzQuery NestedJoinLambdaDsl: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryGroupByHaving() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(3, dept.getId());

        EntityTable table = EntityTable.of(ComplexUser.class);
        String groupField = null;

        // Count users by deptId
        Function countFn = Function.build("COUNT", f ->
                f.addArg(EntityField.of(table, BaseEntity.Fields.id)));

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select(s -> {
                    s.add(table.field(ComplexUser.Fields.departmentId));
                    s.add(countFn, "count");
                })
                //测试多个groupBy是否会丢弃group项, 正常是不丢弃
                .groupBy(g -> g.add(table.field(ComplexUser.Fields.departmentId)))
                .groupBy(g -> g.add(groupField != null, gg -> gg.add(table.field(groupField))))
                .groupBy(g -> g.add(table.field(ComplexUser.Fields.userType)))
                .having(h -> h.add(countFn.ge(0)))
                .build();

        List<StringHashMap> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        log.info("EzQuery GroupBy Having: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryFunctionsAndFormulas() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EntityTable table = EntityTable.of(ComplexUser.class);
        String functionField = null;
        String formulaField = null;

        // Formula: age + 1 (使用刚刚被强化改造且强限制为 .with() 起手的 Formula)
        Formula agePlusOne = Formula.build(f ->
                f.with(table.field(ComplexUser.Fields.age))
                        .add(formulaField != null, ff -> ff.add(table.field(formulaField)))
                        .add(1));

        // Function: CONCAT(username, ' - ', age)
        Function nameDesc = Function.build("CONCAT", f -> f
                .addArg(EntityField.of(table, ComplexUser.Fields.username))
                .addArg(functionField != null, ff -> ff.addArg(table.field(functionField)))
                .addArg(" - ")
                .addArg(EntityField.of(table, ComplexUser.Fields.age))
        );

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select(s -> {
                    s.add(agePlusOne, "nextYearAge");
                    s.add(nameDesc, "description");
                })
                .limit(2)
                .build();

        List<StringHashMap> maps = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(maps);
        Assert.assertFalse(maps.isEmpty());

        StringHashMap firstMap = maps.get(0);
        Assert.assertNotNull(firstMap.get("nextYearAge"));
        Assert.assertNotNull(firstMap.get("description"));
        log.info("EzQuery Functions & Formulas: {}", JacksonUtils.toJsonString(maps));
    }

    @Test
    public void ezQueryCaseWhen() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(3, dept.getId());

        EntityTable table = EntityTable.of(ComplexUser.class);

        CaseWhen ageGroup = CaseWhen.build(c -> c
                .when(w -> w.add(table.field(ComplexUser.Fields.age).lt(19)), "Young")
                .when(w -> w.add(table.field(ComplexUser.Fields.age).ge(19)), "Adult")
                .els("Unknown")
        );

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select(s -> {
                    s.add(table.field(ComplexUser.Fields.username));
                    s.add(table.field(ComplexUser.Fields.age));
                    s.add(ageGroup, "ageGroup");
                })
                .orderBy(o -> o.add(table.field(ComplexUser.Fields.age), OrderType.ASC))
                .limit(5)
                .build();

        List<StringHashMap> maps = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(maps);
        Assert.assertFalse(maps.isEmpty());
        log.info("EzQuery CaseWhen: {}", JacksonUtils.toJsonString(maps));
    }

    @Test
    public void ezQueryWindowFunction() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(3, dept.getId());

        EntityTable table = EntityTable.of(ComplexUser.class);

        // 1. 无 partition, 无 order, 无 frame
        Function countFunc1 = Function.build("COUNT", f -> f.addArg(EntityField.of(table, BaseEntity.Fields.id)));
        WindowFunction wf1 = WindowFunction.build(countFunc1);

        // 2. 多个 partition, 多个 order
        Function rowNumFunc = Function.build("ROW_NUMBER", f -> {
        });
        WindowFunction wf2 = WindowFunction.build(rowNumFunc, w -> w
                .partitionBy(EntityField.of(table, ComplexUser.Fields.status))
                .orderBy(EntityField.of(table, ComplexUser.Fields.username), OrderType.ASC)
                .orderBy(EntityField.of(table, BaseEntity.Fields.createTime), OrderType.DESC));

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select(s -> {
                    s.add(table.field(ComplexUser.Fields.username));
                    s.add(table.field(ComplexUser.Fields.status));
                    s.add(wf1, "totalCount");
                    s.add(wf2, "rn");
                })
                .limit(10)
                .build();

        List<StringHashMap> maps = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(maps);
        Assert.assertFalse(maps.isEmpty());
        log.info("EzQuery WindowFunction: {}", JacksonUtils.toJsonString(maps));
    }

    // =================================================================================================================
    // Supplemented Missing Tests (BaseMapper, EzMapper, Advanced EzQuery)
    // =================================================================================================================

    @Test
    public void ezBaseMapperSelectByTableAndId() {
        String deptId = this.insertAndGetDepartment().getId();
        String id = this.insertAndGetComplexUserId(deptId);
        ComplexUser user = this.sqlSession.getMapper(ComplexUserMapper.class).selectByTableAndId(EntityTable.of(ComplexUser.class), id);
        Assert.assertNotNull(user);
        log.info("ComplexUserMapper.selectByTableAndId: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezBaseMapperSelectByTableAndIds() {
        String deptId = this.insertAndGetDepartment().getId();
        List<String> ids = this.insertAndGetComplexUserIds(2, deptId);
        List<ComplexUser> users = this.sqlSession.getMapper(ComplexUserMapper.class).selectByTableAndIds(EntityTable.of(ComplexUser.class), ids);
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
        log.info("ComplexUserMapper.selectByTableAndIds: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezBaseMapperSelectBySql() {
        String deptId = this.insertAndGetDepartment().getId();
        this.insertAndGetComplexUserIds(2, deptId);
        List<ComplexUser> users = this.sqlSession.getMapper(ComplexUserMapper.class).selectBySql("SELECT * FROM ez_complex_user LIMIT 2", new HashMap<>());
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
        log.info("ComplexUserMapper.selectBySql: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezSelectById() {
        String deptId = this.insertAndGetDepartment().getId();
        String id = this.insertAndGetComplexUserId(deptId);
        ComplexUser user = this.sqlSession.getMapper(EzMapper.class).selectById(ComplexUser.class, id);
        Assert.assertNotNull(user);
        log.info("EzMapper.selectById: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezSelectByIds() {
        String deptId = this.insertAndGetDepartment().getId();
        List<String> ids = this.insertAndGetComplexUserIds(2, deptId);
        List<ComplexUser> users = this.sqlSession.getMapper(EzMapper.class).selectByIds(ComplexUser.class, ids);
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
        log.info("EzMapper.selectByIds: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezSelectByTableAndIds() {
        String deptId = this.insertAndGetDepartment().getId();
        List<String> ids = this.insertAndGetComplexUserIds(2, deptId);
        List<ComplexUser> users = this.sqlSession.getMapper(EzMapper.class).selectByTableAndIds(EntityTable.of(ComplexUser.class), ComplexUser.class, ids);
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
        log.info("EzMapper.selectByTableAndIds: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezSelectOneMapBySql() {
        this.insertAndGetDepartment();
        Map<String, Object> map = this.sqlSession.getMapper(EzMapper.class).selectOneMapBySql("SELECT * FROM ez_complex_department LIMIT 1", new HashMap<>());
        Assert.assertNotNull(map);
        log.info("EzMapper.selectOneMapBySql: {}", JacksonUtils.toJsonString(map));
    }

    @Test
    public void ezSelectOneObjectBySql() {
        String deptId = this.insertAndGetDepartment().getId();
        this.insertAndGetComplexUserId(deptId);
        ComplexUser user = this.sqlSession.getMapper(EzMapper.class).selectOneObjectBySql(ComplexUser.class, "SELECT * FROM ez_complex_user LIMIT 1", new HashMap<>());
        Assert.assertNotNull(user);
        log.info("EzMapper.selectOneObjectBySql: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void ezSelectObjectBySql() {
        String deptId = this.insertAndGetDepartment().getId();
        this.insertAndGetComplexUserIds(2, deptId);
        List<ComplexUser> users = this.sqlSession.getMapper(EzMapper.class).selectObjectBySql(ComplexUser.class, "SELECT * FROM ez_complex_user LIMIT 2", new HashMap<>());
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
        log.info("EzMapper.selectObjectBySql: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQueryConcurrentSelectRetTypeTest() throws InterruptedException, java.util.concurrent.ExecutionException {
        EzQuery<ComplexUser> userQuery = EzQuery.builder(ComplexUser.class).from(EntityTable.of(ComplexUser.class))
                .select(Select.EzSelectBuilder::addAll).page(1, 1).build();
        EzQuery<ComplexDepartment> deptQuery = EzQuery.builder(ComplexDepartment.class).from(EntityTable.of(ComplexDepartment.class))
                .select(Select.EzSelectBuilder::addAll).page(1, 1).build();

        int threadCount = 10;
        int loopCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            futures.add(executorService.submit(() -> {
                try (SqlSession threadSession = MySqlBaseTest.sqlSessionFactory.openSession()) {
                    startLatch.await();
                    EzMapper mapper = threadSession.getMapper(EzMapper.class);
                    for (int j = 0; j < loopCount; j++) {
                        if (index % 2 == 0) {
                            List<?> ret = mapper.query(userQuery);
                            Assert.assertNotNull(ret);
                            if (!ret.isEmpty()) {
                                Assert.assertNotNull(ret.get(0));
                                Assert.assertTrue(ret.get(0) instanceof ComplexUser);
                            }
                        } else {
                            List<?> ret = mapper.query(deptQuery);
                            Assert.assertNotNull(ret);
                            if (!ret.isEmpty()) {
                                Assert.assertNotNull(ret.get(0));
                                Assert.assertTrue(ret.get(0) instanceof ComplexDepartment);
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
        for (java.util.concurrent.Future<String> future : futures) {
            String result = future.get();
            org.junit.Assert.assertEquals("OK", result);
        }
        executorService.shutdown();
    }

    @Test
    public void ezQueryLambdaGroupByHavingTest() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(3, dept.getId());

        EntityTable table = EntityTable.of(ComplexUser.class);
        Function countFn = Function.build("COUNT", f ->
                f.addArg(EntityField.of(table, BaseEntity.Fields.id)));

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
                .from(table)
                .select(s -> {
                    s.add(table.field(ComplexUser.Fields.departmentId));
                    s.add(countFn, "count");
                })
                .groupBy(g -> g.add(table.field(ComplexUser.Fields.departmentId)))
                .having(h -> h.add(countFn.ge(1)))
                //测试多个orderBy是否会丢弃排序项, 正常是不应该丢弃
                .orderBy(o -> o.add(table.field(ComplexUser.Fields.departmentId), OrderType.ASC))
                .orderBy(o -> o.add(countFn.desc()))
                .build();

        List<StringHashMap> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        log.info("EzQuery LambdaGroupByHaving: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryNestedCondition() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        ComplexUser u1 = this.buildComplexUser(dept.getId());
        u1.setUsername("TestUser1");
        u1.setAge(18);
        u1.setUserType((short) 1);
        this.sqlSession.getMapper(ComplexUserMapper.class).insert(u1);

        ComplexUser u2 = this.buildComplexUser(dept.getId());
        u2.setUsername("TestUser3");
        u2.setAge(25);
        u2.setUserType((short) 0);
        this.sqlSession.getMapper(ComplexUserMapper.class).insert(u2);
        this.sqlSession.commit();

        EntityTable userTable = EntityTable.of(ComplexUser.class);

        // (age < 20 OR username = 'TestUser3') AND userType = 1
        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w
                        .addGroup(g -> g
                                .add(userTable.field(ComplexUser.Fields.age).lt(20))
                                .add(AndOr.OR, userTable.field(ComplexUser.Fields.username), Operator.eq, "TestUser3"))
                )
                .where(w -> w
                        .add(userTable.field(ComplexUser.Fields.userType).eq((short) 1))
                )
                .build();

        List<ComplexUser> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        log.info("EzQuery NestedCondition: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryOrderBy() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(3, dept.getId());
        EntityTable userTable = EntityTable.of(ComplexUser.class);
        String orderField = null;
        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .orderBy(o -> {
                    o.add(userTable.field(ComplexUser.Fields.age).asc());
                    o.add(orderField != null, oo -> oo.add(userTable.field(orderField).asc()));
                    o.add(userTable.field(BaseEntity.Fields.createTime).desc());
                })
                .page(1, 5)
                .build();
        List<ComplexUser> users = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(users);
        log.info("EzQuery OrderBy: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQueryUnion() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(3, dept.getId());

        EntityTable userTable = EntityTable.of(ComplexUser.class);

        EzQuery<ComplexUser> q1 = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(s -> s.addAll(ComplexUser.Fields.avatar, ComplexUser.Fields.description))
                .where(w -> w.add(userTable.field(ComplexUser.Fields.status).eq(ComplexUser.UserStatus.NORMAL)))
                .build();

        EzQuery<ComplexUser> q2 = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(s -> s.addAll(ComplexUser.Fields.avatar, ComplexUser.Fields.description))
                .where(w -> w.add(userTable.field(ComplexUser.Fields.status).eq(ComplexUser.UserStatus.DISABLED)))
                .build();

        EzQuery<ComplexUser> q3 = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(s -> s.addAll(ComplexUser.Fields.avatar, ComplexUser.Fields.description))
                .where(w -> w.add(userTable.field(ComplexUser.Fields.status).eq(ComplexUser.UserStatus.DISABLED)))
                .unionAll(q2)
                .build();

        EzQuery<ComplexUser> unionQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(s -> s.addAll(ComplexUser.Fields.avatar, ComplexUser.Fields.description))
                .where(w -> w.add(userTable.field(ComplexUser.Fields.username).eq("NonExistent")))
                .union(q1)
                .unionAll(q3)
                .page(1, 10)
                .orderBy(o -> o.add(userTable.field(ComplexUser.Fields.username), OrderType.ASC))
                .build();

        List<ComplexUser> users = this.sqlSession.getMapper(EzMapper.class).query(unionQuery);
        Assert.assertNotNull(users);
        log.info("EzQuery Union: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQuerySubQuery() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EntityTable subTable = EntityTable.of(ComplexUser.class);

        // Select where ID in (Select ID from ...)
        EzQuery<String> subInfo = EzQuery.builder(String.class).from(subTable)
                .select(s -> s.add(subTable.field(BaseEntity.Fields.id)))
                .build();

        EntityTable userTable = EntityTable.of(ComplexUser.class);
        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class).from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w -> w.add(userTable.field(BaseEntity.Fields.id).in(subInfo)))
                .build();

        List<ComplexUser> users = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(users);
        log.info("EzQuery SubQuery: {}", JacksonUtils.toJsonString(users));
    }

    @Test
    public void ezQueryCount() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class).from(EntityTable.of(ComplexUser.class))
                .select(Select.EzSelectBuilder::addAll).build();
        int count = this.sqlSession.getMapper(EzMapper.class).queryCount(query);
        Assert.assertTrue(count >= 2);
        log.info("EzQuery Count: {}", count);
    }

    @Test
    public void ezQueryOne() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class).from(EntityTable.of(ComplexUser.class))
                .select(Select.EzSelectBuilder::addAll).limit(1).build();
        ComplexUser user = this.sqlSession.getMapper(EzMapper.class).queryOne(query);
        Assert.assertNotNull(user);
        log.info("EzQuery One: {}", JacksonUtils.toJsonString(user));
    }

    @Test
    public void encQuery() {
        String deptId = this.insertAndGetDepartment().getId();
        String id = this.insertAndGetComplexUserId(deptId);
        ComplexUser user = this.sqlSession.getMapper(ComplexUserMapper.class).selectById(id);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getExtInfo()); // 验证JSON转换
        EntityTable entityTable = EntityTable.of(ComplexUser.class);
        EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class).from(entityTable)
                .select(Select.EzSelectBuilder::addAll)
                .where(w ->
                        w.add(entityTable.field(ComplexUser.Fields.secretContent).eq(user.getSecretContent())))
                .page(1, 1)
                .build();
        ComplexUser encUser = this.sqlSession.getMapper(EzMapper.class).queryOne(query);
        log.info("ComplexUserMapper.encTest: {}", JacksonUtils.toJsonString(encUser));
    }
}
