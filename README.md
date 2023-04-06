# 说明
ez-mybatis通过mybatis拦截器机制, 实现数据的增删查改并支持部分JPA注解, 在不修改mybatis源码的前提下, 提供通用且易用的mapper, 以帮助开发者减少对数据库访问的开发, 专注于业务代码开发

# spring boot使用
```
<dependency>
    <groupId>org.rdlinux</groupId>
    <artifactId>ez-mybatis-spring-boot-start</artifactId>
    <version>0.4.2.RS</version>
</dependency>
```

# 实体标注
```java
package org.rdlinux.ezmybatis.java.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.apache.ibatis.type.StringTypeHandler;
import org.rdlinux.ezmybatis.annotation.ColumnHandler;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/** 标记为实体以及指定表明和schema */
@Entity
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
