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
        //初始化
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
        //调用初始监听器
        Map<String, EzMybatisInitListener> initListenerMap = applicationContext.getBeansOfType(
                EzMybatisInitListener.class);
        initListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisInitListener::order))
                .forEach(e -> e.onDone(ezMybatisConfig));
    }
}
