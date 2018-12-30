# README

## 简介：

该项目旨在提供一个对dao层通用的操作，支持普通java程序和spring程序

插入支持：支持指定主键生成模式，uuid，自增，自指定；支持枚举指定使用名称还是序列。

删除支持：直接删除实体支持，主键删除支持。

更新支持：全替换更新支持，非空字段更新支持。

查询支持：连接查询，自定义条件查询，自定义sql查询，主键count查询，ps:未来将支持懒加载。

## 特性：

1. 关键字转义，防止sql注入；
2. 注解支持，对原有代码改动小；
3. 已经可以完成大部分的crud操作，但如统计类需求需要一次加载多表数据的，为了提高性能，用户可以使用自己的mybatis实现。
4. 支持深层次的join查询，支持排序，用户可指定where条件操作符，where条件链接符\(and,or\)，由于分页的原因暂不支持实体的关联加载，后续将支持懒加载。
5. 实体字段与数据库字段的映射默认是驼峰转下划线，可通过注解指定数据库的列名。

## 配置：

### 1.pom依赖引入

```text
<dependency>
    <groupId>org.linuxprobe</groupId>
    <artifactId>mybatis-universal-crud</artifactId>
    <version>2.0.7.RELEASE</version>
</dependency>
```

### 2.在普通java程序中使用

#### 首先我们需要像使用原生的mybatis一样，配置mybatis-config.xml文件

特别注意以下标签的配置，它是该项目特有的，用于扫描实体信息和搜索类信息，多个包使用","隔开

```text
<setting name="universalCrudScan" value="org.linuxprobe.demo.model"></setting>
```

```text
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!-- 引入外部资源文件 -->
	<!-- <properties resource="jdbc.properties"></properties> -->
	<settings>
		<!--开启驼峰命名 -->
		<setting name="mapUnderscoreToCamelCase" value="true" />
		<setting name="jdbcTypeForNull" value="NULL"></setting>
		<setting name="lazyLoadingEnabled" value="true"></setting>
		<setting name="aggressiveLazyLoading" value="false"></setting>
		<!--开启二级缓存 -->
		<setting name="cacheEnabled" value="true"></setting>
		<!-- 指定mybatis-universal-crud-simple的扫描路径，主要用于扫描实体信息和搜索类信息，多个包使用","隔开 -->
		<setting name="universalCrudScan" value="org.linuxprobe.demo.model"></setting>
	</settings>
	<!-- typeAliases:起别名的标签 typeAlias:为某个java类型起别名 type:指定要起别名的全类名，默认别名就是类小写，(但是其实他是不区分大小写的，也就是说你全部大写也没问题) 
		alias:新的别名 package:为某个包下的类批量起别名(这个会存在的一个问题就是可能会重复，重复的情况下还可以用@Alias来解决) name:指定包名(这个就相当于是为这个包下的所有的类都创建了一个默认的别名) 
		当然还有一个起别名的方式就是在实体上使用@Alias来起别名 注：起别名看自己的情况，推荐还是在mapper文件中使用全类名 -->
	<typeAliases>
		<!-- <typeAlias type="com.wj.mybatis.bean.Employee" alias="emp"></typeAlias> 
			<package name="com.wj.mybatis.bean"></package> -->
	</typeAliases>
	<!-- 配置环境：可以配置多个环境，default：配置某一个环境的唯一标识，表示默认使用哪个环境 -->
	<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<!-- 配置连接信息 -->
				<property name="driver" value="com.mysql.jdbc.Driver" />
				<property name="url"
					value="jdbc:mysql://www.linuxprobe.org:3306/crud_test?useUnicode=true&amp;characterEncoding=UTF-8&amp;useSSL=false" />
				<property name="username" value="temp" />
				<property name="password" value="123456" />
			</dataSource>
		</environment>
	</environments>
	<!-- 配置映射文件：用来配置sql语句和结果集类型等 -->
	<mappers>
		<!-- <mapper class="org.linuxprobe.crud.mapper.UniversalMapper" /> -->
	</mappers>
</configuration>
```

#### 然后配置sqlSessionFactoryBuilder，并根据Builder获得SqlSessionFactory，最后通过SqlSessionFactory获得SqlSession来对数据库执行操作。

```text
#头部import
import org.apache.ibatis.io.Resources;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSession;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSessionFactory;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSessionFactoryBuilder;

#具体代码实现
UniversalCrudSqlSessionFactory sqlSessionFactory = null;
String resource = "mybatis-config.xml";
Reader reader = null;
try {
    reader = Resources.getResourceAsReader(resource);
} catch (IOException e) {
    e.printStackTrace();
}
UniversalCrudSqlSessionFactoryBuilder sqlSessionFactoryBuilder = new UniversalCrudSqlSessionFactoryBuilder();
sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);
UniversalCrudSqlSession sqlSession = sqlSessionFactory.openSession();
```

#### 使用示例

[项目地址请点击](https://github.com/linuxprobe-org/java-project-demo)

[代码实现请点击](https://github.com/linuxprobe-org/java-project-demo/blob/master/src/test/java/org/linuxprobe/demo/MybatisTest.java)

