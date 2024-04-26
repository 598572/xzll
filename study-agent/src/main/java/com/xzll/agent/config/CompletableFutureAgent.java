package com.xzll.agent.config;

//import com.alibaba.fastjson.JSON;

import com.xzll.agent.config.po.ClassInfo;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author: hzz
 * @Date: 2023/3/3 09:15:21
 * @Description: 对CompletableFuture插桩
 */
public class CompletableFutureAgent {

	private static volatile Map<String, String> classNameMethodNameMap;

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			int finalI = i;
			CompletableFuture.runAsync(()->{
				System.out.println("当前i值:"+ finalI);
			});
		}
		Thread.sleep(5000L);

	}


	static Map<String, String> splitCommaColonStringToKV(String commaColonString) {
		Map<String, String> ret = new HashMap();
		if (StringUtils.isBlank(commaColonString)) {
			return ret;
		}
		if (StringUtils.isNotBlank(commaColonString)) {
			String[] splitKvArray = commaColonString.trim().split("\\s*,\\s*");
			String[] var3 = splitKvArray;
			int var4 = splitKvArray.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				String kvString = var3[var5];
				String[] kv = kvString.trim().split("\\s*:\\s*");
				if (kv.length != 0) {
					if (kv.length == 1) {
						ret.put(kv[0], "");
					} else {
						ret.put(kv[0], kv[1]);
					}
				}
			}

			return ret;
		} else {
			return ret;
		}
	}

	private static List<String> needMonitorThreadPool;

	/**
	 * 实现了带Instrumentation参数的premain()方法。
	 *
	 * @param args
	 * @param inst
	 * @throws Exception
	 */
	public static void premain(String args, Instrumentation inst) throws Exception {
		//调用addTransformer()方法对启动时所有的类(应用层)进行拦截
		classNameMethodNameMap = splitCommaColonStringToKV(args);

		//需要监控的线程池
		needMonitorThreadPool = classNameMethodNameMap.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());

		inst.addTransformer(new DefineTransformer(), true);
	}

	static class DefineTransformer implements ClassFileTransformer {

		/**
		 * 如果事先知道哪些类需要修改，最简单的修改类方式如下：
		 * <p>
		 * 1、通过调用ClassPool.get()方法获取一个CtClass对象
		 * 2、修改它
		 * 3、调用CtClass对象的writeFile()或toBytecode()方法获取修改后的类文件
		 *
		 * @param loader
		 * @param className
		 * @param classBeingRedefined
		 * @param protectionDomain
		 * @param classfileBuffer
		 * @return
		 * @throws IllegalClassFormatException
		 */
		@Override
		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

//			System.out.println("在main方法前加载类 : " + className);
			ClassInfo classInfo = new ClassInfo(className, classfileBuffer, loader);
			//要处理的类的函数
			if (needMonitorThreadPool.contains(className)) {
				System.out.println("transform_当前className:" + className);
				String monitorMethod = classNameMethodNameMap.get(className);
				CtClass clazz = null;
				try {
					clazz = classInfo.getCtClass();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("对CompletableFuture的asyncRunStage方法进行插桩，从而输出每次任务运行后的线程池信息");
				try {
					CtMethod monitorMethodCt = clazz.getDeclaredMethod(monitorMethod);
					System.out.println("transform_before:" + monitorMethodCt.toString());
					//植入探针方法
					String methodBody =
								"{" +
									"if ($2 == null) {throw new NullPointerException();}" +
									"        java.util.concurrent.CompletableFuture/*<Void>*/ d = new java.util.concurrent.CompletableFuture/*<Void>*/();" +
									"		 com.xzll.agent.config.advice.ForkJoinPoolMonitorAdvice.beforeExecute($2);" +
									"        $1.execute(new java.util.concurrent.CompletableFuture$AsyncRun(d,$2));" +
									"		 com.xzll.agent.config.advice.ForkJoinPoolMonitorAdvice.afterExecute($$);" +
									" return d; " +
								"}";
					monitorMethodCt.setBody(methodBody);
					System.out.println("transform_after :" + monitorMethodCt.toString());
					System.out.println();
					//通过toBytecode获取并返回被修改后的字节码。从而替换未插桩前的代码
					return clazz.toBytecode();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null != clazz) {
						//调用CtClass对象的detach()方法后，对应class的其他方法将不能被调用。但是，你能够通过ClassPool的get()方法，
						//重新创建一个代表对应类的CtClass对象。如果调用ClassPool的get()方法， ClassPool将重新读取一个类文件，并且重新创建一个CtClass对象，并通过get()方法返回
						//如下所说：
						//detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
//						clazz.detach();
					}
				}
			}
			return classfileBuffer;
		}


		private void handle(CtClass[] nested) {
			Arrays.stream(nested).forEach((e) -> {
				if (e.getName().equals("java.util.concurrent.CompletableFuture$AsyncRun")) {
					System.out.println("哈哈哈");
				}
			});
		}


//		/**
//		 * 获取 agent jar 的路径，抄springboot的
//		 */
//		private String getAgentJarPath() {
//			ProtectionDomain protectionDomain = getClass().getProtectionDomain();
//			CodeSource codeSource = protectionDomain.getCodeSource();
//			URI location = null;
//			try {
//				location = (codeSource == null ? null : codeSource.getLocation().toURI());
//			} catch (URISyntaxException e) {
//				return null;
//			}
//			String path = (location == null ? null : location.getSchemeSpecificPart());
//			if (path == null) {
//				throw new IllegalStateException("Unable to determine code source");
//			}
//			File root = new File(path);
//			if (!root.exists()) {
//				throw new IllegalStateException(
//						"Unable to determine code source from " + root);
//			}
//			return path;
//		}

//		private void appendAgentNestedJars(ClassLoader classLoader) {
//			String agentJarPath = getAgentJarPath();
//			if (agentJarPath == null) return;
//
//			//LaunchedURLClassLoader 是属于 springboot-loader 的类，没有放到jar in jar里边，所以它是被AppClassLoader加载的
//			if (classLoader instanceof LaunchedURLClassLoader) {
//				LaunchedURLClassLoader launchedURLClassLoader = (LaunchedURLClassLoader) classLoader;
//				try {
//					Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//					method.setAccessible(true);
//					//遍历 agent jar，处理所有对应目录下的jar包，使用 JarFileArchive 获取到的url才可以处理jar in jar
//					JarFileArchive jarFileArchive = new JarFileArchive(new File(agentJarPath));
//					List<Archive> archiveList = jarFileArchive.getNestedArchives(new Archive.EntryFilter() {
//						@Override
//						public boolean matches(Archive.Entry entry) {
//							if (entry.isDirectory()) {
//								return false;
//							}
//							return entry.getName().startsWith("BOOT-INF/lib/") && entry.getName().endsWith(".jar");
//						}
//					});
//					for (Archive archive : archiveList) {
//						method.invoke(launchedURLClassLoader, archive.getUrl());
//						System.out.println("add url to classloader. url:" + archive.getUrl());
//					}
//				} catch (Throwable t) {
//					t.printStackTrace();
//				}
//			}
//
//
//			System.out.println("trigger add urls to classLoader:" + classLoader.getClass().getName() + " agentJarPath:" + agentJarPath);
//
//		}


	}

	private static String toClassName( String classFile) {
		return classFile.replace('/', '.');
	}
}
