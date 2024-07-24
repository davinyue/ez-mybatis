package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisUpdateInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.listener.*;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * content配置
 */
@Getter
@Setter
@Accessors(chain = true)
public class EzContentConfig {
    /**
     * 属性设置监听器
     */
    private List<EzMybatisFieldSetListener> fieldSetListeners;
    /**
     * 当构建sql获取属性时的监听器
     */
    private List<EzMybatisOnBuildSqlGetFieldListener> onBuildSqlGetFieldListeners;
    /**
     * 配置信息
     */
    private EzMybatisConfig ezMybatisConfig;
    /**
     * mybatis update拦截器
     */
    private EzMybatisUpdateInterceptor updateInterceptor;
    /**
     * 关键词转义符号获取工厂
     */
    private DbKeywordQMFactory dbKeywordQMFactory;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 插入监听器列表
     */
    private List<EzMybatisInsertListener> insertListeners;
    /**
     * 更新监听器列表
     */
    private List<EzMybatisUpdateListener> updateListeners;


    /**
     * 添加属性设置监听器
     */
    public void addFieldSetListener(EzMybatisFieldSetListener listener) {
        if (this.fieldSetListeners == null) {
            this.fieldSetListeners = new LinkedList<>();
        }
        this.fieldSetListeners.add(listener);
        this.fieldSetListeners.sort(Comparator.comparingInt(EzMybatisFieldSetListener::order));
    }

    /**
     * 添加当构建sql获取属性时的监听器
     */
    public void addOnBuildSqlGetFieldListener(EzMybatisOnBuildSqlGetFieldListener listener) {
        if (this.onBuildSqlGetFieldListeners == null) {
            this.onBuildSqlGetFieldListeners = new ArrayList<>();
        }
        this.onBuildSqlGetFieldListeners.add(listener);
        this.onBuildSqlGetFieldListeners.sort(Comparator.comparingInt(EzMybatisOnBuildSqlGetFieldListener::order));
    }

    /**
     * 检查updateInterceptor是否赋值
     */
    private void checkUpdateInterceptor() {
        Assert.notNull(this.updateInterceptor, "updateInterceptor can not be null, " +
                "Please assign a value to updateInterceptor");
    }

    /**
     * 添加insert监听器
     */
    public void addInsertListener(EzMybatisInsertListener listener) {
        this.checkUpdateInterceptor();
        if (this.insertListeners == null) {
            this.insertListeners = new ArrayList<>();
        }
        this.insertListeners.add(listener);
        this.insertListeners.sort(Comparator.comparingInt(EzMybatisInsertListener::order));
        this.updateInterceptor.addInsertListener(listener);
    }

    /**
     * 添加update监听器
     */
    public void addUpdateListener(EzMybatisUpdateListener listener) {
        this.checkUpdateInterceptor();
        if (this.updateListeners == null) {
            this.updateListeners = new ArrayList<>();
        }
        this.updateListeners.add(listener);
        this.updateListeners.sort(Comparator.comparingInt(EzMybatisUpdateListener::order));
        this.updateInterceptor.addUpdateListener(listener);
    }

    /**
     * 添加删除监听器
     */
    public void addDeleteListener(EzMybatisDeleteListener listener) {
        this.checkUpdateInterceptor();
        this.updateInterceptor.addDeleteListener(listener);
    }
}
