//package com.xzll.agent.config;
//
//import com.alibaba.fastjson.JSON;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//import org.apache.commons.lang3.StringUtils;
//
//import java.lang.instrument.ClassFileTransformer;
//import java.lang.instrument.IllegalClassFormatException;
//import java.lang.instrument.Instrumentation;
//import java.security.ProtectionDomain;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @Author: hzz
// * @Date: 2023/3/3 09:15:21
// * @Description: forkjoin 线程池监控
// */
//public class ForkJoinPoolMonitorAgentTest222 {
//
//	private static volatile Map<String, String> kvs;
//
//	public static void main(String[] args) {
//		Map<String, String> stringStringMap = splitCommaColonStringToKV("11:22,33:44,55:66");
//		System.out.println(JSON.toJSON(stringStringMap));
//	}
//
//
//	static Map<String, String> splitCommaColonStringToKV(String commaColonString) {
//		Map<String, String> ret = new HashMap();
//		if (StringUtils.isBlank(commaColonString)) {
//			return ret;
//		}
//		if (StringUtils.isNotBlank(commaColonString)) {
//			String[] splitKvArray = commaColonString.trim().split("\\s*,\\s*");
//			String[] var3 = splitKvArray;
//			int var4 = splitKvArray.length;
//
//			for (int var5 = 0; var5 < var4; ++var5) {
//				String kvString = var3[var5];
//				String[] kv = kvString.trim().split("\\s*:\\s*");
//				if (kv.length != 0) {
//					if (kv.length == 1) {
//						ret.put(kv[0], "");
//					} else {
//						ret.put(kv[0], kv[1]);
//					}
//				}
//			}
//
//			return ret;
//		} else {
//			return ret;
//		}
//	}
//
//	private static List<String> needMonitorThreadPool;
//
//	/**
//	 * 实现了带Instrumentation参数的premain()方法。
//	 *
//	 * @param args
//	 * @param inst
//	 * @throws Exception
//	 */
//	public static void premain(String args, Instrumentation inst) throws Exception {
//		//调用addTransformer()方法对启动时所有的类(应用层)进行拦截
//		kvs = splitCommaColonStringToKV(args);
//
//		//需要监控的线程池
//		needMonitorThreadPool = kvs.entrySet().stream().map((entry) -> entry.getKey()).collect(Collectors.toList());
//
//
//		inst.addTransformer(new DefineTransformer(), true);
//	}
//
//	static class DefineTransformer implements ClassFileTransformer {
//
//		/**
//		 * 如果事先知道哪些类需要修改，最简单的修改类方式如下：
//		 * <p>
//		 * 1、通过调用ClassPool.get()方法获取一个CtClass对象
//		 * 2、修改它
//		 * 3、调用CtClass对象的writeFile()或toBytecode()方法获取修改后的类文件
//		 *
//		 * @param loader
//		 * @param className
//		 * @param classBeingRedefined
//		 * @param protectionDomain
//		 * @param classfileBuffer
//		 * @return
//		 * @throws IllegalClassFormatException
//		 */
//		@Override
//		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//
//			System.out.println("在main方法前加载类 : " + className);
//
//			//要处理的类的函数
//			if (needMonitorThreadPool.contains(className)) {
//
//
//
//
//				System.out.println("transform_当前className:" + className);
//				String monitorMethod = kvs.get(className);
//				CtClass clazz = null;
//				System.out.println("transform_对线程池进行监控插桩");
//				try {
//					// 从ClassPool获得CtClass对象 (ClassPool对象是CtClass对象的容器，CtClass对象是类文件的抽象表示)
//					final ClassPool classPool = ClassPool.getDefault();
//
////					Loader cl = new Loader(classPool);
////					cl.addTranslator(classPool,new TranslatorUtil());
//
//					String classNameAfter = className.replaceAll("/", ".");
//					System.out.println("transform_classNameAfter:" + classNameAfter);
//					clazz = classPool.get(classNameAfter);
//
//
//
////					CtConstructor[] declaredConstructors = clazz.getDeclaredConstructors();
////
////					//获取目标类的所有内部类
////					CtClass[] nestedClasses = clazz.getNestedClasses();
//
////					handle(nestedClasses);
//
//
////					for (int i = 0; i <declaredConstructors.length; i++) {
////						CtConstructor declaredConstructor = declaredConstructors[i];
////
//////						CtConstructor cons = CtNewConstructor.make(c.getParameterTypes(),
//////								c.getExceptionTypes(), this);
////						declaredConstructor.setModifiers(Modifier.PUBLIC);
////						clazz.addConstructor(declaredConstructor);
////					}
//
////					Arrays.stream(nestedClasses).forEach((e) -> {
////						if (e.getName().equals("java.util.concurrent.CompletableFuture$AsyncRun")) {
////							e.setModifiers(Modifier.PUBLIC);
////
////						}
////					});
//
//
//
//
//					CtMethod monitorMethodCt = clazz.getDeclaredMethod(monitorMethod);
//					System.out.println("transform_monitorMethodCt" + monitorMethodCt);
//					//植入探针方法
//					String methodBody =
//							"{" +
//									"if ($2 == null) {throw new NullPointerException();}" +
//									"        java.util.concurrent.CompletableFuture/*<Void>*/ d = new java.util.concurrent.CompletableFuture/*<Void>*/();" +
//									"		 com.xzll.monitor.config.util.ForkJoinMonitorUtil.beforeExecute($2);" +
//									"        $1.execute(new java.util.concurrent.CompletableFuture$AsyncRun(d,$2));" +
//									"		 com.xzll.monitor.config.util.ForkJoinMonitorUtil.afterExecute($$);" +
//									" return d; " +
//									"}";
//					monitorMethodCt.setBody(methodBody);
//					//通过CtClass的toBytecode(); 方法来获取 被修改后的字节码
//					return clazz.toBytecode();
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					if (null != clazz) {
//						//调用CtClass对象的detach()方法后，对应class的其他方法将不能被调用。但是，你能够通过ClassPool的get()方法，
//						//重新创建一个代表对应类的CtClass对象。如果调用ClassPool的get()方法， ClassPool将重新读取一个类文件，并且重新创建一个CtClass对象，并通过get()方法返回
//						//如下所说：
//						//detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
//						clazz.detach();
//					}
//				}
//			}
//			return classfileBuffer;
//
////			if ("java/util/Date".equals(className)) {
////				CtClass clazz = null;
////				System.out.println("对date执行插桩");
////				try {
////					// 从ClassPool获得CtClass对象 (ClassPool对象是CtClass对象的容器，CtClass对象是类文件的抽象表示)
////					final ClassPool classPool = ClassPool.getDefault();
////					clazz = classPool.get("java.util.Date");
////					System.out.println("date的clazz" + clazz);
////					CtMethod getTime = clazz.getDeclaredMethod("getTime");
////					System.out.println("getTime方法" + getTime);
////
////					//(修改字节码) 这里对 java.util.Date.getTime() 方法进行了改写，在 return之前增加了一个 打印操作
////					String methodBody = "{" +
////							"long currentTime = getTimeImpl();" +
////							"System.out.println(\"使用agent探针对Date方法进行修改并打印，当前时间：\"+currentTime );" +
////							"return currentTime/1000;" +
////							"}";
////					getTime.setBody(methodBody);
////
////					//通过CtClass的toBytecode(); 方法来获取 被修改后的字节码
////					return clazz.toBytecode();
////				} catch (Exception ex) {
////					ex.printStackTrace();
////				} finally {
////					if (null != clazz) {
////						//调用CtClass对象的detach()方法后，对应class的其他方法将不能被调用。但是，你能够通过ClassPool的get()方法，
////						//重新创建一个代表对应类的CtClass对象。如果调用ClassPool的get()方法， ClassPool将重新读取一个类文件，并且重新创建一个CtClass对象，并通过get()方法返回
////						//如下所说：
////						//detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
////						clazz.detach();
////					}
////					System.out.println("对date插桩完成");
////				}
////			}
//
//
//		}
//
//
//		private void handle(CtClass[] nested) {
//			Arrays.stream(nested).forEach((e) -> {
//				if (e.getName().equals("java.util.concurrent.CompletableFuture$AsyncRun")) {
//					System.out.println("哈哈哈");
//				}
//			});
//		}
//
//
////		/**
////		 * 获取 agent jar 的路径，抄springboot的
////		 */
////		private String getAgentJarPath() {
////			ProtectionDomain protectionDomain = getClass().getProtectionDomain();
////			CodeSource codeSource = protectionDomain.getCodeSource();
////			URI location = null;
////			try {
////				location = (codeSource == null ? null : codeSource.getLocation().toURI());
////			} catch (URISyntaxException e) {
////				return null;
////			}
////			String path = (location == null ? null : location.getSchemeSpecificPart());
////			if (path == null) {
////				throw new IllegalStateException("Unable to determine code source");
////			}
////			File root = new File(path);
////			if (!root.exists()) {
////				throw new IllegalStateException(
////						"Unable to determine code source from " + root);
////			}
////			return path;
////		}
//
////		private void appendAgentNestedJars(ClassLoader classLoader) {
////			String agentJarPath = getAgentJarPath();
////			if (agentJarPath == null) return;
////
////			//LaunchedURLClassLoader 是属于 springboot-loader 的类，没有放到jar in jar里边，所以它是被AppClassLoader加载的
////			if (classLoader instanceof LaunchedURLClassLoader) {
////				LaunchedURLClassLoader launchedURLClassLoader = (LaunchedURLClassLoader) classLoader;
////				try {
////					Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
////					method.setAccessible(true);
////					//遍历 agent jar，处理所有对应目录下的jar包，使用 JarFileArchive 获取到的url才可以处理jar in jar
////					JarFileArchive jarFileArchive = new JarFileArchive(new File(agentJarPath));
////					List<Archive> archiveList = jarFileArchive.getNestedArchives(new Archive.EntryFilter() {
////						@Override
////						public boolean matches(Archive.Entry entry) {
////							if (entry.isDirectory()) {
////								return false;
////							}
////							return entry.getName().startsWith("BOOT-INF/lib/") && entry.getName().endsWith(".jar");
////						}
////					});
////					for (Archive archive : archiveList) {
////						method.invoke(launchedURLClassLoader, archive.getUrl());
////						System.out.println("add url to classloader. url:" + archive.getUrl());
////					}
////				} catch (Throwable t) {
////					t.printStackTrace();
////				}
////			}
////
////
////			System.out.println("trigger add urls to classLoader:" + classLoader.getClass().getName() + " agentJarPath:" + agentJarPath);
////
////		}
//
//
//	}
//}
