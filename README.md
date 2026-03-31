# Ez-MyBatis

> **兼容性提示**：Ez-MyBatis 支持 Spring Boot 2.x / 3.x，支持 MyBatis 与 MyBatis-Plus 两种生态。请根据项目栈选择对应 Starter 依赖。

## 🚀 简介

**Ez-MyBatis** 是一个基于 **MyBatis 拦截器机制** 的数据访问层增强框架。它在 **无需修改 MyBatis 源码** 的前提下，为你提供统一、简洁、类型安全的通用 Mapper 接口与流式 DSL（EzQuery / EzUpdate / EzDelete 等），并兼容常用 JPA 注解，显著降低手写 XML 与重复 Mapper 的工作量，让你更专注业务逻辑开发。

---

## 💡 为什么选择 Ez-MyBatis

- 你厌倦了维护大量 XML 映射？
- 想在 **MyBatis + 多数据库** 项目中有统一上层抽象？
- 需要 **类型安全查询构建器** 避免字段写错？
- 原生 JOIN 支持，无需手写复杂 SQL, Ez-MyBatis 提供了完整的连表查询 DSL，可无缝替代原生 SQL 的 JOIN 写法，支持：INNER JOIN / LEFT JOIN / RIGHT JOIN 等常用连接方式。并且支持极简的 **Lambda DSL**。
- 既要轻量，又希望在大批量场景里用 **JDBC 极致性能**？

Ez-MyBatis 正是为此而生：在不破坏原生态的前提下增强 MyBatis，可按需渐进接入，低成本迁移、低学习曲线。

---

## ✨ 特性

- 🔧 **零侵入**：基于拦截器，无需改动现有 MyBatis 配置或 Mapper 接口即可逐步接入。
- 📊 **多数据库支持**：MySQL、Oracle、达梦、PostgreSQL、SQL Server；亦可通过兼容配置支持其他国产数据库。
- 🏷️ **JPA 注解支持**：@Table、@Id、@Column、@Transient、@TypeHandler 等常用注解开箱即用。
- ⚡ **高性能通道**：提供 JDBC 直接操作 DAO（如 `JdbcInsertDao`、`JdbcUpdateDao`），大批量导入/更新/替换更高效。
- 🎯 **类型安全 DSL**：全面支持 Lambda 风格 DSL（Lambda DSL）以构建查询、更新和删除，并且支持复杂表达式（Formula）、函数（Function）、条件分支（CaseWhen）及窗口函数（WindowFunction）。
- 🔌 **事件机制**：插入、更新、删除、查询、SQL 构建等生命周期点均可注入自定义逻辑（如加解密、审计、埋点）。
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

---

## 🛠️ 快速开始

### Spring Boot 2.x 集成 MyBatis
```xml
<dependency>
    <groupId>org.rdlinux</groupId>
    <artifactId>ez-mybatis-spring-boot-starter</artifactId>
    <version>1.0.3.RS</version>
</dependency>
```

### Spring Boot 3.x 集成 MyBatis
```xml
<dependency>
    <groupId>org.rdlinux</groupId>
    <artifactId>ez-mybatis-spring3-boot-starter</artifactId>
    <version>1.0.3.RS</version>
</dependency>
```

---

## 📝 实体类定义

推荐使用 Lombok（`@Getter @Setter @FieldNameConstants`）自动生成字段常量，便于类型安全 DSL 构建。

```java
@Getter
@Setter
@FieldNameConstants
public abstract class BaseEntity {
    @Id
    private String id;
    private Date createTime;
    private Date updateTime;
}

@Table(name = "ez_user", schema = "ez_mybatis")
@Getter
@Setter
@FieldNameConstants
public class User extends BaseEntity {
    @TypeHandler(StringTypeHandler.class)
    private String name;
    private Sex sex;
    @Column(name = "age")
    private Integer userAge;
    @Transient
    private String ignore;

    public enum Sex { WOMAN, MAN }
}
```

---

## 💾 数据操作（CRUD）

Ez-MyBatis 提供了极简且强大的数据操作 API。大部分示例基于全新的 **Lambda DSL**。

### 📌 插入数据 (Insert)

```java
// 1. 单条与批量插入 (Mapper方式)
ezMapper.insert(user);
ezMapper.batchInsert(users);

// 2. 动态表名插入
ezMapper.insertByTable(EntityTable.of(User.class, "dynamic_table"), user);

// 3. 高性能 JDBC 插入 (适合大批量)
jdbcInsertDao.insert(user);
jdbcInsertDao.batchInsert(users);

// 4. INSERT INTO ... SELECT 方式
EzQuery<UserDump> query = EzQuery.builder(UserDump.class).from(EntityTable.of(UserDump.class))
        .select(s -> s.addAll()).build();
ezMapper.insertByQuery(EntityTable.of(User.class), query);
```

### 📌 更新数据 (Update)

#### 基础更新与替换
```java
// 仅更新非空字段 (忽略 null 值)
ezMapper.update(user);
ezMapper.batchUpdate(users);

// 替换所有字段 (包括 null 值)
ezMapper.replace(user);
ezMapper.batchReplace(users);

// 高性能 JDBC 更新/替换
jdbcUpdateDao.batchUpdate(users);
jdbcUpdateDao.batchReplace(users);
// JDBC 指定字段更新
jdbcUpdateDao.update(user, Arrays.asList(User.Fields.name, User.Fields.age));
```

#### Lambda DSL 条件更新
使用全新的 Lambda DSL 构建复杂更新语句，支持动态条件。
```java
EntityTable table = EntityTable.of(User.class);
boolean updateAge = true;

EzUpdate ezUpdate = EzUpdate.update(table)
        .set(s -> {
            s.add(table.field(User.Fields.name).set("Lambda Update"));
            // 支持预置条件动态设值
            s.add(updateAge, table.field(User.Fields.age).set(99));
            // 将字段置为 NULL
            s.add(table.field(User.Fields.email).setToNull());
        })
        .where(w -> w.addCondition(table.field(BaseEntity.Fields.id).eq("1")))
        .build();

ezMapper.ezUpdate(ezUpdate);

// 多操作并行（批量）处理
ezMapper.ezBatchUpdate(Arrays.asList(ezUpdate1, ezUpdate2));
```

#### 函数(Function) / 表达式(Formula) / 分支(CaseWhen) 更新
```java
// 表达式 Formula: age + 10
Formula formula = Formula.builder(table.field(User.Fields.age)).add(10).done().build();

// 函数 Function: GREATEST(age, 100)
Function function = Function.builder("GREATEST")
        .addArg(EntityField.of(table, User.Fields.age)).addArg(100).build();

// 条件分支 CaseWhen
CaseWhen caseWhen = CaseWhen.builder(table)
        .when().addCondition(table.field(User.Fields.name).eq("老王")).then("VIP")
        .els("普通");

EzUpdate update = EzUpdate.update(table)
        .set(s -> {
            s.add(table.field(User.Fields.age).set(formula));
            s.add(table.field(User.Fields.level).set(caseWhen));
        })
        .where(w -> w.addCondition(table.field(BaseEntity.Fields.id).eq("1")))
        .build();

ezMapper.ezUpdate(update);
```

### 📌 删除数据 (Delete)

```java
// 1. 基础删除
ezMapper.delete(user);
ezMapper.deleteById(User.class, "id1");
ezMapper.batchDeleteById(User.class, Arrays.asList("id1", "id2"));

// 2. Lambda DSL 条件删除
EntityTable userTable = EntityTable.of(User.class);
EzDelete delete = EzDelete.delete(userTable)
        .where(w -> w.addCondition(userTable.field(User.Fields.name).eq("张三")))
        .build();
ezMapper.ezDelete(delete);

// 批量执行多个复杂删除条件
ezMapper.ezBatchDelete(Arrays.asList(delete1, delete2));

// 3. 联表删除(联表删除仅支持mysql, 其它数据库只能单表删除)
EntityTable uoTable = EntityTable.of(UserOrg.class);
EzDelete joinDelete = EzDelete.delete(userTable).delete(uoTable)
        .join(true, uoTable, j -> j.addCondition(
            userTable.field(BaseEntity.Fields.id).eq(uoTable.field(UserOrg.Fields.userId))
        ))
        .where(w -> w.addCondition(userTable.field(BaseEntity.Fields.id).eq("1")))
        .build();
ezMapper.ezDelete(joinDelete);
```

---

## 🔍 查询数据 (Select)

Ez-MyBatis 的 `EzQuery` 提供了强大的 SQL 构建能力，并全面支持 Lambda 表达式链式调用，使得代码可读性大幅提升。

### 📌 基础与简单查询
```java
// 按 ID 查询
User user = ezMapper.selectById(User.class, "id1");
// 批量 ID 查询
List<User> users = ezMapper.selectByIds(User.class, Arrays.asList("id1", "id2"));
// 原生查询
List<User> sqlUsers = ezMapper.selectObjectBySql(User.class, "SELECT * FROM ez_user LIMIT 2", params);
```

### 📌 Lambda DSL 结构化查询
```java
EntityTable userTable = EntityTable.of(User.class);
boolean hasNameFilter = true;

EzQuery<User> query = EzQuery.builder(User.class)
        .from(userTable)
        .select(s -> {
            s.addField(BaseEntity.Fields.id);
            s.addField(User.Fields.name);
            s.add(userTable.field(User.Fields.age));
        })
        .where(w -> {
            // 支持动态条件(Condition)
            w.addCondition(hasNameFilter, userTable.field(User.Fields.name).like("TestUser%"));
            w.addCondition(userTable.field(User.Fields.age).between(10, 30)); // 范围查询专用 API
            w.addCondition(userTable.field(User.Fields.sex).in(Arrays.asList(User.Sex.MAN))); // IN
        })
        .orderBy(o -> {
            o.add(userTable.field(User.Fields.age).asc());
            o.add(userTable.field(BaseEntity.Fields.createTime).desc());
        })
        .page(1, 10)  // 分页
        .build();

List<User> result = ezMapper.query(query);
```

### 📌 复杂联表查询 (Join)
通过 Lambda DSL 支持嵌套多层的联接与精确控制，自动处理联表逻辑。
```java
EntityTable userTable = EntityTable.of(User.class);
EntityTable orgTable = EntityTable.of(UserOrg.class);
EntityTable nestedTable = EntityTable.of(UserOrg.class);

EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
        .from(userTable)
        .select(Select.EzSelectBuilder::addAll)
        // 支持嵌套联接构建
        .join(orgTable, j -> {
            j.addCondition(userTable.field(BaseEntity.Fields.id).eq(orgTable.field(UserOrg.Fields.userId)));
            j.join(nestedTable, jj -> jj.addCondition(
                    orgTable.field(UserOrg.Fields.orgId).eq(nestedTable.field(UserOrg.Fields.orgId))));
        })
        .limit(10)
        .build();
```

### 📌 分组聚合 (GroupBy & Having)
```java
Function countFn = Function.builder("COUNT").addArg(EntityField.of(userTable, BaseEntity.Fields.id)).build();

EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(userTable)
        .select(s -> {
            s.addField(User.Fields.age);
            s.add(countFn, "userCount");
        })
        .groupBy(g -> g.addField(User.Fields.age))
        .having(h -> h.addCondition(countFn.ge(1)))
        .build();
```

### 📌 窗口函数 (Window Function)
Ez-MyBatis 全面支持复杂的数据库窗口函数，包括 `PARTITION BY`、`ORDER BY` 以及窗口帧定义 (`ROWS` / `RANGE`)。
```java
EntityTable table = EntityTable.of(User.class);
Function rowNumFunc = Function.builder("ROW_NUMBER").build();

// 构建窗口函数：ROW_NUMBER() OVER(PARTITION BY sex ORDER BY create_time DESC)
WindowFunction wf = WindowFunction.builder(rowNumFunc)
        .partitionBy(EntityField.of(table, User.Fields.sex))
        .orderBy(EntityField.of(table, BaseEntity.Fields.createTime).desc())
        .build();

// 带有行范围的窗口函数：ROWS BETWEEN 2 PRECEDING AND 2 FOLLOWING
Function sumAgeFunc = Function.builder("SUM").addArg(EntityField.of(table, User.Fields.age)).build();
WindowFunction wfRows = WindowFunction.builder(sumAgeFunc)
        .partitionBy(EntityField.of(table, User.Fields.sex))
        .orderBy(EntityField.of(table, User.Fields.age).asc())
        .rowsBetween(WindowFunction.WindowFrameBound.preceding(2), WindowFunction.WindowFrameBound.following(2))
        .build();

EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
        .select(s -> {
            s.addField(User.Fields.name);
            s.add(wf, "rn");
            s.add(wfRows, "sumAge");
        })
        .limit(10)
        .build();
```

### 📌 子查询, EXISTS 与 UNION
支持在 `IN` / `EXISTS` 条件中使用子查询以及原生的 `UNION` / `UNION ALL`。
```java
// 子查询 IN
EzQuery<String> subInfo = EzQuery.builder(String.class).from(EntityTable.of(User.class))
        .select(s -> s.addField(BaseEntity.Fields.id)).build();
EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
        .select(s -> s.addAll())
        .where(w -> w.addCondition(userTable.field(BaseEntity.Fields.id).in(subInfo)))
        .build();

// EXISTS
EntityTable userOrgTable = EntityTable.of(UserOrg.class);
EzQuery<UserOrg> orgSubQuery = EzQuery.builder(UserOrg.class)
        .from(userOrgTable).select(s -> s.addField(BaseEntity.Fields.id))
        .where(w -> w.addCondition(userOrgTable.field(UserOrg.Fields.userId).eq(userTable.field(BaseEntity.Fields.id))))
        .build();

EzQuery<User> existsQuery = EzQuery.builder(User.class).from(userTable)
        .select(s -> s.addAll())
        .where(w -> w.exists(orgSubQuery))  // 也支持 .notExists(orgSubQuery)
        .build();

// UNION 合并
EzQuery<User> q1 = ...;
EzQuery<User> unionQuery = EzQuery.builder(User.class).from(userTable)
        .select(s -> s.addAll())
        .union(q1)
        .build();
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
        if (model instanceof User) {
            String encrypted = ((User) model).getSecretContent();
            if (encrypted != null) {
                ((User) model).setSecretContent(EncryptUtil.decrypt(encrypted));
            }
        }
        return model;
    }
});
```

---

## ❓ 常见问题 FAQ

### Q1: 如何处理复杂的查询条件（嵌套 AND / OR）？
使用 `groupCondition()` 以及支持 Lambda 传入以构建任意嵌套逻辑：
```java
EzQuery<User> query = EzQuery.builder(User.class)
        .from(table)
        .where(w -> {
            w.addCondition(table.field(User.Fields.sex).eq(User.Sex.MAN));
            w.groupCondition() // 开始嵌套条件分组 ( age < 20 OR name = 'TestUser' )
                .addCondition(table.field(User.Fields.age).lt(20))
                .addCondition(AndOr.OR, table.field(User.Fields.name), Operator.eq, "TestUser")
                .done(); // 结束分组
        })
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
