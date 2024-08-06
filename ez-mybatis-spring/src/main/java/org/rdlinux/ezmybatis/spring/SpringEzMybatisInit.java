package org.rdlinux.ezmybatis.spring;

import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.EzMybatisEntityInfoCache;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.core.interceptor.listener.*;
import org.rdlinux.ezmybatis.utils.Assert;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.Comparator;
import java.util.Map;

public class SpringEzMybatisInit {
    public static void init(EzMybatisConfig ezMybatisConfig, ApplicationContext applicationContext) {
        Assert.notNull(ezMybatisConfig, "ezMybatisConfig can not be null");
        Assert.notNull(applicationContext, "applicationContext can not be null");
        //初始化实体信息构造器
        applicationContext.getBeansOfType(EntityInfoBuilder.class).values()
                .forEach(EzEntityClassInfoFactory::setEntityInfoBuilder);
        //初始化实体信息缓存器
        try {
            EzMybatisEntityInfoCache entityInfoCache = applicationContext.getBean(EzMybatisEntityInfoCache.class);
            EzEntityClassInfoFactory.setEntityInfoCache(entityInfoCache);
        } catch (NoSuchBeanDefinitionException e) {
            //NoSuchBeanDefinitionException继承类有NoUniqueBeanDefinitionException, 它是找到多个bean定义, 如果是多个
            //bean定义应该抛出异常
            if (!e.getClass().getName().equals(NoSuchBeanDefinitionException.class.getName())) {
                throw e;
            }
        }
        //初始化上下文
        EzMybatisContent.init(ezMybatisConfig);
        //添加事件处理器
        Map<String, EzMybatisInsertListener> insertListenerMap = applicationContext.getBeansOfType(
                EzMybatisInsertListener.class);
        insertListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisInsertListener::order))
                .forEach(e -> EzMybatisContent.addInsertListener(ezMybatisConfig, e));
        Map<String, EzMybatisUpdateListener> updateListenerMap = applicationContext.getBeansOfType(
                EzMybatisUpdateListener.class);
        updateListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisUpdateListener::order))
                .forEach(e -> EzMybatisContent.addUpdateListener(ezMybatisConfig, e));
        Map<String, EzMybatisDeleteListener> deleteListenerMap = applicationContext.getBeansOfType(
                EzMybatisDeleteListener.class);
        deleteListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisDeleteListener::order))
                .forEach(e -> EzMybatisContent.addDeleteListener(ezMybatisConfig, e));
        Map<String, EzMybatisOnBuildSqlGetFieldListener> buildSqlGetFieldListenerMap = applicationContext
                .getBeansOfType(EzMybatisOnBuildSqlGetFieldListener.class);
        buildSqlGetFieldListenerMap.values().stream()
                .sorted(Comparator.comparingInt(EzMybatisOnBuildSqlGetFieldListener::order))
                .forEach(e -> EzMybatisContent.addOnBuildSqlGetFieldListener(ezMybatisConfig, e));
        Map<String, EzMybatisQueryRetListener> buildRetListenerMap = applicationContext.getBeansOfType(
                EzMybatisQueryRetListener.class);
        buildRetListenerMap.values().stream()
                .sorted(Comparator.comparingInt(EzMybatisQueryRetListener::order))
                .forEach(e -> EzMybatisContent.addQueryRetListener(ezMybatisConfig, e));
        //调用初始监听器
        Map<String, EzMybatisInitListener> initListenerMap = applicationContext.getBeansOfType(
                EzMybatisInitListener.class);
        initListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisInitListener::order))
                .forEach(e -> e.onDone(ezMybatisConfig));
    }
}
