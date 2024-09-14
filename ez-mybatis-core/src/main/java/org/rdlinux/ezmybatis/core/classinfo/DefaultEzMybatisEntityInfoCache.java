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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultEzMybatisEntityInfoCache implements EzMybatisEntityInfoCache {
    /**
     * 实体信息映射
     */
    protected static final ConcurrentMap<Configuration, ConcurrentMap<String, EntityClassInfo>> ENTITY_INFO_MAP =
            new ConcurrentHashMap<>();
    private static final String CLASS_TAG = "target" + File.separator + "classes";
    private static final String CLASS_TEST_TAG = "target" + File.separator + "test-classes";
    private static final Log log = LogFactory.getLog(DefaultEzMybatisEntityInfoCache.class);

    public DefaultEzMybatisEntityInfoCache() {
        this.initClassFileChangeWatch();
    }

    private List<Path> findPath() {
        //当前项目运行路径
        Path currentRunPath = Paths.get("").toAbsolutePath();
        Path parent = currentRunPath.getParent();
        File parentFile = new File(parent.toUri());
        //当前路径下的文件夹
        File[] currentFiles = parentFile.listFiles((dir, name) -> !name.startsWith("."));
        //下级路径
        File currentFile = new File(currentRunPath.toUri());
        File[] sonFiles = currentFile.listFiles((dir, name) -> !name.startsWith("."));
        List<File> listenFiles = new LinkedList<>();
        if (currentFiles != null) {
            listenFiles.addAll(Arrays.asList(currentFiles));
        }
        if (sonFiles != null) {
            listenFiles.addAll(Arrays.asList(sonFiles));
        }
        List<Path> sonPaths = new LinkedList<>();
        for (File file : listenFiles) {
            if (!file.isDirectory()) {
                continue;
            }
            String absolutePath = file.getAbsolutePath();
            File sonFile = new File(absolutePath + File.separator + CLASS_TAG);
            if (sonFile.exists()) {
                sonPaths.add(Paths.get(absolutePath + File.separator + CLASS_TAG));
            }
            sonFile = new File(absolutePath + File.separator + CLASS_TEST_TAG);
            if (sonFile.exists()) {
                sonPaths.add(Paths.get(absolutePath + File.separator + CLASS_TEST_TAG));
            }
        }
        return sonPaths;
    }

    /**
     * 初始化类文件变更监控
     */
    protected void initClassFileChangeWatch() {
        URL classLocation = DefaultEzMybatisEntityInfoCache.class.getResource("/");
        if (classLocation == null) {
            log.debug("Disable hot reloading of class information.");
            return;
        }
        if (classLocation.toString().startsWith("jar:file:")) {
            if (log.isDebugEnabled()) {
                log.debug("Disable hot reloading of class information.");
            }
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Enable hot reloading of class information.");
            log.debug("Hot reloading of class information is only supported for the built artifact directories " +
                    "in the project's \"target/classes\" and \"target/test-classes\".");
        }
        List<Path> allPath = this.findPath();
        if (allPath.isEmpty()) {
            return;
        }
        Thread cleanThread = new Thread(() -> {
            try {
                //创建监视服务类
                WatchService ws = FileSystems.getDefault().newWatchService();
                // 删除和修改事件
                WatchEvent.Kind<?>[] kinds = {
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY};
                Map<WatchKey, Path> keys = new HashMap<>(16);
                // 注册子目录到监视服务
                for (Path path : allPath) {
                    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir,
                                                                 BasicFileAttributes attrs) throws IOException {
                            keys.put(dir.register(ws, kinds), dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
                while (true) {
                    WatchKey watchKey = ws.take();
                    Path path = keys.get(watchKey);
                    String pathStr = path.toString();
                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                    for (WatchEvent<?> event : watchEvents) {
                        if (event.context().toString().endsWith(".class")) {
                            String packageStr;
                            String flagStr = CLASS_TAG;
                            if (pathStr.contains(CLASS_TEST_TAG)) {
                                flagStr = CLASS_TEST_TAG;
                            }
                            packageStr = pathStr.substring(pathStr.indexOf(flagStr) + flagStr.length() + 1)
                                    .replace(File.separator, ".");
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
            } catch (Exception e) {
                DefaultEzMybatisEntityInfoCache.log.warn(e.getMessage());
            }
        });
        cleanThread.setDaemon(true);
        cleanThread.setName("ez-mybatis-entity-info-hot-reloading");
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
        ConcurrentMap<String, EntityClassInfo> entityInfo = ENTITY_INFO_MAP.computeIfAbsent(configuration,
                k -> new ConcurrentHashMap<>());
        entityInfo.put(entityClassInfo.getEntityClass().getName(), entityClassInfo);
    }
}
