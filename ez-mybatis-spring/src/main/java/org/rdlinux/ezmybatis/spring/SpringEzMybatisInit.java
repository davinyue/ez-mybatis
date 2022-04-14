package org.rdlinux.ezmybatis.spring;

import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.utils.Assert;
import org.springframework.context.ApplicationContext;

import java.util.Comparator;
import java.util.Map;

public class SpringEzMybatisInit {
    public static void init(EzMybatisConfig ezMybatisConfig, ApplicationContext applicationContext) {
        Assert.notNull(ezMybatisConfig, "ezMybatisConfig can not be null");
        Assert.notNull(applicationContext, "applicationContext can not be null");
        EzMybatisContent.init(ezMybatisConfig);
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
    }
}
