package org.rdlinux.ezmybatis.core.classinfo;

import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

/**
 * 默认实体信息热加载通知器实现。
 */
public class DefaultEntityInfoReloadNotifier implements EntityInfoReloadNotifier {
    private final EzMybatisEntityInfoCache entityInfoCache;

    public DefaultEntityInfoReloadNotifier(EzMybatisEntityInfoCache entityInfoCache) {
        Assert.notNull(entityInfoCache, "EntityInfoCache can not be null");
        this.entityInfoCache = entityInfoCache;
    }

    @Override
    public void notifyClassChanged(Class<?> ntClass) {
        Assert.notNull(ntClass, "NtClass can not be null");
        ReflectionUtils.clearCache(ntClass);
        this.entityInfoCache.remove(ntClass);
    }

    @Override
    public void notifyClassChanged(String className) {
        Assert.notNull(className, "ClassName can not be null");
        Class<?> ntClass = this.loadClass(className);
        if (ntClass != null) {
            ReflectionUtils.clearCache(ntClass);
        }
        this.entityInfoCache.remove(className);
    }

    @Override
    public void notifyAllChanged() {
        ReflectionUtils.clearAllCache();
        this.entityInfoCache.clear();
    }

    private Class<?> loadClass(String className) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                return Class.forName(className, false, contextClassLoader);
            }
            return Class.forName(className);
        } catch (Throwable e) {
            return null;
        }
    }
}
