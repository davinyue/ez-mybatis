package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.rdlinux.ezmybatis.utils.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 实体信息热加载管理器。
 */
public class EntityInfoHotReloadManager {
    private static final Log log = LogFactory.getLog(EntityInfoHotReloadManager.class);
    private final BuildOutputPathResolver buildOutputPathResolver;
    private final Supplier<EntityInfoReloadNotifier> reloadNotifierSupplier;

    public EntityInfoHotReloadManager(BuildOutputPathResolver buildOutputPathResolver,
                                      Supplier<EntityInfoReloadNotifier> reloadNotifierSupplier) {
        Assert.notNull(buildOutputPathResolver, "BuildOutputPathResolver can not be null");
        Assert.notNull(reloadNotifierSupplier, "ReloadNotifierSupplier can not be null");
        this.buildOutputPathResolver = buildOutputPathResolver;
        this.reloadNotifierSupplier = reloadNotifierSupplier;
    }

    public void start() {
        if (!this.isHotReloadSupported()) {
            if (log.isDebugEnabled()) {
                log.debug("Disable hot reloading of class information.");
            }
            return;
        }
        Set<Path> buildOutputPaths = this.buildOutputPathResolver.resolve();
        if (buildOutputPaths.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Disable hot reloading of class information because no build output path was resolved.");
            }
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Enable hot reloading of class information.");
        }
        Thread cleanThread = new Thread(() -> this.watch(buildOutputPaths));
        cleanThread.setDaemon(true);
        cleanThread.setName("ez-mybatis-entity-info-hot-reloading");
        cleanThread.start();
    }

    private boolean isHotReloadSupported() {
        URL classLocation = DefaultEzMybatisEntityInfoCache.class.getResource("/");
        return classLocation != null && !classLocation.toString().startsWith("jar:file:");
    }

    private void watch(Set<Path> buildOutputPaths) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Map<WatchKey, Path> watchKeys = new HashMap<>(16);
            for (Path buildOutputPath : buildOutputPaths) {
                this.registerRecursively(watchService, watchKeys, buildOutputPath);
            }
            while (true) {
                WatchKey watchKey = watchService.take();
                Path watchedPath = watchKeys.get(watchKey);
                if (watchedPath == null) {
                    watchKey.reset();
                    continue;
                }
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    this.handleWatchEvent(watchService, watchKeys, watchedPath, event);
                }
                watchKey.reset();
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void handleWatchEvent(WatchService watchService, Map<WatchKey, Path> watchKeys, Path watchedPath,
                                  WatchEvent<?> event) throws IOException {
        Path changedPath = watchedPath.resolve((Path) event.context()).normalize();
        if (StandardWatchEventKinds.OVERFLOW == event.kind()) {
            return;
        }
        if (Files.isDirectory(changedPath, LinkOption.NOFOLLOW_LINKS)
                && StandardWatchEventKinds.ENTRY_CREATE == event.kind()) {
            this.registerRecursively(watchService, watchKeys, changedPath);
            return;
        }
        if (!changedPath.toString().endsWith(".class")) {
            return;
        }
        String className = this.toClassName(changedPath);
        if (className == null) {
            return;
        }
        this.reloadNotifierSupplier.get().notifyClassChanged(className);
    }

    private void registerRecursively(WatchService watchService, Map<WatchKey, Path> watchKeys, Path root)
            throws IOException {
        if (!Files.exists(root)) {
            return;
        }
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                WatchKey watchKey = dir.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                watchKeys.put(watchKey, dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private String toClassName(Path classFilePath) {
        Path normalizedClassFilePath = classFilePath.toAbsolutePath().normalize();
        for (Path buildOutputPath : this.buildOutputPathResolver.resolve()) {
            Path normalizedBuildOutputPath = buildOutputPath.toAbsolutePath().normalize();
            if (!normalizedClassFilePath.startsWith(normalizedBuildOutputPath)) {
                continue;
            }
            Path relativePath = normalizedBuildOutputPath.relativize(normalizedClassFilePath);
            String className = relativePath.toString();
            className = className.substring(0, className.length() - ".class".length());
            return className.replace(File.separatorChar, '.');
        }
        return null;
    }
}
