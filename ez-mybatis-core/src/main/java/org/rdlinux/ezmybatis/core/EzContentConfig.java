package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisUpdateInterceptor;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisFieldSetListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;

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
    private EzMybatisConfig ezMybatisConfig;
    private Configuration configuration;
    private EzMybatisUpdateInterceptor updateInterceptor;
    private DbKeywordQMFactory dbKeywordQMFactory;
    private DbType dbType;
    private List<EzMybatisInsertListener> insertListeners;


    /**
     * 添加属性设置监听器
     */
    public void addFieldSetListener(EzMybatisFieldSetListener fieldSetListener) {
        if (this.fieldSetListeners == null) {
            this.fieldSetListeners = new LinkedList<>();
        }
        this.fieldSetListeners.add(fieldSetListener);
        this.fieldSetListeners.sort(Comparator.comparingInt(EzMybatisFieldSetListener::order));
    }
}
