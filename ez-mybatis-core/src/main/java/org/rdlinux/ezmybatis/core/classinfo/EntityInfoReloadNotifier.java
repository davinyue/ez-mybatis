package org.rdlinux.ezmybatis.core.classinfo;

/**
 * 实体信息热加载通知器。
 * <p>
 * 用于统一处理来自文件监听、IDE HotSwap、DevTools 等外部环境的类变更通知。
 */
public interface EntityInfoReloadNotifier {

    /**
     * 通知指定类已发生变更
     */
    void notifyClassChanged(Class<?> ntClass);

    /**
     * 按类名通知类已发生变更
     */
    void notifyClassChanged(String className);

    /**
     * 通知全部缓存失效
     */
    void notifyAllChanged();
}
