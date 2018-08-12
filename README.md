
简介：
==
mybatis-universal-crud-simple基于java反射开发，选择运行于mybatis上是因为可以利用mybatis缓存功能，实际上完全可以稍作修改运行在jdbc上。


特性：
===
1.	对原有代码没有入侵性，只需在需要保存的类以及成员上打上相关注解即可,不会影响原有代码实现和逻辑。
2.	用户无需编写任何的mybatis映射文件，如有需求也可编写自己的映射文件。
3.	支持插入和批量插入。
4.	支持字段全更新和部分更新。
5.	支持删除和批量删除同一类型实体。
6.	支持深层次的left join查询，支持排序，用户可指定where条件操作符，where条件链接符(and,or)，同时处理了关键字符的转义，防止sql注入，但不支持实体的关联加载，如果有关联加载的需求，用户只需按照实例编写自己的映射文件，修改返回结果映射即可。
7.	实体字段与数据库字段的映射默认是驼峰转下划线，可通过注解指定数据库的列名。
8.	修改数据库表信息后，只需修改对应实体即可，无需修改映射文件。


配置简单，只需三步。
===
1.	在pom文件里面添加依赖，
---
```
<dependency>
	<groupId>org.linuxprobe</groupId>
	<artifactId>mybatis-universal-crud-simple</artifactId>
	<version>1.1.4.RELEASE</version>
</dependency>
```
2.	bean扫描配置
---
spring框架应在配置文件里面添加包扫描
```
<context:component-scan
	base-package=" com.river.cruise.service, org.linuxprobe.crud">
</context:component-scan>
```
Spring boot框架应在启动文件添加注解@ComponentScan("org.linuxprobe.**")

3.	配置mybatis
将maven下载的依赖包mybatis-universal-crud-simple-1.1.4.RELEASE.jar解压把
/src/main/java/org/linuxprobe/crud/mapper/mapping/UniversalMapper.xml文件复制到你的项目的mapping映射文件所在目录，并配置mybatis扫描/src/main/java/org/linuxprobe/crud/mapper/下的接口。

性能良好
===
根据java反射生成sql，生成的sql让mybatis去执行，并无多余操作。

方便快捷
===
普通的增删查改完全够用，如过有特殊需求，用户自行往自己的映射文件添加自己的实现即可。用户的实体service可以继续mybatis-universal-crud-simple并覆盖里面的方法，根据需求重写方法。例如插入数据库前检测数据是否重复。

与jpa对比的优点
===
Jap可以通过规范编只写方法接口不编写方法实现实现数据的查询，但条件过多时，方法名太长，不同的条件可能需要编写不同的方法。而mybatis-universal-crud-simple则可一个方法通吃。
