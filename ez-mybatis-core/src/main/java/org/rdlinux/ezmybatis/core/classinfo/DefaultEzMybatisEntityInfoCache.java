package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.utils.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultEzMybatisEntityInfoCache implements EzMybatisEntityInfoCache {
    /**
     * 实体信息映射
     */
    protected static final ConcurrentMap<Configuration, ConcurrentMap<String, EntityClassInfo>> ENTITY_INFO_MAP =
            new ConcurrentHashMap<>();
    private static final Log log = LogFactory.getLog(DefaultEzMybatisEntityInfoCache.class);

    public DefaultEzMybatisEntityInfoCache() {
        this.initClassFileChangeWatch();
    }

    /**
     * 初始化类文件变更监控
     */
    protected void initClassFileChangeWatch() {
        URL classLocation = DefaultEzMybatisEntityInfoCache.class.getResource("/");
        if (classLocation.toString().startsWith("jar:file:")) {
            return;
        }
        Thread cleanThread = new Thread(() -> {
            try {
                //需要监视的文件目录（注意：只能监听目录）
                Path start = Paths.get(classLocation.toURI());
                //创建监视服务类
                WatchService ws = FileSystems.getDefault().newWatchService();
                // 删除和修改事件
                WatchEvent.Kind<?>[] kinds = {
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY};
                Map<WatchKey, Path> keys = new HashMap<>(16);
                // 注册子目录到监视服务。
                Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir,
                                                             BasicFileAttributes attrs) throws IOException {
                        keys.put(dir.register(ws, kinds), dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                while (true) {
                    WatchKey watchKey = ws.take();
                    Path path = keys.get(watchKey);
                    String pathStr = path.toString();
                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                    String separator = String.valueOf(File.separatorChar);
                    String classSubPath = "classes";
                    String splitStr = separator + "target" + separator + classSubPath + separator;
                    for (WatchEvent<?> event : watchEvents) {
                        if (pathStr.contains(splitStr) && event.context().toString().endsWith(".class")) {
                            String packageStr = pathStr.substring(pathStr.indexOf(splitStr) + splitStr.length())
                                    .replace(separator, ".");
                            String className = event.context().toString().split("\\.")[0];
                            String key = packageStr + "." + className;
                            for (ConcurrentMap<String, EntityClassInfo> cache : ENTITY_INFO_MAP.values()) {
                                EntityClassInfo classInfo = cache.remove(key);
                                if (classInfo != null && log.isDebugEnabled()) {
                                    log.debug(String.format("Cleaning the class information of %s", key));
                                }
                            }
                        }
                    }
                    watchKey.reset();
                }
            } catch (Exception ignored) {
            }
        });
        cleanThread.setDaemon(true);
        cleanThread.setName("ez-mybatis-entity-info-clean");
        cleanThread.start();
    }

    @Override
    public EntityClassInfo get(Configuration configuration, Class<?> ntClass) {
        Assert.notNull(configuration, "Configuration can not be null");
        Assert.notNull(ntClass, "NtClass can not be null");
        Map<String, EntityClassInfo> entityInfo = ENTITY_INFO_MAP.get(configuration);
        if (entityInfo == null) {
            return null;
        }
        return entityInfo.get(ntClass.getName());
    }

    @Override
    public void set(Configuration configuration, EntityClassInfo entityClassInfo) {
        Assert.notNull(configuration, "Configuration can not be null");
        Assert.notNull(entityClassInfo, "EntityClassInfo can not be null");
        ConcurrentMap<String, EntityClassInfo> entityInfo = ENTITY_INFO_MAP.get(configuration);
        if (entityInfo == null) {
            entityInfo = new ConcurrentHashMap<>();
            ENTITY_INFO_MAP.put(configuration, entityInfo);
        }
        entityInfo.put(entityClassInfo.getEntityClass().getName(), entityClassInfo);
    }
}
