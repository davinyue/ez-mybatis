# 简介：

该项目基于mybatis封装, 目前只支持mysql, 旨在提供一个对dao层通用的操作，支持普通java程序和spring程序.

# 特性：
1. 关键字转义，防止sql注入；
2. 注解支持；
3. 查询, 普通条件查询, 连表查询, 懒加载;
4. 更新, 替换更新和非空字段更新;
5. 插入, 单条插入, 批量插入，指定枚举处理, 时间处理, boolean处理;
6. 删除, 根据主键删除

# 使用
## maven依赖
```
<dependency>
	<groupId>org.linuxprobe</groupId>
	<artifactId>mybatis-universal-crud</artifactId>
	<version>2.1.1.RELEASE</version>
</dependency>
```
## 和spring集成
1. spring xml配置文件加入
````
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd">
    <aop:aspectj-autoproxy poxy-target-class="true"/>
    <!-- datasource的配置省略 -->
    <bean id="datasource" class="com.zaxxer.hikari.HikariDataSource"/>
    <bean name="sqlSessionFactory" class="org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionFactoryBean">
        <property name="dataSource" ref="datasource"/>
        <property name="universalCrudScan" value="org.linuxprobe.crud.demo.model;org.linuxprobe.crud.demo.query"/>
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <!-- 扫描sql配置文件:mapper需要的xml文件 -->
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>
    </bean>
</beans>
```

[项目地址请点击](https://github.com/linuxprobe-org/java-project-demo)

[代码实现请点击](https://github.com/linuxprobe-org/java-project-demo/blob/master/src/test/java/org/linuxprobe/demo/MybatisTest.java)

