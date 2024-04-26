package com.xzll.agent.config;

import com.xzll.agent.config.advice.MysqlFieldEncryptAndDecryptAdvice;
import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @Author: hzz
 * @Date: 2023/3/3 09:15:21
 * @Description: MYSQL加解密 agent
 */
public class MysqlFieldCryptByExecuteBodyAgent {

	/**
	 * 实现了带Instrumentation参数的premain()方法。
	 */
	public static void premain(String args, Instrumentation inst) throws Exception {
		//调用addTransformer()方法对启动时所有的类(应用层)进行拦截
		inst.addTransformer(new MysqlReadWriteTransformer(), true);
	}

	static class MysqlReadWriteTransformer implements ClassFileTransformer {
		/**
		 * 如果事先知道哪些类需要修改，最简单的修改类方式如下：
		 * <p>
		 * 1、通过调用ClassPool.get()方法获取一个CtClass对象
		 * 2、修改它
		 * 3、调用CtClass对象的writeFile()或toBytecode()方法获取修改后的类文件
		 **/
		@Override
		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			if ("com/baomidou/mybatisplus/core/override/MybatisMapperMethod".equals(className)) {
				CtClass clazz = null;
				System.out.println("对MybatisMapperMethod执行插桩实现读解密,写加密");
				try {
					// 从ClassPool获得CtClass对象 (ClassPool对象是CtClass对象的容器，CtClass对象是类文件的抽象表示)
					final ClassPool classPool = ClassPool.getDefault();
					classPool.insertClassPath(new ClassClassPath(MysqlFieldEncryptAndDecryptAdvice.class));
					clazz = classPool.get("com.baomidou.mybatisplus.core.override.MybatisMapperMethod");
					CtMethod getTime = clazz.getDeclaredMethod("execute");
					String body = "{\n" +
										"return com.xzll.agent.config.advice.MysqlFieldEncryptAndDecryptAdvice.executeAgent($0,$1,$2);\n" +
							      "}\n";
					getTime.setBody(body);
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
//						clazz.detach();
					}
					System.out.println("对sqlSession插桩完成");
				}
			}

//			if ("com/baomidou/mybatisplus/core/override/MybatisMapperMethod".equals(className)) {
//				CtClass clazz = null;
//				System.out.println("对MybatisMapperMethod执行插桩");
//				try {
//					// 从ClassPool获得CtClass对象 (ClassPool对象是CtClass对象的容器，CtClass对象是类文件的抽象表示)
//					final ClassPool classPool = ClassPool.getDefault();
//					clazz = classPool.get("com.baomidou.mybatisplus.core.override.MybatisMapperMethod");
//					CtMethod getTime = clazz.getDeclaredMethod("execute");
//					String s = "{" +
//									"if(java.util.Objects.equals($0.command.getType(), org.apache.ibatis.mapping.SqlCommandType.SELECT)){" +
//										"System.out.println(\"插桩前嘿嘿\");" +
//										"com.xzll.agent.config.advice.MysqlFieldDecryptAdvice.decryptSelect($_);\n" +
//										"System.out.println(\"插桩后嘿嘿\" );" +
//									"} " +
//								"}\n";
//					getTime.insertAfter(s);
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
//					System.out.println("对sqlSession插桩完成");
//				}
//			}
			return classfileBuffer;
		}
	}

//	public static void main(String[] args) {
//		CtNewMethod.make()
//	}


	//不能这么写 否则很难受
//	String s = "{\n" +
//			"System.out.println(\"插桩前嘿嘿\");" +
//			"        java.lang.Object result;\n" +
//			"        java.lang.Object param;\n" +
//			"        switch($0.command.getType()) {\n" +
//			"        case org.apache.ibatis.mapping.SqlCommandType.INSERT:\n" +
//			"            param = $0.method.convertArgsToSqlCommandParam($2);\n" +
//			"            result = $0.rowCountResult($1.insert($0.command.getName(), param));\n" +
//			"            break;\n" +
//			"        case org.apache.ibatis.mapping.SqlCommandType.UPDATE:\n" +
//			"            param = $0.method.convertArgsToSqlCommandParam($2);\n" +
//			"            result = $0.rowCountResult($1.update($0.command.getName(), param));\n" +
//			"            break;\n" +
//			"        case org.apache.ibatis.mapping.SqlCommandType.DELETE:\n" +
//			"            param = $0.method.convertArgsToSqlCommandParam($2);\n" +
//			"            result = $0.rowCountResult($1.delete($0.command.getName(), param));\n" +
//			"            break;\n" +
//			"        case org.apache.ibatis.mapping.SqlCommandType.SELECT:\n" +
//			"            if ($0.method.returnsVoid() && $0.method.hasResultHandler()) {\n" +
//			"                $0.executeWithResultHandler($1, $2);\n" +
//			"                result = null;\n" +
//			"            } else if ($0.method.returnsMany()) {\n" +
//			"                result = $0.executeForMany($1, $2);\n" +
//			"            } else if ($0.method.returnsMap()) {\n" +
//			"                result = $0.executeForMap($1, $2);\n" +
//			"            } else if ($0.method.returnsCursor()) {\n" +
//			"                result = $0.executeForCursor($1, $2);\n" +
//			"            } else if (com.baomidou.mybatisplus.core.metadata.IPage.class.isAssignableFrom($0.method.getReturnType())) {\n" +
//			"                result = $0.executeForIPage($1, $2);\n" +
//			"            } else {\n" +
//			"                param = $0.method.convertArgsToSqlCommandParam($2);\n" +
//			"                result = $1.selectOne($0.command.getName(), param);\n" +
//			"                if ($0.method.returnsOptional() && (result == null || !$0.method.getReturnType().equals(result.getClass()))) {\n" +
//			"                    result = java.util.Optional.ofNullable(result);\n" +
//			"                }\n" +
//			"            }\n" +
//			"            break;\n" +
//			"        case org.apache.ibatis.mapping.SqlCommandType.FLUSH:\n" +
//			"            result = $1.flushStatements();\n" +
//			"            break;\n" +
//			"        default:\n" +
//			"            throw new org.apache.ibatis.binding.BindingException(\"Unknown execution method for: \" + $0.command.getName());\n" +
//			"        }\n" +
//			"\n" +
//			"        if (result == null && $0.method.getReturnType().isPrimitive() && !$0.method.returnsVoid()) {\n" +
//			"            throw new org.apache.ibatis.binding.BindingException(\"Mapper method '\" + $0.command.getName() + \" attempted to return null from a method with a primitive return type (\" + $0.method.getReturnType() + \").\");\n" +
//			"        } else {\n" +
//			"            return result;\n" +
//			"        }\n" +
//			"    }";

}
