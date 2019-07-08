# 简介：

该项目基于mybatis封装, 目前只支持mysql, 旨在提供一个对dao层通用的操作，支持普通java程序和spring程序.

# 1 特性：
1. 关键字转义，防止sql注入；
2. 注解支持；
3. 查询, 普通条件查询, 连表查询, 懒加载;
4. 更新, 替换更新和非空字段更新;
5. 插入, 单条插入, 批量插入，指定枚举处理, 时间处理, boolean处理;
6. 删除, 根据主键删除；
7. 实体字段支持javax.validation验证。

# 2 使用
## 2.1 和spring集成
### 2.1.1 maven依赖
```
<dependency>
	<groupId>org.linuxprobe</groupId>
	<artifactId>mybatis-universal-crud</artifactId>
	<version>2.1.1.RELEASE</version>
</dependency>
```
### 2.1.2 spring xml配置文件加入

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd">
    <aop:aspectj-autoproxy poxy-target-class="true"></aop:aspectj>
    <!-- datasource的配置省略 -->
    <bean id="datasource" class="com.zaxxer.hikari.HikariDataSource"></bean>
    <bean name="sqlSessionFactory" class="org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionFactoryBean">
        <property name="dataSource" ref="datasource"></property>
        <property name="universalCrudScan" value="org.linuxprobe.crud.demo.model;org.linuxprobe.crud.demo.query"></property>
        <property name="configLocation" value="classpath:mybatis-config.xml"></property>
        <!-- 扫描sql配置文件:mapper需要的xml文件 -->
        <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
    </bean>
	<bean name="sqlSessionTemplaten" class="org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionTemplaten">
        <constructor-arg ref="sqlSessionFactory"></constructor>
    </bean>
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="org.linuxprobe.**.mapper"></property>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
    </bean>
</beans>
```


## 2.2 和spring boot集成
### 2.2.1 pom引入spring boot专用依赖
```
<dependency>
	<groupId>org.linuxprobe</groupId>
	<artifactId>
		mybatis-universal-crud-spring-boot-starter
	</artifactId>
	<version>2.1.0.RELEASE</version>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### 2.2.2 yml配置
```yaml
mybatis:
  mapperLocations: classpath*:org/linuxprobe/**/mapping/*.xml
  configLocation: classpath:/mybatis-config.xml
  #配置查询类所在包, 实体所在包, 可配置多个
  universalCrudScans:
    - org.linuxprobe.universalcrudspringbootdemo.query
      org.linuxprobe.universalcrudspringbootdemo.model
```

### 2.2.3 启用类添加注解
```
@EnableAspectJAutoProxy(exposeProxy = true)
/** 此处指定你的mapper接口所在包 */
@MapperScan(basePackages = "org.linuxprobe")
```


## 2.3 crud示例
### 2.3.1 新建实体类
urer实体
```java
package org.linuxprobe.universalcrudspringbootdemo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.linuxprobe.crud.core.annoatation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 使用@Entity标注一个实体;
 * 使用@Table指定对应的表名
 */
@Entity
@Table(value = "permission")
@Getter
@Setter
@Accessors(chain = true)
public class User {
    @PrimaryKey(PrimaryKey.Strategy.NATIVE)
    private Integer id;

    private String name;

    private List<Role> roles;
    /**
     * 使用@Column指定列明;
     * 使用@EnumHandler来指定枚举的保存方式
     */
    @Column("sex")
    @NotNull
    @EnumHandler(EnumHandler.EnumCustomerType.Ordinal)
    private Sex xingbie;

    public static enum Sex {
        Man,
        WoMan
    }
}

```
