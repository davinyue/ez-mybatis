---
description: 建立一个spring boot引用程序
---

# 在spring boot程序中使用

#### 在yml配置文件中添加以下配置

```text
spring:
  aop:
    auto: true
    proxy-target-class: true
mybatis:
  #指定Mybatis映射文件位置
  mapperLocations: classpath*:org/linuxprobe/**/mapping/*.xml 
  #指定Mybatis配置文件
  configLocation: classpath:/mybatis-config.xml  
  #指定实体和查询类所在包，请替换成自己的包路径
  universalCrudScan: org.linuxprobe.demo.model,org.linuxdpring.demo.query
```

#### 在入口类添加以下两个注解

```text
/** 指定mybatis接口扫描路径 */
@MapperScan("org.linuxprobe.demo.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
```

新建MybatisConfig.java配置文件

```text
package org.linuxprobe.demo;

import java.io.IOException;

import javax.sql.DataSource;

import org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionFactoryBean;
import org.linuxprobe.crud.mybatis.spring.UniversalCrudSqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties("mybatis")
@Getter
@Setter
public class MybatisConfig {
	private String mapperLocations;
	private String configLocation;
	private String universalCrudScan;

	@Bean
	public UniversalCrudSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
		UniversalCrudSqlSessionFactoryBean bean = new UniversalCrudSqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setUniversalCrudScan(universalCrudScan);
		/** 设置mapper.xml文件所在位置 */
		Resource[] mapperLocationsResources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
		bean.setMapperLocations(mapperLocationsResources);
		/** 设置config.xml文件所在位置 */
		Resource configLocationResources = new PathMatchingResourcePatternResolver().getResource(configLocation);
		bean.setConfigLocation(configLocationResources);
		return bean;
	}

	@Bean
	public UniversalCrudSqlSessionTemplate sqlSessionTemplate(UniversalCrudSqlSessionFactoryBean sqlSessionFactory) {
		UniversalCrudSqlSessionTemplate bean = null;
		try {
			bean = new UniversalCrudSqlSessionTemplate(sqlSessionFactory.getObject());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bean;
	}
}

```

