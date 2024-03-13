# 说明
ez-mybatis通过mybatis拦截器机制, 实现数据的增删查改并支持部分JPA注解, 在不修改mybatis源码的前提下, 提供通用且易用的mapper, 以帮助开发者减少对数据库访问的开发, 专注于业务代码开发

# spring boot使用
```
<dependency>
    <groupId>org.rdlinux</groupId>
    <artifactId>ez-mybatis-spring-boot-start</artifactId>
    <version>0.7.6.RS</version>
</dependency>
```

# 实体标注
BaseEntity基础实体
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

User实体, 将继承父级的所有属性
```java
package org.rdlinux.ezmybatis.java.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.apache.ibatis.type.StringTypeHandler;
import org.rdlinux.ezmybatis.annotation.ColumnHandler;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/** 标记为实体以及指定表明和schema */
@Table(name = "ez_user", schema = "ez_mybatis")
@Getter
@Setter
@FieldNameConstants
public class User extends BaseEntity {
    /** 指定TypeHandler */
    @ColumnHandler(StringTypeHandler.class)
    private String name;
    private Sex sex;
    /** 指定列名为age */
    @Column(name = "age")
    private Integer userAge;
    /** 忽略本字段保存 */
    @Transient
    private String ignore;

    public static enum Sex {
        WOMAN,
        MAN
    }
}

```

# 保存实体
```java
@Resource
private EzMapper ezMapper;

User user = new User();
ezMapper.insert(user);
```

# 更新实体
## 不更新空字段
```java
@Resource
private EzMapper ezMapper;

User user = new User();
user.setId("016cdcdd76f94879ab3d24850514812b");
user.setName("王二");
user.setName("王");
user.setUserAge(27);
user.setSex(User.Sex.MAN);
this.ezMapper.update(user);
```

## 更新所有字段
```java
@Resource
private EzMapper ezMapper;

User user = new User();
user.setId("016cdcdd76f94879ab3d24850514812b");
user.setName("王二");
user.setName("王");
user.setUserAge(27);
user.setSex(User.Sex.MAN);
this.ezMapper.replace(user);
```

## 条件更新
在当前示例中, 可以在where构造器里面指定更多的条件对指定字段进行更新
```java
@Resource
private EzMapper ezMapper;

public void updateByEzParam() {
    EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class))
            .set()
            //设置userAge属性的值为1, 会自动转换为age列
            .setField(User.Fields.userAge, 1)
            //设置userAge属性的值为关键词age, 等价于sql set age = age
            .setFieldKeywords(User.Fields.userAge, "age")
            //设置age列的值为关键词age, 等价于sql set age = age
            .setColumnKeywords("age", "age")
            .done()
            .where()
            //当id等于1时更新
            .addFieldCondition("id", "1")
            .done()
            .build();
    this.mapper.ezUpdate(ezUpdate);
}
```

## 条件更新之设置某列的值为表达式的值
在当前示例中, 将id等于1的数据年龄更新为加10岁
```java
@Resource
private EzMapper ezMapper;

@Test
public void formulaUpdateTest() {
    EzMapper mapper = sqlSession.getMapper(EzMapper.class);
    EntityTable table = EntityTable.of(User.class);
    Formula formula = Formula.builder(table).withField(User.Fields.userAge).addValue(10).done().build();
    EzUpdate ezUpdate = EzUpdate.update(table)
            .set().setFieldFormula(User.Fields.userAge, formula).done()
            .where()
            .addFieldCondition(BaseEntity.Fields.id, "1").done()
            .build();
    this.mapper.ezUpdate(ezUpdate);
}
```

## 条件更新之设置某列的值为函数返回值
在当前示例中, 将id等于1的数据年龄使用GREATEST函数找到最大值并设置为最大值, 将更新时间设置为当前时间
```java
@Resource
private EzMapper ezMapper;

@Test
public void functionUpdateTest() {
    EntityTable table = EntityTable.of(User.class);
    Function function = Function.builder(table).setFunName("GREATEST").addFieldArg(User.Fields.userAge)
            .addValueArg(100).build();

    Function updateTimeFunction = Function.builder(table).setFunName("now").build();
    EzUpdate ezUpdate = EzUpdate.update(table)
            .set()
            .setFieldFunction(User.Fields.userAge, function)
            .setFieldFunction(BaseEntity.Fields.updateTime, updateTimeFunction)
            .done()
            .where()
            .addFieldCondition(BaseEntity.Fields.id, "1").done()
            .build();
    this.mapper.ezUpdate(ezUpdate);
}
```

## 条件更新之设置某列的值为casewhen表达式返回值
这个列子稍微复杂一些，它对前面提到的表达式以及函数进行了嵌套。在当前示例中, 将id在列表"1,2,3,4"的数据name字段根据casewhen进行更新, 当名字等于"张三1"时, 设置为"李四"； 当名字等于"张2"时, 设置为"function"函数的返回值"2"； 当名字等于"王二1"时, 设置为"formula"表达式的返回值"101"； 当名字等于"王二2"时, 设置为"sonCaseWhen"表达式的返回值"王二1"。
```java
@Resource
private EzMapper ezMapper;

@Test
public void functionUpdateTest() {
    EntityTable table = EntityTable.of(User.class);
    Formula formula = Formula.builder(table).withValue(1).addValue(100).done().build();
    Function function = Function.builder(table).setFunName("GREATEST").addValueArg(1).addValueArg(2).build();

    CaseWhen sonCaseWhen = CaseWhen.builder(table)
            .when()
            .addFieldCondition(User.Fields.name, "张三1").then("李四")
            .els("王二1");

    CaseWhen caseWhen = CaseWhen.builder(table)
            .when()
            .addFieldCondition(User.Fields.name, "张三1").then("李四")
            .when()
            .addFieldCondition(User.Fields.name, "张三2").thenFunc(function)
            .when()
            .addFieldCondition(User.Fields.name, "王二1").thenFormula(formula)
            .when()
            .addFieldCondition(User.Fields.name, "王二2").thenCaseWhen(sonCaseWhen)
            .els("王二1");

    EzUpdate ezUpdate = EzUpdate.update(table)
            .set().setField(User.Fields.name, caseWhen).done()
            .where()
            .addFieldCondition(BaseEntity.Fields.id, Operator.in, Arrays.asList("1", "2","3","4"))
            .done()
            .build();
    this.mapper.ezUpdate(ezUpdate);
}
```

# 删除实体
## 根据实体删除
```java
@Resource
private EzMapper ezMapper;

User user = new User();
user.setId("016cdcdd76f94879ab3d24850514812b");
this.ezMapper.delete(user);
```

## 根据id删除
```java
@Resource
private EzMapper ezMapper;

this.ezMapper.deleteById(User.class, "016cdcdd76f94879ab3d24850514812b");
```


## 根据id批量删除
```java
@Resource
private EzMapper ezMapper;

List<String> userIds = new LinkedList<>();
for (int i = 0; i < 2; i++) {
    userIds.add("016cdcdd76f94879ab3d24850514812b" + i);
}
this.ezMapper.batchDeleteById(User.class, userIds);
```

## 条件删除
该示例中, 我们将删除名字为"张三", 并且年龄为55或者78岁的数据, 等价于sql "where name = '张三' and (age = 55 or age = 78)";

对于or条件, 可以使用groupCondition将其作为一个条件组, 为其加上括号
```java
@Resource
private EzMapper ezMapper;

EntityTable userTable = EntityTable.of(User.class);
EzDelete delete = EzDelete.delete(userTable)
        .where()
        .addFieldCondition(User.Fields.name, "张三")
        .groupCondition()
        .addFieldCondition(User.Fields.userAge, 55)
        .addFieldCondition(AndOr.OR, User.Fields.userAge, 78)
        .done()
        .done()
        .build();
this.ezDelete(delete);
```

# 查询
## 根据id查询
```java
@Resource
private EzMapper ezMapper;

//单条查询
User user = this.ezMapper.selectById(User.class, "04b7abcf2c454e56b1bc85f6599e19a5");

//批量查询
List<String> ids = new LinkedList<>();
ids.add("04b7abcf2c454e56b1bc85f6599e19a5");
ids.add("085491774b2240688edb1b31772ff629");
List<User> users = this.ezMapper.selectByIds(User.class, ids);
```

## 高级查询

### 指定查询内容
在当前查询中, 只查询用户的age列, name列, "二三班"作为class列, 123.12作为balance列
```java
@Resource
private EzMapper ezMapper;

@Test
public void test() {
    EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(EntityTable.of(User.class))
            .select()
            .addField(User.Fields.userAge)
            .addField(User.Fields.name)
            .addValue("二三班", "class")
            .addValue(123.12, "balance")
            .done()
            .build();
    List<StringHashMap> users = this.ezMapper.query(query);
}
```

### 分页查询
在当前查询中, 查询user表所有列, 并且分页取第一页的5条数据
```java
@Resource
private EzMapper ezMapper;

@Test
public void test() {
    EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(EntityTable.of(User.class))
            .select()
            .addAll()
            .done()
            .page(1, 5)
            .build();
    List<StringHashMap> users = this.ezMapper.query(query);
}
```

### group查询
在当前查询中, 首先构建了一个count(*) 函数, 来查询分组后每组数据的总数；在指定查询列时, 为count(*)函数的结果指定别名为ct; group时, 根据age列和name列进行group， 并且having指定了分组后总数大于1的结果
```java
@Resource
private EzMapper ezMapper;

@Test
public void test() {
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
    List<StringHashMap> users = this.ezMapper.query(query);
}
```

### 排序查询
在当前查询中, 从user表查询结果，返回类型指定为User实体类, 同时分页，并根据age列和name列进行排序, 其中name列指定使用倒排序
```java
@Resource
private EzMapper ezMapper;

@Test
public void test() {
    EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
            .select()
            .addAll()
            .done()
            .orderBy()
            .addField(User.Fields.userAge)
            .addField(User.Fields.name, OrderType.DESC)
            .done()
            .page(1, 5)
            .build();
    List<User> users = this.ezMapper.query(query);
}
```

### 指定条件查询
在当前查询中, 从user表查询结果，返回类型指定为User实体类, 同时分页，并且条件为name不在指定值内
```java
@Resource
private EzMapper ezMapper;

@Test
public void test() {
    EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
            .select()
            .addAll()
            .done()
            .where()
            .addFieldCondition(User.Fields.name, Operator.notIn, "1")
            .addFieldCondition(User.Fields.name, Operator.notIn, Collections.singletonList("张三"))
            .addFieldCondition(User.Fields.name, Operator.notIn, Arrays.asList("李四", "王二"))
            .done()
            .page(1, 5)
            .build();
    List<User> users = this.ezMapper.query(query);
}
```

### 连表查询
在当前查询中, user表和user_org表进行inner join查询, on条件为user表的id等于user_org表的user_id, 对于查询结果集, select在默认情况下是取from表的结果, 如果select指定了表, 则从指定表过去结果; 这样可以实现select查询任何一个表的列；addFieldCompareCondition指定了两个表的关联条件, addFieldCondition(User.Fields.name,  "张三")指定了user表name必须等于"张三的数据才参与连接"; joinTableCondition将条件切换到被关联表，接着添加的条件是user_org表的org_id列必须等于2；masterTableCondition又将条件切换回了主表, 并指定user表的age列等于22。
```java
@Resource
private EzMapper ezMapper;

@Test
public void test() {
    EntityTable userOrgTable = EntityTable.of(UserOrg.class);
    EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
            .select()
            .addAll().done()
            .select(userOrgTable)
            .addField(UserOrg.Fields.orgId)
            .done()
            .join(userOrgTable)
            .addFieldCompareCondition(BaseEntity.Fields.id, UserOrg.Fields.userId)
            .addFieldCondition(User.Fields.name,  "张三")
            .joinTableCondition()
            .addFieldCondition(UserOrg.Fields.orgId, "2")
            .masterTableCondition()
            .addFieldCondition(User.Fields.userAge,  22)
            .done()
            .page(1, 5)
            .build();
    List<User> users = this.ezMapper.query(query);
}
```