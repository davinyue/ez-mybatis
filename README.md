# Ez-MyBatis

&#x20; &#x20;

> **兼容性提示**：Ez-MyBatis 支持 Spring Boot 2.x / 3.x，支持 MyBatis 与 MyBatis-Plus 两种生态。请根据项目栈选择对应
> Starter 依赖。

## 🚀 简介

**Ez-MyBatis** 是一个基于 **MyBatis 拦截器机制** 的数据访问层增强框架。它在 **无需修改 MyBatis 源码**
的前提下，为你提供统一、简洁、类型安全的通用 Mapper 接口与流式 DSL（EzQuery / EzUpdate / EzDelete 等），并兼容常用 JPA
注解，显著降低手写 XML 与重复 Mapper 的工作量，让你更专注业务逻辑开发。

---

## 💡 为什么选择 Ez-MyBatis

- 你厌倦了维护大量 XML 映射？
- 想在 **MyBatis + 多数据库** 项目中有统一上层抽象？
- 需要 **类型安全查询构建器** 避免字段写错？
- 原生 JOIN 支持，无需手写复杂 SQL, Ez-MyBatis 提供了完整的连表查询 DSL，可无缝替代原生 SQL 的 JOIN 写法，支持：INNER JOIN /
  LEFT JOIN / RIGHT JOIN 等常用连接方式
- 既要轻量，又希望在大批量场景里用 **JDBC 极致性能**？

Ez-MyBatis 正是为此而生：在不破坏原生态的前提下增强 MyBatis，可按需渐进接入，低成本迁移、低学习曲线。

---

## ✨ 特性

- 🔧 **零侵入**：基于拦截器，无需改动现有 MyBatis 配置或 Mapper 接口即可逐步接入。
- 📊 **多数据库支持**：MySQL、Oracle、达梦、PostgreSQL、SQL Server；亦可通过兼容配置支持其他国产数据库。
- 🏷️ **JPA 注解支持**：@Table、@Id、@Column、@Transient、@TypeHandler 等常用注解开箱即用。
- ⚡ **高性能通道**：提供 JDBC 直接操作 DAO（如 `JdbcInsertDao`、`JdbcUpdateDao`），大批量导入/更新更高效。
- 🔌 **事件机制**：插入、更新、删除、查询、SQL 构建等生命周期点均可注入自定义逻辑（如加解密、审计、埋点）。
- 🎯 **类型安全 DSL**：所有字段都有常量（如 `User.Fields.age`），IDE 自动提示，不怕写错列名。
- 🧪 **可混用**：Ez-MyBatis DSL 可与原生 MyBatis / MyBatis-Plus 共存，便于渐进式迁移。

---

## 📋 支持的数据库

| 数据库        | 支持状态 | 备注          |
|------------|------|-------------|
| MySQL      | ✅    | 完全支持        |
| Oracle     | ✅    | 完全支持        |
| 达梦         | ✅    | 完全支持        |
| PostgreSQL | ✅    | 完全支持        |
| SQL Server | ✅    | 完全支持        |
| 其他国产数据库    | ⚠️   | 需配置兼容的数据库类型 |

Ez-MyBatis 会自动根据数据源的 Driver Class 识别数据库类型（支持 MySQL, Oracle, PostgreSql, SqlServer, 达梦等常用驱动），通常情况下
**无需手动配置** `db-type`。

对于无法自动识别的国产数据库（如高斯数据库），可配置为兼容的数据库类型（示例：映射为 PostgreSQL）：

### 数据库类型配置

```yaml
ez-mybatis:
  db-type: POSTGRE_SQL  # 指强制指定数据库类型
```

---

## 🛠️ 快速开始

> 下列依赖片段仅示例，请根据自身版本与 BOM 统一管理。

### Spring Boot 2.x 集成 MyBatis

```xml

<dependencys>
    <dependency>
        <groupId>org.rdlinux</groupId>
        <artifactId>ez-mybatis-spring-boot-starter</artifactId>
        <version>1.0.3.RS</version>
    </dependency>
</dependencys>
```

### Spring Boot 2.x 集成 MyBatis-Plus

```xml

<dependencys>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.12</version>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-jsqlparser-4.9</artifactId>
        <version>3.5.12</version>
    </dependency>
    <dependency>
        <groupId>org.rdlinux</groupId>
        <artifactId>ez-mybatis-to-plus-spring-boot-starter</artifactId>
        <version>1.0.3.RS</version>
    </dependency>
</dependencys>
```

### Spring Boot 3.x 集成 MyBatis

```xml

<dependencies>
    <dependency>
        <groupId>org.rdlinux</groupId>
        <artifactId>ez-mybatis-spring3-boot-starter</artifactId>
        <version>1.0.3.RS</version>
    </dependency>
</dependencys>
```

### Spring Boot 3.x 集成 MyBatis-Plus

```xml

<dependencys>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-jsqlparser-4.9</artifactId>
        <version>3.5.12</version>
    </dependency>
    <dependency>
        <groupId>org.rdlinux</groupId>
        <artifactId>ez-mybatis-to-plus-spring3-boot-starter</artifactId>
        <version>1.0.3.RS</version>
    </dependency>
</dependencys>
```

---

## 📝 实体类定义

> 推荐使用 Lombok（`@Getter @Setter @FieldNameConstants`）自动生成字段常量，便于类型安全 DSL 构建。
>
> **💡 提示**：如果不方便使用 Lombok，或者更偏好 Lambda 风格，**Ez-MyBatis** 提供了 `FnField` 工具类，支持通过 Getter
> 方法引用获取字段名，同样能实现类型安全：
>
> ```java
> // 替代 User.Fields.name
> String nameField = FnField.of(User::getName);
> 
> // 在 DSL 中使用
> .addFieldCondition(FnField.of(User::getName), "张三")
> ```

### 基础实体

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
```

### 用户实体示例

```java

@Table(name = "ez_user", schema = "ez_mybatis")  // 指定表名和模式
@Getter
@Setter
@FieldNameConstants
public class User extends BaseEntity {
    @TypeHandler(StringTypeHandler.class)  // 指定类型处理器
    private String name;

    private Sex sex;

    @Column(name = "age")  // 指定数据库列名
    private Integer userAge;

    @Transient  // 忽略该字段
    private String ignore;

    public enum Sex {
        WOMAN, MAN
    }
}
```

---

## 💾 数据操作（CRUD）

本节展示 Ez-MyBatis 在 **插入 / 更新 / 删除** 方面的用法；每种操作均支持 Mapper 方式与 JDBC 高性能方式。

### 插入数据

```java

@Service
public class UserService {
    @Resource
    private EzMapper ezMapper;

    @Resource
    private JdbcInsertDao jdbcInsertDao;

    public void saveUser() {
        User user = new User();
        user.setName("张三");
        user.setSex(User.Sex.MAN);
        user.setUserAge(25);

        // 方式一：使用 Mapper（适合少量数据）
        ezMapper.insert(user);

        // 方式二：使用 JDBC（高性能，适合大批量数据）
        jdbcInsertDao.insert(user);
    }

    // 方式三：INSERT INTO ... SELECT ...
    public void insertByQuery() {
        // 将查询结果插入到 User 对应的表中
        EzQuery<User> query = EzQuery.builder(User.class)
                .from(EntityTable.of(UserDump.class)) // 从备份表查询
                .select().addAll().done()
                .build();

        ezMapper.insertByQuery(EntityTable.of(User.class), query);
    }
}
```

---

### 更新数据

#### 仅更新非空字段

```java
public void updateUser() {
    User user = new User();
    user.setId("016cdcdd76f94879ab3d24850514812b");
    user.setName("李四");
    user.setUserAge(30);

    // 只更新设置了值的字段
    ezMapper.update(user);
    // 或者使用 JDBC（高性能）
    jdbcUpdateDao.update(user);
}
```

#### 更新所有字段（replace）

```java
public void replaceUser() {
    User user = new User();
    user.setId("016cdcdd76f94879ab3d24850514812b");
    user.setName("王五");
    user.setUserAge(35);

    // 更新所有字段（包括 null 值）
    ezMapper.replace(user);
    // 或者使用 JDBC（高性能）
    jdbcUpdateDao.replace(user);
}
```

#### 条件更新

```java
public void conditionalUpdate() {
    EntityTable table = EntityTable.of(User.class);
    EzUpdate ezUpdate = EzUpdate.update(table)
            .set()
            // 设置年龄为 1
            .add(table.field(User.Fields.age).set(1))
            // 设置年龄为原值 (通过列名引用)
            .add(table.field(User.Fields.age).set(TableColumn.of(table, "age")))
            // 设置年龄为原值 (通过实体字段引用)
            .add(table.field(User.Fields.age).set(table.field(User.Fields.age)))
            // 年龄加 1
            .add(table.field(User.Fields.age).set(
                    Formula.builder(table.field(User.Fields.age)).add(1).done().build()))
            // 通过列名设置
            .add(table.column("name").set("张三"))
            // 将字段设置为 NULL
            .add(table.field(User.Fields.name).setToNull())
            .done()
            .where()
            // 条件：ID = 1
            .addCondition(table.field(BaseEntity.Fields.id).eq("1"))
            .done()
            .build();
    ezMapper.ezUpdate(ezUpdate);
}
```

> **💡 提示**：`EntityField.set()` 和 `TableColumn.set()` 是新增的 fluent API，可以构建更新项并直接通过 `add()` 添加到 SET
> 子句中。
> 旧的 `setField()` / `setColumn()` 方法仍然可用，保持向后兼容。

#### 表达式更新（Formula）

```java
public void formulaUpdate() {
    EntityTable table = EntityTable.of(User.class);

    // 创建表达式：年龄 + 10
    Formula formula = Formula.builder(table.field(User.Fields.age))
            .add(10)
            .done()
            .build();

    EzUpdate ezUpdate = EzUpdate.update(table)
            .set()
            .add(table.field(User.Fields.age).set(formula))  // 年龄增加 10
            .done()
            .where()
            .addCondition(table.field(BaseEntity.Fields.id).eq("1"))
            .done()
            .build();

    ezMapper.ezUpdate(ezUpdate);
}
```

#### 函数更新（Function）

```java
public void functionUpdate() {
    EntityTable table = EntityTable.of(User.class);

    // GREATEST 函数：取最大值
    Function ageFunction = Function.builder("GREATEST")
            .addArg(table.field(User.Fields.age))
            .addArg(100)
            .build();

    EzUpdate ezUpdate = EzUpdate.update(table)
            .set()
            .add(table.field(User.Fields.age).set(ageFunction))  // 年龄取最大值
            .done()
            .where()
            .addCondition(table.field(BaseEntity.Fields.id).eq("1"))
            .done()
            .build();

    ezMapper.ezUpdate(ezUpdate);
}
```

#### CASE WHEN 更新

```java
public void caseWhenUpdate() {
    EntityTable table = EntityTable.of(User.class);

    // 假设有函数
    Function someFunction = Function.builder("UPPER")
            .addArg(table.field(User.Fields.name))
            .build();

    // 嵌套的 CASE WHEN
    CaseWhen nestedCaseWhen = CaseWhen.builder(table)
            .when().addCondition(table.field(User.Fields.name).eq("张三1")).then("李四")
            .els("王二1");

    // 主 CASE WHEN
    CaseWhen caseWhen = CaseWhen.builder(table)
            .when().addCondition(table.field(User.Fields.name).eq("张三1")).then("李四")
            .when().addCondition(table.field(User.Fields.name).eq("张三2")).then(someFunction)
            .when().addCondition(table.field(User.Fields.name).eq("王二2")).then(nestedCaseWhen)
            .els("默认值");

    EzUpdate ezUpdate = EzUpdate.update(table)
            .set()
            .add(table.field(User.Fields.name).set(caseWhen))
            .done()
            .where()
            .addCondition(table.field(BaseEntity.Fields.id).in(Arrays.asList("1", "2", "3", "4")))
            .done()
            .build();

    ezMapper.ezUpdate(ezUpdate);
}
```

---

### 删除数据

#### 根据实体删除

```java
public void deleteUser() {
    User user = new User();
    user.setId("016cdcdd76f94879ab3d24850514812b");
    ezMapper.delete(user);
}
```

#### 根据ID删除（单条/批量）

```java
// 单条删除
public void deleteById() {
    ezMapper.deleteById(User.class, "016cdcdd76f94879ab3d24850514812b");
}

// 批量删除
public void batchDeleteById() {
    List<String> userIds = Arrays.asList("id1", "id2", "id3");
    ezMapper.batchDeleteById(User.class, userIds);
}
```

#### 条件删除

```java
public void conditionalDelete() {
    EntityTable userTable = EntityTable.of(User.class);

    EzDelete delete = EzDelete.delete(userTable)
            .where()
            .addCondition(userTable.field(User.Fields.name).eq("张三"))
            .groupCondition()  // 条件分组
            .addCondition(userTable.field(User.Fields.age).eq(55))
            .orCondition(userTable.field(User.Fields.age).eq(78))  // OR 条件
            .done()
            .done()
            .build();

    // 等价于 SQL: WHERE name = '张三' AND (age = 55 OR age = 78)
    ezMapper.ezDelete(delete);
}
```

---

## 🔍 查询数据

### 基础查询（按 ID / 批量 ID）

```java
// 根据ID查询单条
User user = ezMapper.selectById(User.class, "04b7abcf2c454e56b1bc85f6599e19a5");

// 根据ID批量查询
List<String> ids = Arrays.asList("id1", "id2", "id3");
List<User> users = ezMapper.selectByIds(User.class, ids);
```

---

### 指定查询字段

```java
public void selectSpecificFields() {
    EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
            .from(EntityTable.of(User.class))
            .select()
            .addField(User.Fields.age)        // 查询年龄字段
            .addField(User.Fields.name)           // 查询姓名字段
            .add("二三班", "class")           // 添加常量值
            .add(123.12, "balance")          // 添加数值常量
            .done()
            .build();

    List<StringHashMap> result = ezMapper.query(query);
}
```

> `StringHashMap` 为示例类型，可替换为 `Map<String, Object>` 或项目内统一 DTO。

---

### 分页查询

```java
public void pageQuery() {
    EzQuery<User> query = EzQuery.builder(User.class)
            .from(EntityTable.of(User.class))
            .select()
            .addAll()  // 查询所有字段
            .done()
            .page(1, 10)   // 第1页，每页10条
            .build();

    List<User> users = ezMapper.query(query);
}
```

---

### 分组聚合查询

```java
public void groupByQuery() {
    EntityTable table = EntityTable.of(User.class);

    // COUNT(*) 函数
    Function countFunc = Function.builder("COUNT")
            .addArg(org.rdlinux.ezmybatis.core.sqlstruct.Keywords.of("*"))
            .build();

    EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class)
            .from(table)
            .select()
            .addField(User.Fields.age)     // 分组字段
            .addField(User.Fields.name)        // 分组字段
            .add(countFunc, "total")           // 聚合函数
            .done()
            .groupBy()
            .addField(User.Fields.age)     // 按年龄分组
            .addField(User.Fields.name)        // 按姓名分组
            .done()
            .having()
            .addCondition(countFunc.gt(1))     // HAVING count > 1
            .done()
            .build();

    List<StringHashMap> result = ezMapper.query(query);
}
```

---

### 排序查询

```java
public void orderByQuery() {
    EzQuery<User> query = EzQuery.builder(User.class)
            .from(EntityTable.of(User.class))
            .select()
            .addAll()
            .done()
            .orderBy()
            .addField(User.Fields.age)                    // 按年龄升序
            .addField(User.Fields.name, OrderType.DESC)       // 按姓名降序
            .done()
            .page(1, 10)
            .build();

    List<User> users = ezMapper.query(query);
}
```

---

### 条件查询

```java
public void conditionalQuery() {
    EntityTable userTable = EntityTable.of(User.class);
    EzQuery<User> query = EzQuery.builder(User.class)
            .from(userTable)
            .select()
            .addAll()
            .done()
            .where()
            // NOT IN 条件
            .addCondition(userTable.field(User.Fields.name).notIn("张三"))
            .addCondition(userTable.field(User.Fields.name).notIn(Arrays.asList("李四", "王五")))
            // 其他条件
            .addCondition(userTable.field(User.Fields.age).gt(18))  // 年龄 > 18
            .done()
            .page(1, 10)
            .build();

    List<User> users = ezMapper.query(query);
}
```

---

### 连表查询

```java
public void joinQuery() {
    EntityTable userTable = EntityTable.of(User.class);
    EntityTable userOrgTable = EntityTable.of(UserOrg.class);

    EzQuery<User> query = EzQuery.builder(User.class)
            .from(userTable)
            .select()
            .addAll()                                        // 查询用户表所有字段
            .done()
            .select(userOrgTable)                               // 切换到关联表
            .addField(UserOrg.Fields.orgId)                 // 查询组织ID
            .done()
            .join(userOrgTable)                                 // INNER JOIN
            .addCondition(userTable.field(BaseEntity.Fields.id).eq(userOrgTable.field(UserOrg.Fields.userId)))  // ON 条件
            .done()
            .where()
            .addCondition(userTable.field(User.Fields.name).eq("张三"))     // 主表条件
            .addCondition(userOrgTable.field(UserOrg.Fields.orgId).eq("2")) // 关联表条件
            .addCondition(userTable.field(User.Fields.age).eq(22))
            .done()
            .page(1, 10)
            .build();

    List<User> users = ezMapper.query(query);
}
```

---

## 🚀 进阶用法

### 🔄 动态表名操作

对于分库分表或动态表名场景，Ez-MyBatis 支持在运行时指定表名。所有 `*ByTable` 结尾的方法均支持动态表名。

#### 动态表名插入/更新/删除

```java
public void dynamicTableOps() {
    // 1. 定义动态表（表名为 ez_user_2023）
    EntityTable dynamicTable = EntityTable.of(User.class, "ez_user_2023");

    User user = new User();
    user.setId("1");
    user.setName("Dynamic");

    // 2. 插入到指定表
    ezMapper.insertByTable(dynamicTable, user);

    // 3. 更新指定表
    ezMapper.updateByTable(dynamicTable, user);

    // 4. 删除指定表数据
    ezMapper.deleteByTable(dynamicTable, user);
}
```

#### 动态表名查询

```java
public void dynamicQuery() {
    EntityTable table = EntityTable.of(User.class, "ez_user_2023");
    EzQuery<User> query = EzQuery.builder(User.class)
            .from(table) // 指定 FROM 表名
            .select().addAll().done()
            .where()
            .addCondition(table.field(User.Fields.age).gt(18))
            .done()
            .build();

    List<User> users = ezMapper.query(query);
}
```

### 🔗 联合查询 (UNION)

支持标准 SQL 的 `UNION` 和 `UNION ALL` 操作。

```java
public void unionQuery() {
    EntityTable table = EntityTable.of(User.class);
    // 子查询 1
    EzQuery<User> query1 = EzQuery.builder(User.class)
            .from(table)
            .select().addAll().done()
            .where().addCondition(table.field(User.Fields.age).gt(18)).done()
            .build();

    // 子查询 2
    EzQuery<User> query2 = EzQuery.builder(User.class)
            .from(table)
            .select().addAll().done()
            .where().addCondition(table.field(User.Fields.name).like("admin%")).done()
            .build();

    // 构建 UNION 查询
    EzQuery<User> unionQuery = EzQuery.builder(User.class)
            .from(table)                        // 主结构
            .select().addAll().done()           // 主 Select
            .union(query1)                      // UNION
            .unionAll(query2)                   // UNION ALL
            .build();

    List<User> result = ezMapper.query(unionQuery);
}
```

### 🧩 原生 SQL 支持

虽然 DSL 能覆盖绝大部分场景，但 Ez-MyBatis 依然保留了执行原生 SQL 的能力，并支持自动映射。

```java
public void rawSqlOps() {
    String sql = "SELECT * FROM ez_user WHERE age > #{age}";
    Map<String, Object> params = new HashMap<>();
    params.put("age", 18);

    // 1. 查询返回 Map
    List<Map<String, Object>> mapList = ezMapper.selectMapBySql(sql, params);

    // 2. 查询返回实体对象
    List<User> users = ezMapper.selectObjectBySql(User.class, sql, params);

    // 3. 执行更新 / 删除 SQL
    ezMapper.updateBySql("UPDATE ez_user SET age = 20 WHERE id = '1'", null);
}
```

---

## 🔔 事件监听扩展点

Ez-MyBatis 在数据操作生命周期提供多个监听接口，方便做 **审计、埋点、加解密、缓存、数据脱敏** 等逻辑扩展。只需实现接口并注册为
Spring Bean 即可生效。

> `order()` 用于控制多个监听器执行顺序；数值越小越先执行（若框架约定不同，请以实际实现为准）。

### 插入事件监听

```java

@Component
public class UserInsertListener implements EzMybatisInsertListener {

    @Override
    public void onInsert(Object model) {
        System.out.println("插入单条数据: " + model);
    }

    @Override
    public void onBatchInsert(Collection<?> models) {
        System.out.println("批量插入数据: " + models.size() + " 条");
    }

    @Override
    public int order() {
        return 100;  // 执行顺序
    }
}
```

### 更新事件监听

```java

@Component
public class UserUpdateListener implements EzMybatisUpdateListener {

    @Override
    public void onUpdate(Object entity) {
        System.out.println("更新数据: " + entity);
    }

    @Override
    public void onBatchUpdate(Collection<Object> models) {
        System.out.println("批量更新数据: " + models.size() + " 条");
    }

    @Override
    public void onEzUpdate(EzUpdate ezUpdate) {
        System.out.println("条件更新: " + ezUpdate);
    }

    @Override
    public int order() {
        return 100;
    }
}
```

### 删除事件监听

```java

@Component
public class UserDeleteListener implements EzMybatisDeleteListener {

    @Override
    public void onDelete(Object entity) {
        System.out.println("删除数据: " + entity);
    }

    @Override
    public void onDeleteById(Object id, Class<?> entityClass) {
        System.out.println("根据ID删除: " + id + ", 实体类: " + entityClass.getSimpleName());
    }

    @Override
    public int order() {
        return 100;
    }
}
```

### 查询结果处理监听

可用于 **数据解密、格式转换、脱敏** 等后处理：

```java

@Component
public class QueryResultListener implements EzMybatisQueryRetListener {

    @Override
    public <T> T onBuildDone(T model) {
        // 单条记录处理，如解密敏感字段
        if (model instanceof User) {
            User user = (User) model;
            // 解密用户姓名等操作
            user.setName(decrypt(user.getName()));
        }
        return model;
    }

    @Override
    public void onBatchBuildDone(List<Object> models) {
        // 批量记录处理完成后的操作
        System.out.println("批量查询完成，共 " + models.size() + " 条记录");
    }

    @Override
    public int order() {
        return 100;
    }

    private String decrypt(String encryptedText) {
        // 解密逻辑
        return encryptedText;
    }
}
```

### SQL 构建字段值处理监听

在 SQL 构建阶段对字段值做预处理（如 **加密、脱敏、格式化、租户注入**）：

```java

@Component
public class SqlBuildListener implements EzMybatisOnBuildSqlGetFieldListener {

    @Override
    public Object onGet(boolean isSimple, Class<?> entityType, Field field, Object value) {
        // 在构建 SQL 时对字段值进行处理
        if (field.getName().equals("name") && value instanceof String) {
            // 加密姓名字段
            return encrypt((String) value);
        }
        return value;
    }

    @Override
    public int order() {
        return 100;
    }

    private String encrypt(String plainText) {
        // 加密逻辑
        return plainText;
    }
}
```

---

## 📖 注解说明

| 注解             | 说明      | 示例                                                |
|----------------|---------|---------------------------------------------------|
| `@Table`       | 指定表名和模式 | `@Table(name = "user_table", schema = "app")`     |
| `@Id`          | 标识主键字段  | `@Id private String id;`                          |
| `@Column`      | 指定列名    | `@Column(name = "user_age") private Integer age;` |
| `@Transient`   | 忽略字段    | `@Transient private String temp;`                 |
| `@TypeHandler` | 指定类型处理器 | `@TypeHandler(StringTypeHandler.class)`           |

> **小贴士**：当数据库列名与字段名不一致时务必使用 `@Column`；当需要在字段层面接入自定义类型转换时使用 `@TypeHandler`。

---

## ❓ 常见问题 FAQ

### Q1: 如何处理复杂的查询条件（嵌套 AND / OR）？

使用 `groupCondition()` 创建条件分组，构建任意嵌套逻辑：

```java
EntityTable table = EntityTable.of(User.class);
EzQuery<User> query = EzQuery.builder(User.class)
        .from(table)
        .where()
        .addCondition(table.field(User.Fields.name).eq("张三"))
        .groupCondition()  // (age > 20 OR age < 60)
        .addCondition(table.field(User.Fields.age).gt(20))
        .orCondition(table.field(User.Fields.age).lt(60))
        .done()
        .done()
        .build();
```

### Q2: Mapper 和 JDBC 方式如何选择？

| 场景           | 推荐方式     | 理由                   |
|--------------|----------|----------------------|
| 少量数据、简单 CRUD | Mapper   | 代码简洁、集成方便            |
| 大批量导入/批量更新   | JDBC DAO | 性能优、减少 ORM 开销        |
| 渐进迁移         | 两者并行     | 可逐步从老 Mapper 迁移到 DSL |

### Q3: 如何实现字段加密/解密？

- **写入前加密**：实现 `EzMybatisOnBuildSqlGetFieldListener`，在构建 SQL 时对字段值加密。
- **查询后解密**：实现 `EzMybatisQueryRetListener`，在对象构建完成后解密。

### Q4: 如何扩展新数据库类型？

若数据库协议兼容（如国产数据库对 PostgreSQL 或 MySQL 高兼容），可先通过 `db-type` 指定兼容方；如需完全适配，可提交 Issue 或
PR。

---

## 📄 许可证

本项目基于 **Apache License 2.0** 开源，可自由用于商业与非商业用途，须保留版权与许可证声明。

---

## 🤝 贡献

欢迎提交 Issue、Pull Request 帮助改进项目！

- 修复 bug
- 扩展数据库方言
- 增补文档、示例、测试用例

> 💡 **提示**: 更多详细的 API 文档和使用示例，请访问项目官方文档（或 Wiki / 示例仓库）。

---

谢谢使用 **Ez-MyBatis**！如果它对你有帮助，欢迎点个 ⭐ Star 支持一下！

