package ink.dvc.ezmybatis.spring.boot.start;

import ink.dvc.ezmybatis.core.mapper.EzMapper;
import ink.dvc.ezmybatis.spring.EzMybatisMapperScannerConfigurer;
import ink.dvc.ezmybatis.spring.boot.EzConfigurationCustomizer;
import ink.dvc.ezmybatis.spring.interceptor.EzMybatisSpringUpdateInterceptor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Import(EzMybatisAutoConfiguration.EzMapperRegistrar.class)
@Configuration
@ConditionalOnClass({EzMapper.class})
@AutoConfigureBefore({MybatisAutoConfiguration.class})
public class EzMybatisAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(EzMybatisAutoConfiguration.class);

    @Bean
    public EzConfigurationCustomizer ezConfigurationCustomizer() {
        return new EzConfigurationCustomizer(null, null,
                this.ezMybatisSpringUpdateInterceptor());
    }

    @Bean
    public EzMybatisSpringUpdateInterceptor ezMybatisSpringUpdateInterceptor() {
        return new EzMybatisSpringUpdateInterceptor();
    }

    public static class EzMapperRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                            BeanDefinitionRegistry registry) {
            List<String> packages = new LinkedList<>(AutoConfigurationPackages.get(this.beanFactory));
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
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }
}
