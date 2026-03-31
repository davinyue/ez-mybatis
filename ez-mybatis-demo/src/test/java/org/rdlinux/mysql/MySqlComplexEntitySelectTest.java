package org.rdlinux.mysql;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.*;
import org.rdlinux.ezmybatis.demo.mapper.ComplexUserMapper;
import org.rdlinux.ezmybatis.enumeration.OrderType;
import org.rdlinux.ezmybatis.utils.StringHashMap;
import org.rdlinux.luava.json.JacksonUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class MySqlComplexEntitySelectTest extends MySqlBaseTest {

    private static final com.github.javafaker.Faker faker = new com.github.javafaker.Faker(java.util.Locale.CHINA);

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
        user.setSalary(new BigDecimal(faker.number().randomDouble(2, 3000, 50000)));
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

    private ComplexOrder insertAndGetOrder(String userId, ComplexOrder.OrderStatus status) {
        ComplexOrder order = new ComplexOrder();
        order.setUserId(userId);
        order.setOrderNo(faker.code().asin());
        order.setTotalAmount(new BigDecimal(faker.number().randomDouble(2, 10, 5000)));
        order.setStatus(status);
        this.sqlSession.getMapper(EzMapper.class).insert(order);
        this.sqlSession.commit();
        return order;
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
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());
        List<Map<String, Object>> maps = this.sqlSession.getMapper(EzMapper.class).selectMapBySql("SELECT * FROM ez_complex_user LIMIT 2", new HashMap<>());
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

        // Select all fields
        EzQuery<ComplexUser> queryAll = EzQuery.builder(ComplexUser.class).from(EntityTable.of(ComplexUser.class))
                .select().addAll().done()
                .page(1, 2)
                .build();
        List<ComplexUser> usersAll = this.sqlSession.getMapper(EzMapper.class).query(queryAll);
        Assert.assertNotNull(usersAll);
        log.info("EzQuery Basic Select All: {}", JacksonUtils.toJsonString(usersAll));

        // Select specific fields
        EzQuery<ComplexUser> querySpecific = EzQuery.builder(ComplexUser.class).from(EntityTable.of(ComplexUser.class))
                .select().addField(BaseEntity.Fields.id).addField(ComplexUser.Fields.username).done()
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
                    s.addField(BaseEntity.Fields.id);
                    s.addField(ComplexUser.Fields.username);
                    s.add(includeAge, userTable.field(ComplexUser.Fields.age));
                })
                .where(w -> w.addCondition(hasNameFilter, userTable.field(ComplexUser.Fields.username).like("被查用户_%")))
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

        ComplexOrder order = this.insertAndGetOrder(user1.getId(), ComplexOrder.OrderStatus.PAID);
        this.sqlSession.commit();

        // 1. EQ (= 枚举匹配)
        EzQuery<ComplexUser> eqQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(ComplexUser.Fields.status).eq(ComplexUser.UserStatus.NORMAL)).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(eqQuery));

        // 2. GT (>)
        EzQuery<ComplexUser> gtQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(ComplexUser.Fields.accountBalance).gt(200.0)).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(gtQuery));

        // 3. IS NOT NULL
        EzQuery<ComplexUser> isNotNullQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(ComplexUser.Fields.departmentId).isNotNull()).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(isNotNullQuery));

        // 4. IN
        EzQuery<ComplexUser> inQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(ComplexUser.Fields.age).in(Arrays.asList(20, 30))).done()
                .page(1, 2).build();
        Assert.assertNotNull(mapper.query(inQuery));

        // 5. BETWEEN
        EzQuery<ComplexUser> betweenQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select().addAll().done()
                .where().addCondition(userTable.field(ComplexUser.Fields.age).between(18, 25)).done()
                .page(1, 1).build();
        Assert.assertNotNull(mapper.query(betweenQuery));

        // 6. EXISTS (关联查询)
        EntityTable orderTableCondition = EntityTable.of(ComplexOrder.class);
        EzQuery<ComplexOrder> orderSubQuery = EzQuery.builder(ComplexOrder.class)
                .from(orderTableCondition).select().addField(BaseEntity.Fields.id).done()
                .where(e -> {
                    e.addCondition(orderTableCondition.field(ComplexOrder.Fields.userId)
                            .eq(userTable.field(BaseEntity.Fields.id)));
                })
                .build();

        EzQuery<ComplexUser> existsQuery = EzQuery.builder(ComplexUser.class).from(userTable)
                .select().addAll().done()
                .where().exists(orderSubQuery).done()
                .page(1, 10).build();
        List<ComplexUser> existsRet = mapper.query(existsQuery);
        Assert.assertNotNull(existsRet);
        log.info("EzQuery EXISTS: {}", JacksonUtils.toJsonString(existsRet));
    }

    @Test
    public void ezQueryJoin() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(2, dept.getId());

        EntityTable userTable = EntityTable.of(ComplexUser.class);
        EntityTable deptTable = EntityTable.of(ComplexDepartment.class);

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
                .from(userTable)
                .select().addAll().done()
                .join(deptTable)
                .addCondition(userTable.field(ComplexUser.Fields.departmentId).eq(deptTable.field(BaseEntity.Fields.id)))
                .done()
                .page(1, 10)
                .build();

        List<StringHashMap> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 2);
        log.info("EzQuery Join: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryNestedJoinLambdaDslTest() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        String uId = this.insertAndGetComplexUserId(dept.getId());
        this.insertAndGetOrder(uId, ComplexOrder.OrderStatus.PENDING);

        EntityTable userTable = EntityTable.of(ComplexUser.class);
        EntityTable deptTable = EntityTable.of(ComplexDepartment.class);
        EntityTable orderTable = EntityTable.of(ComplexOrder.class);

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
                .from(userTable)
                .select(Select.EzSelectBuilder::addAll)
                .join(deptTable, j -> {
                    j.addCondition(userTable.field(ComplexUser.Fields.departmentId).eq(deptTable.field(BaseEntity.Fields.id)));
                    // 同级连带查询订单
                    j.join(orderTable, jj -> jj.addCondition(
                            userTable.field(BaseEntity.Fields.id).eq(orderTable.field(ComplexOrder.Fields.userId))));
                })
                .limit(10)
                .build();

        List<StringHashMap> result = this.sqlSession.getMapper(EzMapper.class).query(query);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        log.info("EzQuery NestedJoinLambdaDsl: {}", JacksonUtils.toJsonString(result));
    }

    @Test
    public void ezQueryGroupByHaving() {
        ComplexDepartment dept = this.insertAndGetDepartment();
        this.insertAndGetComplexUserIds(3, dept.getId());

        EntityTable table = EntityTable.of(ComplexUser.class);

        // Count users by deptId
        Function countFn = Function.build("COUNT", f -> f.addArg(EntityField.of(table, BaseEntity.Fields.id)));

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select(s -> {
                    s.addField(ComplexUser.Fields.departmentId);
                    s.add(countFn, "count");
                })
                .groupBy(g -> g.addField(ComplexUser.Fields.departmentId))
                .having(h -> h.addCondition(Alias.of("count").ge(0)))
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

        // Formula: age + 1 (使用刚刚被强化改造且强限制为 .with() 起手的 Formula)
        Formula agePlusOne = Formula.build(f ->
                f.with(table.field(ComplexUser.Fields.age)).add(1));

        // Function: CONCAT(username, ' - ', age)
        Function nameDesc = Function.build("CONCAT", f -> f
                .addArg(EntityField.of(table, ComplexUser.Fields.username))
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

        CaseWhen ageGroup = CaseWhen.build(table, c -> c
                .when(w -> w.addCondition(table.field(ComplexUser.Fields.age).lt(19)), "Young")
                .when(w -> w.addCondition(table.field(ComplexUser.Fields.age).ge(19)), "Adult")
                .els("Unknown")
        );

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select(s -> {
                    s.addField(ComplexUser.Fields.username);
                    s.addField(ComplexUser.Fields.age);
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
        WindowFunction wf1 = WindowFunction.builder(countFunc1).build();

        // 2. 多个 partition, 多个 order
        Function rowNumFunc = Function.build("ROW_NUMBER", f -> {
        });
        WindowFunction wf2 = WindowFunction.builder(rowNumFunc)
                .partitionBy(EntityField.of(table, ComplexUser.Fields.status))
                .orderBy(EntityField.of(table, ComplexUser.Fields.username), OrderType.ASC)
                .orderBy(EntityField.of(table, BaseEntity.Fields.createTime), OrderType.DESC)
                .build();

        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .select(s -> {
                    s.addField(ComplexUser.Fields.username);
                    s.addField(ComplexUser.Fields.status);
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
}
