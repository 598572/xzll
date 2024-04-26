package com.xzll.agent.config.advice;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.override.MybatisMapperMethod;
import com.xzll.agent.config.util.AESUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: 黄壮壮
 * @Date: 2024/4/20 10:37:11
 * @Description:
 *
 * 对baomidou 的 com.baomidou.mybatisplus.core.override.MybatisMapperMethod 类中的 execute方法进行增强，
 * 写时拦截到需要加密的字段（DO类上带有注解 @SensitiveData且字段上带有注解@EncryptTransaction的实体类中的字段 ） 进行aes加密，读时拦截需要解密（被@EncryptTransaction修饰）的字段用aes工具解密
 */
public class MysqlFieldEncryptAndDecryptAdvice {


	private static <T> T decryptRead(T resultObject) {
		try {
			if (Objects.nonNull(resultObject)) {
				if (resultObject instanceof ArrayList) {
					List resultList = (List) resultObject;
					if (!CollectionUtils.isEmpty(resultList) && existSensitiveData(resultList.get(0))) {
						for (Object result : resultList) {
							decrypt(result);
						}
					}
				} else {
					if (existSensitiveData(resultObject)) {
						decrypt(resultObject);
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return resultObject;
	}


	public static <T> T decrypt(T result) {
		//取出resultType的类
		Class<?> resultClass = result.getClass();
		Field[] declaredFields = resultClass.getDeclaredFields();
		for (Field field : declaredFields) {
			//取出所有被DecryptTransaction注解的字段 将其解密
			if (Objects.nonNull(field.getAnnotation(DecryptTransaction.class))) {
				field.setAccessible(true);
				try {
					Object object = field.get(result);
					String value = (String) object;
					if (StringUtils.isNotBlank(value)) {
						//对注解的字段进行逐一解密
						try {
							value = AESUtils.decrypt(value);
						} catch (Exception e) {
						}
						field.set(result, value);
					}
				} catch (Exception e) {
				}
			}
		}
		return result;
	}


	public static <T> T encrypt(T result) {
		//取出resultType的类
		Class<?> resultClass = result.getClass();
		Field[] declaredFields = resultClass.getDeclaredFields();
		for (Field field : declaredFields) {
			//取出所有被DecryptTransaction注解的字段 将其解密
			if (Objects.nonNull(field.getAnnotation(EncryptTransaction.class))) {
				field.setAccessible(true);
				try {
					Object object = field.get(result);
					String value = (String) object;
					if (StringUtils.isNotBlank(value)) {
						//对注解的字段进行逐一解密
						try {
							value = AESUtils.encrypt(value);
						} catch (Exception e) {
						}
						field.set(result, value);
					}
				} catch (Exception e) {
				}
			}
		}
		return result;
	}


	/**
	 * 加密写字段（如果存在敏感信息的话）
	 *
	 * @param args
	 */
	public static void encryptWrite(Object[] args) {
		try {
			for (Object object : args) {
				if (object instanceof List) {
					List resultList = (List) object;
					if (!CollectionUtils.isEmpty(resultList) && existSensitiveData(resultList.get(0))) {
						for (Object result : resultList) {
							encrypt(result);
						}
					}
				} else {
					if (existSensitiveData(object)) {
						encrypt(object);
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * 将原 MybatisMapperMethod 类的 execute方法进行增强
	 *
	 * @param mapperMethod
	 * @param sqlSession
	 * @param args
	 * @return
	 */
	public static Object executeAgent(MybatisMapperMethod mapperMethod, SqlSession sqlSession, Object[] args) {
		Object result;
		Object param;
		/**
		 * 由于源代码中，都是直接使用this来访问 成员变量/方法，这种方式在 MybatisMapperMethod 类肯定是可行的，
		 * 但是不在此类时通过this无法访问，所以就有了下边的：使用反射 获取私有成员变量/执行成员方法 ）
		 */

		//（使用反射获取 MybatisMapperMethod 类中的私有成员变量）
		MapperMethod.SqlCommand commandValue = (MapperMethod.SqlCommand) ReflectUtil.getFieldValue(mapperMethod, "command");
		MapperMethod.MethodSignature methodValue = (MapperMethod.MethodSignature) ReflectUtil.getFieldValue(mapperMethod, "method");
		switch (commandValue.getType()) {
			case INSERT:
				//原代码
				//param = this.method.convertArgsToSqlCommandParam(args);
				//result = this.rowCountResult(sqlSession.insert(commandValue.getName(), param));

				//改后代码
				MysqlFieldEncryptAndDecryptAdvice.encryptWrite(args);
				param = methodValue.convertArgsToSqlCommandParam(args);
				result = ReflectUtil.invoke(mapperMethod, "rowCountResult", sqlSession.insert(commandValue.getName(), param));
				break;
			case UPDATE:
				//param = this.method.convertArgsToSqlCommandParam(args);
				//result = this.rowCountResult(sqlSession.update(this.command.getName(), param));
				MysqlFieldEncryptAndDecryptAdvice.encryptWrite(args);
				param = methodValue.convertArgsToSqlCommandParam(args);
				result = ReflectUtil.invoke(mapperMethod, "rowCountResult", sqlSession.update(commandValue.getName(), param));
				break;
			case DELETE:
				//param = this.method.convertArgsToSqlCommandParam(args);
				//result = this.rowCountResult(sqlSession.delete(this.command.getName(), param));

				param = methodValue.convertArgsToSqlCommandParam(args);
				result = ReflectUtil.invoke(mapperMethod, "rowCountResult", sqlSession.delete(commandValue.getName(), param));
				break;
			case SELECT:
				if (methodValue.returnsVoid() && methodValue.hasResultHandler()) {
					//this.executeWithResultHandler(sqlSession, args);
					ReflectUtil.invoke(mapperMethod, "executeWithResultHandler", sqlSession, args);
					result = null;
				} else if (methodValue.returnsMany()) {
					//result = this.executeForMany(sqlSession, args);
					result = ReflectUtil.invoke(mapperMethod, "executeForMany", sqlSession, args);
				} else if (methodValue.returnsMap()) {
					//result = this.executeForMap(sqlSession, args);
					result = ReflectUtil.invoke(mapperMethod, "executeForMap", sqlSession, args);

				} else if (methodValue.returnsCursor()) {
					//result = this.executeForCursor(sqlSession, args);
					result = ReflectUtil.invoke(mapperMethod, "executeForCursor", sqlSession, args);
				} else if (IPage.class.isAssignableFrom(methodValue.getReturnType())) {
					//result = this.executeForIPage(sqlSession, args);
					result = ReflectUtil.invoke(mapperMethod, "executeForIPage", sqlSession, args);
				} else {
					param = methodValue.convertArgsToSqlCommandParam(args);
					result = sqlSession.selectOne(commandValue.getName(), param);
					if (methodValue.returnsOptional() && (result == null || !methodValue.getReturnType().equals(result.getClass()))) {
						result = Optional.ofNullable(result);
					}
				}
				break;
			case FLUSH:
				result = sqlSession.flushStatements();
				break;
			default:
				throw new BindingException("Unknown execution method for: " + commandValue.getName());
		}

		if (result == null && methodValue.getReturnType().isPrimitive() && !methodValue.returnsVoid()) {
			throw new BindingException("Mapper method '" + commandValue.getName() + " attempted to return null from a method with a primitive return type (" + methodValue.getReturnType() + ").");
		} else {
			if (Objects.equals(commandValue.getType(), SqlCommandType.SELECT) && result != null) {
				result = MysqlFieldEncryptAndDecryptAdvice.decryptRead(result);
			}
			return result;
		}
	}


	/**
	 * 是否存在敏感字段 true存在 false不存在
	 *
	 * @param object
	 * @return
	 */
	private static boolean existSensitiveData(Object object) {
		Class<?> objectClass = object.getClass();
		SensitiveData sensitiveData = AnnotationUtil.getAnnotation(objectClass, SensitiveData.class);
		return Objects.nonNull(sensitiveData);
	}


//	public static void main(String[] args) throws IllegalAccessException {
//		ArrayList objects = new ArrayList<>();
//		AdminUserDTO2 adminUserDTO = new AdminUserDTO2();
//		adminUserDTO.setFullnameEncrypt("CkBIbjRqj2Syn2vZvTADKHHdUeIfgascXWL6utE962k=");
//		adminUserDTO.setFullname("CkBIbjRqj2Syn2vZvTADKKo6mGCb88SCdf4W0oB6W/s=");
//		adminUserDTO.setUsername("呵呵");
//		adminUserDTO.setPassword("123");
//		adminUserDTO.setSex(1);
//		objects.add(adminUserDTO);
//
//		AdminUserDTO2 adminUserDTO2 = new AdminUserDTO2();
//		adminUserDTO2.setFullnameEncrypt("CkBIbjRqj2Syn2vZvTADKKo6mGCb88SCdf4W0oB6W/s=");
//		adminUserDTO2.setFullname("CkBIbjRqj2Syn2vZvTADKHHdUeIfgascXWL6utE962k=");
//		adminUserDTO2.setUsername("呵呵2");
//		adminUserDTO2.setPassword("2");
//		adminUserDTO2.setSex(0);
//		objects.add(adminUserDTO2);
//
//		objects.forEach(x -> {
//			Object decrypt1 = decrypt(x);
//			System.out.println(decrypt1);
//		});
//
//
//	}




}
