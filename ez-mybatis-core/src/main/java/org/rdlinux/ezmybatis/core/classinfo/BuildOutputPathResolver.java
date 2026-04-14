package org.rdlinux.ezmybatis.core.classinfo;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 标准编译输出目录解析器。
 * <p>
 * 默认自动发现当前模块、子模块以及必要父级范围内的标准 Maven 编译输出目录，
 * 以保证多模块项目下仍可零配置使用热加载能力。
 */
public class BuildOutputPathResolver {
    private static final String CLASS_TAG = "target" + File.separator + "classes";
    private static final String CLASS_TEST_TAG = "target" + File.separator + "test-classes";
    private static final Log log = LogFactory.getLog(BuildOutputPathResolver.class);

    public Set<Path> resolve() {
        Set<Path> resolvedPaths = new LinkedHashSet<>();
        Path currentRunPath = Paths.get("").toAbsolutePath().normalize();
        this.collectBuildOutputPaths(currentRunPath, resolvedPaths);
        Path parent = currentRunPath.getParent();
        if (parent != null) {
            this.collectBuildOutputPaths(parent, resolvedPaths);
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("Resolved %s hot reload path(s).", resolvedPaths.size()));
        }
        return resolvedPaths;
    }

    private void collectBuildOutputPaths(Path root, Set<Path> resolvedPaths) {
        if (root == null) {
            return;
        }
        this.addBuildOutputPath(root, resolvedPaths);
        File rootFile = root.toFile();
        File[] children = rootFile.listFiles((dir, name) -> !name.startsWith("."));
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (!child.isDirectory()) {
                continue;
            }
            this.addBuildOutputPath(child.toPath(), resolvedPaths);
        }
    }

    private void addBuildOutputPath(Path modulePath, Set<Path> resolvedPaths) {
        File classesDir = modulePath.resolve(CLASS_TAG).toFile();
        if (classesDir.exists() && classesDir.isDirectory()) {
            resolvedPaths.add(classesDir.toPath().toAbsolutePath().normalize());
        }
        File testClassesDir = modulePath.resolve(CLASS_TEST_TAG).toFile();
        if (testClassesDir.exists() && testClassesDir.isDirectory()) {
            resolvedPaths.add(testClassesDir.toPath().toAbsolutePath().normalize());
        }
    }
}
