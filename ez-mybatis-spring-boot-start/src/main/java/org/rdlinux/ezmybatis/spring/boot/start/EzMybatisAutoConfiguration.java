package org.rdlinux.ezmybatis.spring.boot.start;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.TableNamePattern;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.dao.EzDao;
import org.rdlinux.ezmybatis.core.dao.JdbcInsertDao;
import org.rdlinux.ezmybatis.core.dao.JdbcUpdateDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.spring.EzMybatisMapperScannerConfigurer;
import org.rdlinux.ezmybatis.spring.SpringEzMybatisInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Import(EzMybatisAutoConfiguration.EzMapperRegistrar.class)
@Configuration
@ConditionalOnClass({EzMapper.class, EzMybatisProperties.class})
@EnableConfigurationProperties(EzMybatisProperties.class)
@AutoConfigureBefore({MybatisAutoConfiguration.class})
public class EzMybatisAutoConfiguration implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EzMybatisAutoConfiguration.class);
    private ApplicationContext applicationContext;
    @Resource
    private EzMybatisProperties ezMybatisProperties;

    @Bean
    public JdbcInsertDao jdbcInsertDao() {
        SqlSessionTemplate sqlSessionTemplate = this.applicationContext.getBean("sqlSessionTemplate",
                SqlSessionTemplate.class);
        return new JdbcInsertDao(sqlSessionTemplate);
    }

    @Bean
    public JdbcUpdateDao jdbcUpdateDao() {
        SqlSessionTemplate sqlSessionTemplate = this.applicationContext.getBean("sqlSessionTemplate",
                SqlSessionTemplate.class);
        return new JdbcUpdateDao(sqlSessionTemplate);
    }

    @Bean
    public EzDao ezDao() {
        EzMapper ezMapper = this.applicationContext.getBean(EzMapper.class);
        return new EzDao(ezMapper);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ConfigurationCustomizer ezConfigurationCustomizer() {
        return configuration -> {
            EzMybatisConfig ezMybatisConfig = new EzMybatisConfig(configuration);
            ezMybatisConfig.setEscapeKeyword(this.ezMybatisProperties.isEscapeKeyword());
            if (this.ezMybatisProperties.getMapRetKeyPattern() != null) {
                ezMybatisConfig.setMapRetKeyPattern(this.ezMybatisProperties.getMapRetKeyPattern());
            }
            ezMybatisConfig.setTableNamePattern(TableNamePattern.ORIGINAL);
            if (this.ezMybatisProperties.getTableNamePattern() != null) {
                ezMybatisConfig.setTableNamePattern(this.ezMybatisProperties.getTableNamePattern());
            }
            if (this.ezMybatisProperties.getEnableOracleOffsetFetchPage() != null) {
                ezMybatisConfig.setEnableOracleOffsetFetchPage(this.ezMybatisProperties.getEnableOracleOffsetFetchPage());
            }
            SpringEzMybatisInit.init(ezMybatisConfig, EzMybatisAutoConfiguration.this.applicationContext);
            if (this.ezMybatisProperties.getDbType() != null) {
                EzMybatisContent.setDbType(configuration, this.ezMybatisProperties.getDbType());
            }
        };
    }

    public static class EzMapperRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                            BeanDefinitionRegistry registry) {
            List<String> packages = new LinkedList<>(AutoConfigurationPackages.get(this.beanFactory));
            packages.add(EzMapper.class.getPackage().getName());
            if (EzMybatisAutoConfiguration.log.isDebugEnabled()) {
                packages.forEach(pkg -> EzMybatisAutoConfiguration.log
                        .debug("Using auto-configuration base package '{}'", pkg));
            }
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
                    EzMybatisMapperScannerConfigurer.class);
            builder.addPropertyValue("processPropertyPlaceHolders", true);
            builder.addPropertyValue("annotationClass", Mapper.class);
            builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
            BeanWrapper beanWrapper = new BeanWrapperImpl(EzMybatisMapperScannerConfigurer.class);
            Set<String> propertyNames = Stream.of(beanWrapper.getPropertyDescriptors()).map(PropertyDescriptor::getName)
                    .collect(Collectors.toSet());
            if (propertyNames.contains("lazyInitialization")) {
                // Need to mybatis-spring 2.0.2+
                builder.addPropertyValue("lazyInitialization", "${mybatis.lazy-initialization:false}");
            }
            if (propertyNames.contains("defaultScope")) {
                // Need to mybatis-spring 2.0.6+
                builder.addPropertyValue("defaultScope", "${mybatis.mapper-default-scope:}");
            }
            registry.registerBeanDefinition(EzMybatisMapperScannerConfigurer.class.getName(),
                    builder.getBeanDefinition());
        }

        @Override
        public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }
}
