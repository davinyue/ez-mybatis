package org.linuxprobe.crud.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScan {
	private static List<Class<?>> scanDirClass(String basePackage) {
		String testClasspath = ClassScan.class.getResource("/").getPath();
		String classpath = null;
		if (testClasspath.endsWith("test-classes/")) {
			classpath = testClasspath.substring(0, testClasspath.length() - "test-classes/".length()) + "classes/";
		} else {
			classpath = testClasspath;
			testClasspath = null;
		}
		List<File> classFiles = new LinkedList<>();
		/** 扫描classpath下面的文件 */
		basePackage = basePackage.replace(".", File.separator);
		String searchPath = classpath + basePackage;
		classFiles.addAll(getClassFile(new File(searchPath)));
		/** 扫描testClasspath下面的文件 */
		searchPath = testClasspath + basePackage;
		classFiles.addAll(getClassFile(new File(searchPath)));
		List<Class<?>> result = new LinkedList<>();
		for (File classFile : classFiles) {
			String s = classFile.getAbsolutePath();
			s = s.replace(classpath.replace("/", "\\").replaceFirst("\\\\", ""), "").replace("\\", ".")
					.replace(".class", "");
			Class<?> cls = null;
			try {
				cls = Class.forName(s);
				result.add(cls);
			} catch (ClassNotFoundException e) {
				continue;
			}
		}
		return result;
	}

	/**
	 * 该方法会得到所有的类，将类的绝对路径写入到classPaths中
	 * 
	 * @param file
	 */
	private static List<File> getClassFile(File searchPathFile) {
		List<File> classFiles = new LinkedList<>();
		if (searchPathFile.isDirectory()) {
			File[] files = searchPathFile.listFiles();
			for (File file : files) {
				classFiles.addAll(getClassFile(file));
			}
		} else {
			if (searchPathFile.getName().endsWith(".class")) {
				classFiles.add(searchPathFile);
			}
		}
		return classFiles;
	}

	private static List<Class<?>> scanJarClass(String basePack) throws IOException {
		/** 通过当前线程得到类加载器从而得到URL的枚举 */
		Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
				.getResources(basePack.replace(".", "/"));
		List<Class<?>> result = new LinkedList<>();
		while (urlEnumeration.hasMoreElements()) {
			/** 得到的结果大概是：jar:file:/C:/Users/ibm/.m2/repository/junit/junit/4.12/junit-4.12.jar!/org/junit */
			URL url = urlEnumeration.nextElement();
			/** 大概是jar */
			String protocol = url.getProtocol();
			if ("jar".equalsIgnoreCase(protocol)) {
				/** 转换为JarURLConnection */
				JarURLConnection connection = (JarURLConnection) url.openConnection();
				if (connection != null) {
					JarFile jarFile = connection.getJarFile();
					if (jarFile != null) {
						/** 得到该jar文件下面的类实体 */
						Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
						while (jarEntryEnumeration.hasMoreElements()) {
							/**
							 * entry的结果大概是这样： org/ org/junit/ org/junit/rules/
							 * org/junit/runners/
							 */
							JarEntry entry = jarEntryEnumeration.nextElement();
							String jarEntryName = entry.getName();
							/** 这里我们需要过滤不是class文件和不在basePack包名下的类 */
							if (jarEntryName.contains(".class")
									&& jarEntryName.replaceAll("/", ".").startsWith(basePack)) {
								String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/",
										".");
								Class<?> cls = null;
								try {
									cls = Class.forName(className);
									result.add(cls);
								} catch (ClassNotFoundException e) {
									continue;
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 扫描指定包下面的类
	 * 
	 * @param basePackage
	 *            需扫描包
	 */
	public static List<Class<?>> scan(String basePackage) throws IOException {
		List<Class<?>> result = new LinkedList<>();
		result.addAll(ClassScan.scanDirClass(basePackage));
		result.addAll(ClassScan.scanJarClass(basePackage));
		return result;
	}
}
