package com.if2c.harald.sheduler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.quartz.Job;

import com.if2c.harald.job.JobBase;

public class FunctionHelper {
	/**
	 * 从文件加载类
	 * 
	 * @param pkg
	 *            包路径
	 * @param file
	 *            文件
	 * @return 类或者null
	 */
	private static Class<Job> loadClassByFile(String pkg, File file) {
		if (!file.isFile()) {
			return null;
		}
		String name = file.getName();
		if (name.endsWith(".class")) {
			String ap = file.getAbsolutePath();
			if (!ap.contains(pkg)) {
				return null;
			}
			name = ap.substring(ap.indexOf(pkg) + pkg.length());
			if (name.startsWith(File.separator)) {
				name = name.substring(1);
			}
			String path = (pkg + "." + name.substring(0, name.lastIndexOf(".")))
					.replace(File.separatorChar, '.');
			try {
				Class<Job> c = (Class<Job>) Class.forName(path);
				if (FunctionHelper.isFunction(c)) {
					return c;
				}
			} catch (ClassNotFoundException e) {
				// do nothing
			}
		}
		return null;
	}

	/**
	 * 遍历文件夹下所有的类
	 * 
	 * @param path
	 *            包路径
	 * @param file
	 *            文件
	 * @param list
	 *            保存类列表
	 */
	private static void dirWalker(String path, File file, List<Class<Job>> list) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					FunctionHelper.dirWalker(path, f, list);
				}
			} else {
				Class<Job> c = FunctionHelper.loadClassByFile(path, file);
				if (c != null) {
					list.add(c);
				}
			}
		}
	}

	/**
	 * 获取包下所有的函数实现类
	 * 
	 * @param pkg
	 *            包名,此处只是为了限定,防止漫无目的的查找.不用设置也可以,就要每找到一个类就要加载一次判断了
	 * @return 类列表
	 */
	public static List<Class<Job>> getClasses(String pkg) {
		List<Class<Job>> list = new ArrayList<Class<Job>>();
		for (File f : FunctionHelper.listPaths()) {
			// 如果是以文件的形式保存在服务器上
			if (f.isDirectory()) {
				// 获取包的物理路径
				String path = pkg.replace('.', File.separatorChar);
				FunctionHelper.dirWalker(path, f, list);
			} else {// 尝试是否是jar文件
				// 获取jar
				JarFile jar = null;
				try {
					jar = new JarFile(f);
				} catch (IOException e) {
					// 有可能不是一个jar
				}
				if (jar == null) {
					continue;
				}
				String path = pkg.replace('.', '/');
				// 从此jar包 得到一个枚举类
				Enumeration<JarEntry> entries = jar.entries();
				// 同样的进行循环迭代
				while (entries.hasMoreElements()) {
					// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					// 如果是以/开头的
					if (name.charAt(0) == '/') {
						// 获取后面的字符串
						name = name.substring(1);
					}
					// 如果前半部分和定义的包名相同
					if (name.contains(path)) {
						if (name.endsWith(".class") && !entry.isDirectory()) {
							name = name.replace("/", ".").substring(0,
									name.lastIndexOf("."));
							try {
								Class<Job> c = (Class<Job>) Class.forName(name);
								if (FunctionHelper.isFunction(c)) {
									list.add(c);
								}
							} catch (Exception e) {
								// 找不到无所谓了
							}
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 判断类是否函数类.<br>
	 * 首先,类不能是抽象的,其次,类必须实现函数接口
	 * 
	 * @param c
	 *            类
	 * @return 是否是函数类
	 */
	public static boolean isFunction(Class<?> c) {
		if (c == null) {
			return false;
		}
		if (c.isInterface()) {
			return false;
		}
		if (Modifier.isAbstract(c.getModifiers())) {
			return false;// 抽象
		}
		// Class<?>[] interfaces = c.getInterfaces();
		// if (interfaces == null || interfaces.length == 0) {
		// return false;
		// }
		// for (Class<?> i : interfaces) {
		// if (i == IFunction.class) {
		// return true;
		// }
		// }
		return JobBase.class.isAssignableFrom(c);
	}

	/**
	 * 获取项目的path下所有的文件夹和文件
	 * 
	 * @return 文件列表
	 */
	private static List<File> listPaths() {
		List<File> files = new ArrayList<File>();
		String jars = System.getProperty("java.class.path");
		if (jars == null) {
			System.err.println("java.class.path is null!");
			return files;
		}
		jars = jars.replaceAll(":", ";");
		URL root = FunctionHelper.class.getClassLoader().getResource("");
		if (root == null) {
			System.err.println("path root is null!");
			return files;
		}
		String path = null;
		try {
			path = URLDecoder.decode(root.getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return files;
		}
		File dir = new File(path);
		String[] array = (jars).split(";");
		if (array != null) {
			for (String s : array) {
				if (s == null) {
					continue;
				}
				File f = new File(s);
				if (f.exists()) {
					files.add(f);
				} else {// 有些jar就在系统目录下,省略了路径,要加上
					File jar = new File(dir, s);
					if (jar.exists()) {
						files.add(jar);
					}
				}
			}
		}
		return files;
	}
}