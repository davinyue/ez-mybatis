package org.rdlinux;

import org.apache.ibatis.session.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.classinfo.BuildOutputPathResolver;
import org.rdlinux.ezmybatis.core.classinfo.DefaultEntityInfoReloadNotifier;
import org.rdlinux.ezmybatis.core.classinfo.EzMybatisEntityInfoCache;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class HotReloadSupportTest {

    @Test
    public void shouldClearReflectionCacheAndNotifyClassChange() throws Exception {
        ReflectionUtils.getAllFields(SampleEntity.class);
        Assert.assertTrue(this.getFieldsCache().containsKey(SampleEntity.class));

        RecordingEntityInfoCache cache = new RecordingEntityInfoCache();
        DefaultEntityInfoReloadNotifier notifier = new DefaultEntityInfoReloadNotifier(cache);

        notifier.notifyClassChanged(SampleEntity.class);

        Assert.assertFalse(this.getFieldsCache().containsKey(SampleEntity.class));
        Assert.assertEquals(SampleEntity.class.getName(), cache.removedClassName);
    }

    @Test
    public void shouldResolveSiblingModuleBuildOutputsInMultiModuleLayout() throws Exception {
        Path projectRoot = Files.createTempDirectory("resolver-project");
        Path currentModule = projectRoot.resolve("module-a");
        Path siblingModule = projectRoot.resolve("module-b");
        Files.createDirectories(currentModule.resolve("target/classes"));
        Files.createDirectories(currentModule.resolve("target/test-classes"));
        Files.createDirectories(siblingModule.resolve("target/classes"));

        BuildOutputPathResolver resolver = new BuildOutputPathResolver();
        Set<Path> resolvedPaths = new LinkedHashSet<>();
        Method method = BuildOutputPathResolver.class
                .getDeclaredMethod("collectBuildOutputPaths", Path.class, Set.class);
        method.setAccessible(true);
        method.invoke(resolver, projectRoot, resolvedPaths);

        Set<Path> expectedPaths = new LinkedHashSet<>();
        expectedPaths.add(currentModule.resolve("target/classes").toAbsolutePath().normalize());
        expectedPaths.add(currentModule.resolve("target/test-classes").toAbsolutePath().normalize());
        expectedPaths.add(siblingModule.resolve("target/classes").toAbsolutePath().normalize());

        Assert.assertTrue(resolvedPaths.containsAll(expectedPaths));
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, ?> getFieldsCache() throws Exception {
        Field field = ReflectionUtils.class.getDeclaredField("FIELDS_CACHE");
        field.setAccessible(true);
        return (Map<Class<?>, ?>) field.get(null);
    }

    private static class RecordingEntityInfoCache implements EzMybatisEntityInfoCache {
        private String removedClassName;

        @Override
        public EntityClassInfo get(Configuration configuration, Class<?> ntClass) {
            return null;
        }

        @Override
        public void set(Configuration configuration, EntityClassInfo entityClassInfo) {
        }

        @Override
        public void remove(Class<?> ntClass) {
            this.removedClassName = ntClass.getName();
        }

        @Override
        public void remove(String className) {
            this.removedClassName = className;
        }

        @Override
        public void clear(Configuration configuration) {
        }

        @Override
        public void clear() {
        }
    }

    private static class SampleEntity {
        private String id;
        private String name;
    }
}
