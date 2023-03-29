//package com.xzll.agent.config;
//
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//
//import java.lang.instrument.ClassFileTransformer;
//import java.lang.instrument.IllegalClassFormatException;
//import java.lang.instrument.Instrumentation;
//import java.security.ProtectionDomain;
//
///**
// * @Author: hzz
// * @Date: 2023/3/3 09:15:21
// * @Description: MYSQL加解密 agent
// */
//public class MysqlCryptAgentTest {
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
//		inst.addTransformer(new DefineTransformer(), true);
//	}
//
//	static class DefineTransformer implements ClassFileTransformer {
//
//		/**
//		 * 如果事先知道哪些类需要修改，最简单的修改类方式如下：
//		 *
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
//			//
//			if ("java/util/Date".equals(className)) {
//				CtClass clazz = null;
//				System.out.println("对date执行插桩");
//				try {
//					// 从ClassPool获得CtClass对象 (ClassPool对象是CtClass对象的容器，CtClass对象是类文件的抽象表示)
//					final ClassPool classPool = ClassPool.getDefault();
//					clazz = classPool.get("java.util.Date");
//					System.out.println("date的clazz" + clazz);
//					CtMethod getTime = clazz.getDeclaredMethod("getTime");
//					System.out.println("getTime方法" + getTime);
//
//					//(修改字节码) 这里对 java.util.Date.getTime() 方法进行了改写，在 return之前增加了一个 打印操作
//					String methodBody = "{" +
//							"long currentTime = getTimeImpl();" +
//							"System.out.println(\"使用agent探针对Date方法进行修改并打印，当前时间：\"+currentTime );" +
//							"return currentTime/1000;" +
//							"}";
//					getTime.setBody(methodBody);
//
//					//通过CtClass的toBytecode(); 方法来获取 被修改后的字节码
//					return clazz.toBytecode();
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				} finally {
//					if (null != clazz) {
//						//调用CtClass对象的detach()方法后，对应class的其他方法将不能被调用。但是，你能够通过ClassPool的get()方法，
//						//重新创建一个代表对应类的CtClass对象。如果调用ClassPool的get()方法， ClassPool将重新读取一个类文件，并且重新创建一个CtClass对象，并通过get()方法返回
//						//如下所说：
//						//detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
//						clazz.detach();
//					}
//					System.out.println("对date插桩完成");
//				}
//			}
//			return classfileBuffer;
//		}
//	}
//}
