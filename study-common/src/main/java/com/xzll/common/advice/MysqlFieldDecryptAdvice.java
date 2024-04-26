package com.xzll.common.advice;


import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.override.MybatisMapperMethod;

import com.xzll.common.util.AESUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class MysqlFieldDecryptAdvice {



	public static <T> T decryptSelect(T resultObject) {
		try {
			if (Objects.nonNull(resultObject)){
				if (resultObject instanceof ArrayList) {
					ArrayList resultList = (ArrayList) resultObject;
					if (!CollectionUtils.isEmpty(resultList)) {
						for (Object result : resultList) {
							decrypt(result);
						}
					}
				} else {
					decrypt(resultObject);
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



	public Object executeAgent(MybatisMapperMethod mapperMethod, SqlSession sqlSession, Object[] args) {
		Object result;
		Object param;
		MapperMethod.SqlCommand commandValue = (MapperMethod.SqlCommand) ReflectUtil.getFieldValue(mapperMethod.getClass(), "command");
		MapperMethod.MethodSignature methodValue = (MapperMethod.MethodSignature) ReflectUtil.getFieldValue(mapperMethod.getClass(), "method");

		switch(commandValue.getType()) {
			case INSERT:
				param = methodValue.convertArgsToSqlCommandParam(args);
//				result = this.rowCountResult(sqlSession.insert(commandValue.getName(), param));
				result = ReflectUtil.invoke(mapperMethod, "rowCountResult", sqlSession.insert(commandValue.getName(), param));
				break;
			case UPDATE:
				param = methodValue.convertArgsToSqlCommandParam(args);
//				result = this.rowCountResult(sqlSession.update(this.command.getName(), param));
				result = ReflectUtil.invoke(mapperMethod, "rowCountResult", sqlSession.update(commandValue.getName(), param));
				break;
			case DELETE:
				param = methodValue.convertArgsToSqlCommandParam(args);
//				result = this.rowCountResult(sqlSession.delete(this.command.getName(), param));
				result = ReflectUtil.invoke(mapperMethod, "rowCountResult", sqlSession.delete(commandValue.getName(), param));
				break;
			case SELECT:
				if (methodValue.returnsVoid() && methodValue.hasResultHandler()) {
//					this.executeWithResultHandler(sqlSession, args);
					ReflectUtil.invoke(mapperMethod, "executeWithResultHandler", sqlSession,args);
					result = null;
				} else if (methodValue.returnsMany()) {
//					result = this.executeForMany(sqlSession, args);
					result = ReflectUtil.invoke(mapperMethod, "executeForMany", sqlSession, args);
				} else if (methodValue.returnsMap()) {
//					result = this.executeForMap(sqlSession, args);
					result = ReflectUtil.invoke(mapperMethod, "executeForMap", sqlSession, args);

				} else if (methodValue.returnsCursor()) {
//					result = this.executeForCursor(sqlSession, args);
					result = ReflectUtil.invoke(mapperMethod, "executeForCursor", sqlSession, args);

				} else if (IPage.class.isAssignableFrom(methodValue.getReturnType())) {
//					result = this.executeForIPage(sqlSession, args);
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
			return result;
		}
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
