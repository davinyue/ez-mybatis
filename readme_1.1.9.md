# README\_1.1.9

[1.1.8之前的版本请点击这里查看文档](https://github.com/linuxprobe-org/mybatis-universal-crud-simple/blob/master/README_1.7.md)

fix: 修复sql生成类可能引起内存溢出的bug。

## 简介：

mybatis-universal-crud-simple基于java反射开发，选择运行于mybatis上是因为可以利用mybatis缓存功能，实际上完全可以稍作修改运行在jdbc上。

## 特性：

1. 对原有代码没有入侵性，只需在需要保存的类以及成员上打上相关注解即可,不会影响原有代码实现和逻辑。
2. 用户无需编写任何的mybatis映射文件，如有需求也可编写自己的映射文件。
3. 支持插入和批量插入。
4. 支持字段全更新和部分更新。
5. 支持删除和批量删除同一类型实体。
6. 支持深层次的left join查询，支持排序，用户可指定where条件操作符，where条件链接符\(and,or\)，同时处理了关键字符的转义，防止sql注入，但不支持实体的关联加载，如果有关联加载的需求，用户只需按照实例编写自己的映射文件，修改返回结果映射即可。
7. 实体字段与数据库字段的映射默认是驼峰转下划线，可通过注解指定数据库的列名。
8. 修改数据库表信息后，只需修改对应实体即可，无需修改映射文件。

## 配置：

### 1.在pom文件里面添加依赖，

```text
<dependency>
    <groupId>org.linuxprobe</groupId>
    <artifactId>mybatis-universal-crud-simple</artifactId>
    <version>1.1.8.RELEASE</version>
</dependency>
```

### 2.bean扫描配置

1\)Spring boot框架应在启动文件添加注解@ComponentScan\({"org.linuxprobe.\*\*"}\)和mybatis接口扫描@MapperScan\({ "org.linuxprobe.crud.mapper" }\)。

2\)spring框架应在配置文件里面添加包扫描。

```text
<context:component-scan
    base-package="你的包, org.linuxprobe.crud">
</context:component-scan>
```

3.配置mybatis

### 配置mybatis扫描/src/main/java/org/linuxprobe/crud/mapper/下的接口，如果是spring boot配置了@MapperScan\({ "org.linuxprobe.crud.mapper" }\)则不需要配置此项。

## 性能良好

根据java反射生成sql，生成的sql让mybatis去执行，并无多余操作。

## 方便快捷

普通的增删查改完全够用，如过有特殊需求，用户自行往自己的映射文件添加自己的实现即可。用户的实体service可以继续mybatis-universal-crud-simple并覆盖里面的方法，根据需求重写方法。例如插入数据库前检测数据是否重复。

## 与jpa对比的优点

Jap可以通过一定的规则编写方法接口儿不用编写方法实现实现数据的查询，但条件过多时，方法名太长，不同的条件可能需要编写不同的方法。而mybatis-universal-crud-simple则可一个方法通吃。

## 使用教程

### [1.不继承BaseService的使用教程：](https://github.com/linuxprobe-org/crud-demo/blob/master/src/test/java/org/linuxprobe/demo/ApplicationTests.java)

### [2.继承BaseService，实现自己的扩展](https://github.com/linuxprobe-org/crud-demo)

### 1）实体是BaseModel的直接子类或间接子类，BaseModel类里面已经自带了一个String id字段。

```text
package org.linuxprobe.demo.model;

import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.Table;
import org.linuxprobe.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Table("org")
@Getter
@Setter
public class Org extends BaseModel{
    private String name;
}
```

### 2）实体的查询类是BaseQuery的直接子类或间接子类，BaseQuery类里面已经自带了一个StringParam id字段，继承BaseQuery还可以实现分页和排序。

```text
package org.linuxprobe.demo.query;

import org.linuxprobe.crud.core.annoatation.Search;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.param.impl.StringParam;
import org.linuxprobe.demo.model.Org;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Search(Org.class)
public class OrgQuery extends BaseQuery{
    private StringParam name;
}
```

### 3）Service继承BaseService

```text
package org.linuxprobe.demo.service;

import org.linuxprobe.crud.service.BaseService;
import org.linuxprobe.demo.model.Org;
import org.linuxprobe.demo.query.OrgQuery;

public interface OrgService extends BaseService<Org, OrgQuery>{

}
```

### 4）ServiceImpl继承BaseServiceImpl并实现Service接口

```text
package org.linuxprobe.demo.service.impl;

import org.linuxprobe.crud.service.impl.BaseServiceImpl;
import org.linuxprobe.demo.model.Org;
import org.linuxprobe.demo.query.OrgQuery;
import org.linuxprobe.demo.service.OrgService;
import org.springframework.stereotype.Service;

@Service
public class OrgServiceImpl extends BaseServiceImpl<Org, OrgQuery> implements OrgService{

}
```

### 5）Mapper继承BaseMapper接口

```text
package org.linuxprobe.demo.mapper;

import org.linuxprobe.crud.mapper.BaseMapper;
import org.linuxprobe.demo.model.Org;

public interface OrgMapper extends BaseMapper<Org>{

}
```

### 6）编写xml映射文件,下面的两个实现是必须的，它是BaseMapper里面未实现的接口

```text
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.linuxprobe.demo.mapper.OrgMapper">
    <resultMap id="BaseResultMap" type="org.linuxprobe.demo.model.Org">
        <id column="id" jdbcType="VARCHAR" property="id" />
        <result column="name" jdbcType="VARCHAR" property="name" />
    </resultMap>
    <select id="select"
        parameterType="org.linuxprobe.demo.query.OrgQuery"
        resultMap="BaseResultMap">
        ${sqlr.toSelectSql()}
    </select>

    <select id="selectByPrimaryKey" parameterType="java.lang.String"
        resultMap="BaseResultMap">
        select
            o.*
        from org as o
        where o.id = #{id,jdbcType=VARCHAR}
    </select>
</mapper>
```

