package org.rdlinux.ezmybatis.mp.spring.boot.start;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import org.rdlinux.ezmybatis.mp.entity.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.Collection;

@Configuration
public class EzMybatisPlusAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public MpMySqlEntityInfoBuilder mpMySqlEntityInfoBuilder() {
        return MpMySqlEntityInfoBuilder.getInstance();
    }

    @Bean
    public MpOracleEntityInfoBuilder mpOracleEntityInfoBuilder() {
        return MpOracleEntityInfoBuilder.getInstance();
    }

    @Bean
    public MpDmEntityInfoBuilder mpDmEntityInfoBuilder() {
        return MpDmEntityInfoBuilder.getInstance();
    }

    @Bean
    public MpPostgreSqlEntityInfoBuilder mpPostgreSqlEntityInfoBuilder() {
        return MpPostgreSqlEntityInfoBuilder.getInstance();
    }

    @Bean
    public MpSqlServerEntityInfoBuilder mpSqlServerEntityInfoBuilder() {
        return MpSqlServerEntityInfoBuilder.getInstance();
    }

    @Bean
    public ConfigurationCustomizer ezMbConfigurationCustomizer() {
        return configuration -> {
            Collection<org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer> customizers =
                    this.applicationContext
                            .getBeansOfType(org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer.class)
                            .values();
            for (org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer customizer : customizers) {
                customizer.customize(configuration);
            }
        };
    }
}
