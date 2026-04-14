# Ez-MyBatis

> **兼容性提示**：Ez-MyBatis 支持 Spring Boot 2.x / 3.x，支持 MyBatis 与 MyBatis-Plus 两种生态。请根据项目栈选择对应 Starter 依赖。

## 🚀 简介

**Ez-MyBatis** 是一个基于 **MyBatis 拦截器机制** 的数据访问层增强框架。它在 **无需修改 MyBatis 源码** 的前提下，为你提供统一、简洁、类型安全的通用 Mapper 接口与流式 DSL（EzQuery / EzUpdate / EzDelete 等），并兼容常用 JPA 注解，显著降低手写 XML 与重复 Mapper 的工作量，让你更专注业务逻辑开发。

---

## 💡 为什么选择 Ez-MyBatis

- 你厌倦了维护大量 XML 映射？
- 想在 **MyBatis + 多数据库** 项目中有统一上层抽象？
- 需要 **类型安全查询构建器** 避免字段写错？
- 原生 JOIN 支持，无需手写复杂 SQL，Ez-MyBatis 提供了完整的连表查询、连表删除等 DSL，支持 INNER JOIN / LEFT JOIN / RIGHT JOIN 等常用连接方式，并且提供极简的 **Lambda DSL**。
- 既要轻量，又希望在大批量场景里用 **JDBC 极致性能**？

---

## ✨ 特性

- 🔧 **零侵入**：基于拦截器，无需改动现有 MyBatis 配置或 Mapper 接口即可逐步接入。
- 📊 **多数据库支持**：MySQL、Oracle、达梦、PostgreSQL、SQL Server；亦可通过兼容配置支持其他国产数据库。
- 🏷️ **JPA 注解支持**：`@Table`、`@Id`、`@Column`、`@Transient`、`@TypeHandler` 等常用注解开箱即用。
- ⚡ **高性能通道**：提供 JDBC 直接操作 DAO（如 `JdbcInsertDao`、`JdbcUpdateDao`），大批量导入/更新/替换更高效。
- 🎯 **类型安全 DSL**：全面支持 Lambda 风格 DSL 以构建 CRUD 操作，并且支持复杂表达式（Formula）、函数（Function）、条件分支（CaseWhen）及窗口函数（WindowFunction）。
- 🔌 **事件机制**：读写生命周期点均可注入逻辑（如加解密、审计、埋点）。
- 🧪 **可混用**：Ez-MyBatis DSL 可与原生 MyBatis / MyBatis-Plus 共存，便于渐进式迁移。

---

## 📋 支持的数据库

| 数据库 | 支持状态 | 备注 |
|---|---|---|
| MySQL | ✅ | 完全支持 |
| Oracle | ✅ | 完全支持 |
| 达梦 | ✅ | 完全支持 |
| PostgreSQL | ✅ | 完全支持 |
| SQL Server | ✅ | 完全支持 |
| 其他国产数据库 | ⚠️ | 需配置兼容的数据库类型 |

Ez-MyBatis 会自动根据数据源的 Driver Class 识别数据库类型，通常情况下**无需手动配置**。对于无法自动识别的数据库（如高斯），可配置为兼容的类型（示例：配置为 POSTGRE_SQL）：
```yaml
ez-mybatis:
  db-type: POSTGRE_SQL
```


## 📋 快速开始

### 依赖集成

**Spring Boot 2.x 集成 MyBatis**
```xml
<dependency>
    <groupId>org.rdlinux</groupId>
    <artifactId>ez-mybatis-spring-boot-starter</artifactId>
    <version>1.0.5.RS</version>
</dependency>
```

**Spring Boot 3.x 集成 MyBatis**
```xml
<dependency>
    <groupId>org.rdlinux</groupId>
    <artifactId>ez-mybatis-spring3-boot-starter</artifactId>
    <version>1.0.5.RS</version>
</dependency>
```

---

## 📝 测试模型与实体关联关系
推荐使用 Lombok（`@Getter @Setter @FieldNameConstants`）自动生成字段常量，便于类型安全 DSL 构建。

为了更好地展示 Ez-MyBatis 的强大功能，本项目测试用例中定义了一套贴近真实业务的实体模型结构。各核心业务模型间的关联主从关系如下：

- **部门模型 (`ComplexDepartment`)**
  - 代表组织架构中的部门。
  - **关联关系**：一个部门可以包含多个用户（1 对 N），通过树形结构自身关联 (`parentId`) 描述层级关系。
- **用户模型 (`ComplexUser`)**
  - 代表系统的核心用户信息，包含基本属性（`age`, `salary`, `status`）及 JSON 扩展信息的映射（`ExtInfo`）。
  - **关联关系**：
    - 归属于某个部门（多对一，依赖 `departmentId` 字段）。
    - 是多个订单的归属者（一对多）。
- **订单模型 (`ComplexOrder`)**
  - 代表用户的消费订单，记录金额及状态等。
  - **关联关系**：
    - 属于某一个用户（多对一，依赖 `userId` 字段，关联至 `ComplexUser` 的主键 `id`）。
    - 包含多个订单明细记录（一对多）。
- **订单明细模型 (`ComplexOrderItem`)**
  - 代表订单内购买的具体商品及数量。
  - **关联关系**：作为归属于某个订单的明细行项存在（多对一，依赖 `orderId` 字段，关联至 `ComplexOrder` 的主键 `id`）。

### 实体类参考示例

```java
// 基础实体，统一定义 ID 与公共时间字段
@Getter
@Setter
@FieldNameConstants
public abstract class BaseEntity {
    @Id
    private String id;
    private Date createTime;
    private Date updateTime;
}

// 用户实体
@Table(name = "ez_complex_user")
@Getter
@Setter
@FieldNameConstants
public class ComplexUser extends BaseEntity {
    private String username;
    private Integer age;
    private BigDecimal salary;
    // ... 其它属性
    
    // JSON扩展字段映射
    @TypeHandler(JsonTypeHandler.class)
    private ExtInfo extInfo;
    
    // 关联部门 ID
    private String departmentId;
}

// 部门实体
@Table(name = "ez_complex_department")
@Getter
@Setter
@FieldNameConstants
public class ComplexDepartment extends BaseEntity {
    private String name;
    private String parentId;
}
```

---

## 💾 数据操作（CRUD）实战

借助 Lambda DSL，你可以安全且直观地对上述实体及其交织的关联结构进行数据操作。

### 📌 插入数据 (Insert)

你可以使用原生的 `EzMapper` 接口操作对象，或者使用具备极高批量吞吐能力的 `JdbcInsertDao`。

```java
// 1. 标准单条与批量插入
ezMapper.insert(complexUser);
ezMapper.batchInsert(complexUsers);
ezMapper.insertByTable(EntityTable.of(ComplexUser.class), complexUser);

// 2. 高性能 JDBC 插入
JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(sqlSession);
jdbcInsertDao.insert(complexUser);
jdbcInsertDao.batchInsert(complexUsers);

// 3. 通过查询结果写入目标表
EntityTable orderTable = EntityTable.of(ComplexOrder.class);
EzQuery<ComplexOrder> query = EzQuery.builder(ComplexOrder.class)
        .from(orderTable)
        .select(Select.EzSelectBuilder::addAll)
        .where(w -> w.add(orderTable.field(BaseEntity.Fields.id).eq(seedOrder.getId())))
        .build();

EntityTable archiveTable = EntityTable.of(ComplexOrder.class, "ez_complex_order_archive");
ezMapper.insertByQuery(archiveTable, query);

// 4. MySQL 特有 Upsert (INSERT ... ON DUPLICATE KEY UPDATE)
EntityTable table = EntityTable.of(ComplexUser.class);
MySqlUpsert upsert = MySqlUpsert.into(table)
        .insert(complexUser)
        .onDuplicateKeyUpdate()
        .set()
        .add(table.field(ComplexUser.Fields.username).set("new_name_on_conflict"))
        .done()
        .build();
ezMapper.expandUpdate(upsert);
```

### 📌 更新数据 (Update)

支持对象级全体更新、部分字段更新，以及依托复杂条件或 `CASE WHEN` / `Formula` 构建的灵活表达式更新。另外支持如 Oracle 的 `MERGE INTO` 操作。

```java
EntityTable table = EntityTable.of(ComplexUser.class);

// 1. 根据 Lambda DSL 构造条件更新
EzUpdate ezUpdate = EzUpdate.update(table)
        .set(s -> {
            s.add(table.field(ComplexUser.Fields.username).set("Updated Value"));
            s.add(true, table.field(ComplexUser.Fields.age).set(99));
        })
        .where(w -> w.add(table.field(BaseEntity.Fields.id).eq("user_1")))
        .build();
ezMapper.ezUpdate(ezUpdate);

// 2. Formula / Function / setToNull
Formula scoreFormula = Formula.build(f ->
        f.with(table.field(ComplexUser.Fields.score)).add(1));

Function function = Function.builder("GREATEST")
        .addArg(EntityField.of(table, ComplexUser.Fields.age))
        .addArg(100)
        .build();

EzUpdate funcUpdate = EzUpdate.update(table)
        .set(s -> {
            s.add(table.field(ComplexUser.Fields.score).set(scoreFormula));
            s.add(table.field(ComplexUser.Fields.age).set(function));
            s.add(table.field(ComplexUser.Fields.departmentId).setToNull());
        })
        .where(w -> w.add(table.field(BaseEntity.Fields.id).eq("user_1")))
        .build();
ezMapper.ezUpdate(funcUpdate);

// 3. CaseWhen 更新
CaseWhen caseWhen = CaseWhen.build(c -> c
        .when(w -> w.add(table.field(ComplexUser.Fields.username).eq("old_name")), "new_name")
        .els(EntityField.of(table, ComplexUser.Fields.username)));

EzUpdate caseWhenUpdate = EzUpdate.update(table)
        .set(s -> s.add(table.field(ComplexUser.Fields.username).set(caseWhen)))
        .where(w -> w.add(table.field(BaseEntity.Fields.id).eq("user_1")))
        .build();
ezMapper.ezUpdate(caseWhenUpdate);

// 4. 批量执行多个 EzUpdate
ezMapper.ezBatchUpdate(Arrays.asList(ezUpdate, funcUpdate, caseWhenUpdate));

// 5. 针对 Oracle / 达梦 / PG / MSSQL 等数据库的结构化 MERGE INTO 功能
// sourceTable.column(...) 的列名大小写应与源查询输出列及数据库大小写策略保持一致
EzQueryTable sourceTable = EzQueryTable.of(sourceQuery);
Merge merge = Merge.into(table)
        .using(sourceTable)
        .on(o -> o.add(table.field(ComplexUser.Fields.departmentId).eq(sourceTable.column("id"))))
        .set(s -> s.add(table.field(ComplexUser.Fields.username).set("merge_update_name")))
        .whenNotMatchedThenInsert(insertModel)
        .build();
ezMapper.expandUpdate(merge);
```

### 📌 删除数据 (Delete)

除了按 ID 等基础方式删除外，支持丰富的且复杂的 DSL 判断以及连表支持。

```java
EntityTable itemTable = EntityTable.of(ComplexOrderItem.class);
EntityTable orderTable = EntityTable.of(ComplexOrder.class);

// 1. 基础方式删除
ezMapper.deleteById(ComplexUser.class, "user_1");
ezMapper.batchDeleteById(ComplexUser.class, Arrays.asList("user_1", "user_2"));

// 2. EzDelete 支持 JOIN 与条件拼装, 注意只有MySql系列数据库支持delete join
EzDelete delete = EzDelete.delete(itemTable)
        .delete(orderTable)
        .join(true, orderTable, j -> j.add(itemTable.field(ComplexOrderItem.Fields.orderId)
                .eq(orderTable.field(BaseEntity.Fields.id))))
        .where(w -> w.add(orderTable.field(BaseEntity.Fields.id).eq(orderId)))
        .build();

ezMapper.ezDelete(delete);
```

---

## 🔍 查询数据 (Select)

`EzQuery` 提供极致的 Lambda 查询连缀体验，内建强类型校验保障质量。包含复杂的多表联查、分组统计和各种窗口函数等构建能力。

```java
EntityTable userTable = EntityTable.of(ComplexUser.class);
EntityTable deptTable = EntityTable.of(ComplexDepartment.class);

// 1. 常规范围及模糊匹配
EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class)
        .from(userTable)
        .select(s -> {
            s.add(userTable.field(BaseEntity.Fields.id));
            s.add(userTable.field(ComplexUser.Fields.username));
            s.add(true, userTable.field(ComplexUser.Fields.age));
        })
        .where(w -> w.add(userTable.field(ComplexUser.Fields.username).like("被查用户_%")))
        .orderBy(o -> o.add(userTable.field(ComplexUser.Fields.age), OrderType.ASC))
        .limit(10)
        .build();
List<ComplexUser> users = ezMapper.query(query);

// 2. 嵌套 JOIN：部门 -> 用户 -> 订单
EzQuery<ComplexDepartment> joinQuery = EzQuery.builder(ComplexDepartment.class)
        .from(deptTable)
        .select(Select.EzSelectBuilder::addAll)
        .join(userTable, j -> {
            j.add(deptTable.field(BaseEntity.Fields.id).eq(userTable.field(ComplexUser.Fields.departmentId)));
            j.join(orderTable, jj -> jj.add(
                    userTable.field(BaseEntity.Fields.id).eq(orderTable.field(ComplexOrder.Fields.userId))));
        })
        .limit(10)
        .build();

// 3. 统计聚集查询 (GROUP BY / HAVING / Function)
Function countFn = Function.build("COUNT", f -> f.addArg(EntityField.of(userTable, BaseEntity.Fields.id)));
EzQuery<StringHashMap> aggQuery = EzQuery.builder(StringHashMap.class)
        .from(userTable)
        .select(s -> {
            s.add(userTable.field(ComplexUser.Fields.departmentId));
            s.add(countFn, "userCount");
        })
        .groupBy(g -> g.add(userTable.field(ComplexUser.Fields.departmentId)))
        .having(h -> h.add(countFn.ge(1)))
        .build();

// 4. Function / Formula / CaseWhen / WindowFunction
Formula agePlusOne = Formula.build(f ->
        f.with(userTable.field(ComplexUser.Fields.age)).add(1));
CaseWhen ageGroup = CaseWhen.build(c -> c
        .when(w -> w.add(userTable.field(ComplexUser.Fields.age).lt(19)), "Young")
        .when(w -> w.add(userTable.field(ComplexUser.Fields.age).ge(19)), "Adult")
        .els("Unknown"));

Function rowNumFunc = Function.build("ROW_NUMBER", f -> {});
WindowFunction wf = WindowFunction.build(rowNumFunc, w -> w
        .partitionBy(EntityField.of(userTable, ComplexUser.Fields.status))
        .orderBy(EntityField.of(userTable, ComplexUser.Fields.username), OrderType.ASC)
        .orderBy(EntityField.of(userTable, BaseEntity.Fields.createTime), OrderType.DESC));

EzQuery<StringHashMap> advancedQuery = EzQuery.builder(StringHashMap.class).from(userTable)
        .select(s -> {
            s.add(userTable.field(ComplexUser.Fields.username));
            s.add(agePlusOne, "nextYearAge");
            s.add(ageGroup, "ageGroup");
            s.add(wf, "userRank");
        })
        .limit(10)
        .build();

// 5. 子查询与计数
EzQuery<String> subQuery = EzQuery.builder(String.class)
        .from(userTable)
        .select(s -> s.add(userTable.field(BaseEntity.Fields.id)))
        .build();

EzQuery<ComplexUser> inQuery = EzQuery.builder(ComplexUser.class)
        .from(userTable)
        .select(Select.EzSelectBuilder::addAll)
        .where(w -> w.add(userTable.field(BaseEntity.Fields.id).in(subQuery)))
        .build();

int count = ezMapper.queryCount(inQuery);
```

---

## 🔔 事件监听扩展点

Ez-MyBatis 在数据操作生命周期提供多个监听接口，方便做 **审计、埋点、加解密、缓存、数据脱敏** 等逻辑扩展。只需实现接口并注册为 Spring Bean 即可生效 (`order()` 可控优先级)。

| 监听器接口 | 触发时机 | 典型用途 |
|-----------|---------|---------|
| `EzMybatisInsertListener` | 插入操作执行前 | ID 生成、创建时间填充、审计字段注入 |
| `EzMybatisUpdateListener` | 更新/替换操作执行前 | 更新时间填充、乐观锁版本号递增 |
| `EzMybatisDeleteListener` | 删除操作执行前 | 逻辑删除标记、删除审计日志 |
| `EzMybatisQueryRetListener` | 查询结果构造完成后 | 数据脱敏、敏感字段解密、关联数据补充 |
| `EzMybatisOnBuildSqlGetFieldListener` | SQL 构建时读取实体属性值 | 字段级加密/解密、数据掩码 |

### 📌 SQL 构建字段获取监听器（`EzMybatisOnBuildSqlGetFieldListener`）

该监听器在框架构建 SQL 时读取实体属性值的节点触发，通过 `FieldAccessScope` 枚举区分两种不同的访问场景：

| `FieldAccessScope` | 含义 | 典型用途 |
|---|---|---|
| `ENTITY_PERSIST` | 整体实体持久化：insert / update / replace 等操作遍历实体字段写入 DB | 对所有持久化字段做统一加密后入库 |
| `DSL_CONDITION` | DSL 条件构造：EzQuery / EzUpdate / EzDelete 中用户手动绑定的字段值 | where 条件明文值加密后与数据库密文比对；EzUpdate 指定字段设值时加密 |

#### 使用示例：对 `secretContent` 字段透明加解密
```java
// 注册监听 — SQL 构建时对指定字段加密
EzMybatisContent.addOnBuildSqlGetFieldListener(config, (scope, entityType, field, value) -> {
    if ("secretContent".equals(field.getName()) && value instanceof String) {
        // 不论 ENTITY_PERSIST 还是 DSL_CONDITION，统一加密
        return EncryptUtil.encrypt((String) value);
    }
    return value;
});

// 注册监听 — 查询结果返回时对指定字段解密
EzMybatisContent.addQueryRetListener(config, new EzMybatisQueryRetListener() {
    @Override
    public Object onBuildDone(Object model) {
        if (model instanceof ComplexUser) {
            String encrypted = ((ComplexUser) model).getSecretContent();
            if (encrypted != null) {
                ((ComplexUser) model).setSecretContent(EncryptUtil.decrypt(encrypted));
            }
        }
        return model;
    }
});
```

---

## ❓ 常见问题 FAQ

### Q1: 如何处理复杂的查询条件（嵌套 AND / OR）？
使用 `addGroup()` 即可构建嵌套条件：
```java
EntityTable table = EntityTable.of(ComplexUser.class);

EzQuery<ComplexUser> query = EzQuery.builder(ComplexUser.class)
        .from(table)
        .select(Select.EzSelectBuilder::addAll)
        .where(w -> w
                // 开始嵌套条件分组 ( age < 20 OR name = 'TestUser' )
                .addGroup(g -> g
                        .add(table.field(ComplexUser.Fields.age).lt(20))
                        .add(AndOr.OR, table.field(ComplexUser.Fields.username), Operator.eq, "TestUser"))
                .add(table.field(ComplexUser.Fields.userType).eq((short) 1)))
        .build();
```

### Q2: EzMapper 和 JDBC 方式如何选择？
- **少量数据、简单 CRUD**：推荐使用 `EzMapper` 及 `EzQuery/EzUpdate/EzDelete` DSL 进行操作，代码清晰强大。
- **大批量导入/批量更新**：推荐使用极速单向的 `JdbcInsertDao` 与 `JdbcUpdateDao` 以获得原生 JDBC 处理性能，减少对象映射开销。

---
## 📄 许可证

本项目基于 **Apache License 2.0** 开源，可自由用于商业与非商业用途，须保留版权与许可证声明。

## 🤝 贡献

欢迎提交 Issue、Pull Request 帮助改进项目！
- 修复 bug
- 扩展数据库方言
- 增补文档、示例、测试用例

谢谢使用 **Ez-MyBatis**！如果它对你有帮助，欢迎点个 ⭐ Star 支持一下！
