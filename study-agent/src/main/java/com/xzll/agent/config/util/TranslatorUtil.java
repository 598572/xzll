//package com.xzll.agent.config.util;
//
//import javassist.*;
//
//import java.util.Arrays;
//
///**
// * @Author: hzz
// * @Date: 2023/3/6 17:08:33
// * @Description: 修改类定义
// */
//public class TranslatorUtil implements Translator {
//
//
//	@Override
//	public void start(ClassPool classPool) throws NotFoundException, CannotCompileException {
//
//	}
//
//	@Override
//	public void onLoad(ClassPool classPool, String clasName) throws NotFoundException, CannotCompileException {
//
//
//		if (clasName.equals("java/util/concurrent/CompletableFuture")){
//			CtClass clazz = null;
//
//			String classNameAfter = clasName.replaceAll("/", ".");
//
//			System.out.println("onLoad_classNameAfter:" + classNameAfter);
//			clazz = classPool.get(classNameAfter);
//
//			//获取目标类的所有内部类
//			CtClass[] nestedClasses = clazz.getNestedClasses();
//
//			Arrays.stream(nestedClasses).forEach((e) -> {
//				if (e.getName().equals("java.util.concurrent.CompletableFuture$AsyncRun")) {
//					e.setModifiers(Modifier.PUBLIC);
//				}
//			});
//
//		}
//
//
//
//
//	}
//}
