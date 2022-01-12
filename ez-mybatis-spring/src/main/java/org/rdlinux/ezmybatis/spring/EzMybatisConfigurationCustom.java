package org.rdlinux.ezmybatis.spring;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.utils.Assert;
import org.springframework.context.ApplicationContext;

import java.util.Comparator;
import java.util.Map;

public class EzMybatisConfigurationCustom {
    private Configuration configuration;
    private ApplicationContext applicationContext;


    public EzMybatisConfigurationCustom(Configuration configuration, ApplicationContext applicationContext) {
        Assert.notNull(configuration, "configuration can not be null");
        Assert.notNull(applicationContext, "applicationContext can not be null");
        this.configuration = configuration;
        this.applicationContext = applicationContext;
        this.init();
    }

    private void init() {
        EzMybatisContent.init(this.configuration);
        Map<String, EzMybatisInsertListener> insertListenerMap = this.applicationContext.getBeansOfType(
                EzMybatisInsertListener.class);
        insertListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisInsertListener::order))
                .forEach(e -> EzMybatisContent.addInsertListener(this.configuration, e));
        Map<String, EzMybatisUpdateListener> updateListenerMap = this.applicationContext.getBeansOfType(
                EzMybatisUpdateListener.class);
        updateListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisUpdateListener::order))
                .forEach(e -> EzMybatisContent.addUpdateListener(this.configuration, e));
        Map<String, EzMybatisDeleteListener> deleteListenerMap = this.applicationContext.getBeansOfType(
                EzMybatisDeleteListener.class);
        deleteListenerMap.values().stream().sorted(Comparator.comparingInt(EzMybatisDeleteListener::order))
                .forEach(e -> EzMybatisContent.addDeleteListener(this.configuration, e));
    }
}
