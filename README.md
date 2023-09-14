# 说明
ez-mybatis通过mybatis拦截器机制, 实现数据的增删查改并支持部分JPA注解, 在不修改mybatis源码的前提下, 提供通用且易用的mapper, 以帮助开发者减少对数据库访问的开发, 专注于业务代码开发

# spring boot使用
```
<dependency>
    <groupId>org.rdlinux</groupId>
    <artifactId>ez-mybatis-spring-boot-start</artifactId>
    <version>0.5.4.RS</version>
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
这个列子稍微负责一些，他对前面提到的表达式以及函数进行了嵌套，在当前示例中, 将id在列表"1,2,3,4"的数据name字段根据casewhen进行更新, 当名字等于"张三1"时, 设置为"李四"； 当名字等于"张2"时, 设置为"function"函数的返回值"2"； 当名字等于"王二1"时, 设置为"formula"表达式的返回值"101"； 当名字等于"王二2"时, 设置为"sonCaseWhen"表达式的返回值"王二1"。
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