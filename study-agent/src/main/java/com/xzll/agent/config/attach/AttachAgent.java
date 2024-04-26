package com.xzll.agent.config.attach;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @Author: 黄壮壮
 * @Date: 2024/4/23 10:37:11
 * @Description:
 */
public class AttachAgent {


	public static void agentmain(String args, Instrumentation inst) throws Exception {
		System.out.println("agentmain_开始动态加载jar包");
		inst.addTransformer(new ByAttachLoadAgentTransformer(), true);

		//指定需要转换的类，动态加载时这一步必不可少。否则你修改了字节码但是没进行显示转换 jvm是加载不到的，静态加载的话不需要这一步，因为是在类加载时，而动态加载是在jvm运行时
		inst.retransformClasses(java.lang.Integer.class);
		System.out.println("agentmain_结束动态加载jar包");
		System.out.println();
	}

	static class ByAttachLoadAgentTransformer implements ClassFileTransformer {
		@Override
		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			//操作Integer类
			if ("java/lang/Integer".equals(className)) {
				CtClass clazz = null;
				System.out.println("动态加载agent jar -> 动态插桩Integer.valueOf方法【开始】，" + "当前类：" + className);
				try {
					// 从ClassPool获得CtClass对象 (ClassPool对象是CtClass对象的容器，CtClass对象是类文件的抽象表示)
					final ClassPool classPool = ClassPool.getDefault();
					//不配这个 将找不到 java.lang.Integer 类
					classPool.insertClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
					clazz = classPool.get("java.lang.Integer");
					//获取到Integer的valueOf(int i) 方法。注意此处需要指定形参是int的 因为有多个valueOf方法
					CtMethod valueOf = clazz.getDeclaredMethod("valueOf", new CtClass[]{CtClass.intType});
					//(修改字节码) 这里对 java.lang.Integer的valueOf(int i)进行改写，将以下代码：
					/**
					 *     public static Integer valueOf(int i) {
					 *         if (i >= IntegerCache.low && i <= IntegerCache.high)
					 *             return IntegerCache.cache[i + (-IntegerCache.low)];
					 *         return new Integer(i);
					 *     }
					 *
					 *     改为：
					 *     public static Integer valueOf(int i) {
					 *         System.out.println("修改valueOf方法的实现，将-128-127的int值都装箱，这样的话 只要被valueOf包装过。那么去比较时就都是 false 了，因为是不同的对象");
					 *         return new Integer(i);
					 *     }
					 */

					//在此处修改valueOf方法的实现，将-128-127的int值都装箱，这样的话只要被valueOf包装过。那么去比较时就都是 false 了，因为是不同的对象
					String methodBody = "{" +
											"return new Integer($1);" +
										"}";
					valueOf.setBody(methodBody);
					//通过CtClass的toBytecode(); 方法来获取 被修改后的字节码
					return clazz.toBytecode();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (null != clazz) {
						//调用CtClass对象的detach()方法后，对应class的其他方法将不能被调用。但是，你能够通过ClassPool的get()方法，
						//重新创建一个代表对应类的CtClass对象。如果调用ClassPool的get()方法， ClassPool将重新读取一个类文件，并且重新创建一个CtClass对象，并通过get()方法返回
						//如下所说：
						//detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
						clazz.detach();
					}
					System.out.println("动态加载agent jar -> 动态插桩Integer.valueOf方法【结束】，" + "当前类：" + className);
				}
			}
			return classfileBuffer;
		}
	}

}
