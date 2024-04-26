//package com.xzll.test.service.impl;
//
//
//import com.xzll.agent.config.advice.DecryptTransaction;
//import com.xzll.agent.config.util.AESUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.util.CollectionUtils;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class DecryptImpl {
//
//
//	/**
//	 * 解密
//	 *
//	 * @param result resultType的实例
//	 * @param <T>
//	 * @return
//	 * @throws IllegalAccessException
//	 */
////    public static <T> T decrypt(T result) throws IllegalAccessException {
////        //取出resultType的类
////        Class<?> resultClass = result.getClass();
////        Field[] declaredFields = resultClass.getDeclaredFields();
////        for (Field oldField : declaredFields) {
////            // 原字段
////            DecryptBeforeTransaction decryptPrimary = oldField.getAnnotation(DecryptBeforeTransaction.class);
////            if (!Objects.isNull(decryptPrimary)) {
////                String fieParam = decryptPrimary.value();
////                oldField.setAccessible(true);
////                for (Field field : declaredFields) {
////                    //取出所有被DecryptTransaction注解的字段
////                    DecryptTransaction decryptTransaction = field.getAnnotation(DecryptTransaction.class);
////                    if (!Objects.isNull(decryptTransaction)) {
////                        String param = decryptTransaction.value();
////                        field.setAccessible(true);
////                        Object object = field.get(result);
////                        if (param.equals(fieParam)) {
////                            try {
////                                String value = (String) object;
////                                if(!ObjectUtils.isEmpty(value)){
////                                    //对注解的字段进行逐一解密
////                                    try {
////                                        value = AESUtils.decrypt(value);
////                                    }catch (Exception e){
////
////                                    }
////                                    oldField.set(result, value);
////                                    field.set(result, value);
////                                }
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
////                            break;
////                        }
////                    }
////                }
////            }
////        }
////        return result;
////    }
//	public static <T> T decrypt(T result) {
//		//取出resultType的类
//		Class<?> resultClass = result.getClass();
//		Field[] declaredFields = resultClass.getDeclaredFields();
//		for (Field field : declaredFields) {
//			//取出所有被DecryptTransaction注解的字段 将其解密
//			if (Objects.nonNull(field.getAnnotation(DecryptTransaction.class))) {
//				field.setAccessible(true);
//				try {
//					Object object = field.get(result);
//					String value = (String) object;
//					if (StringUtils.isNotBlank(value)) {
//						//对注解的字段进行逐一解密
//						try {
//							value = AESUtils.decrypt(value);
//						} catch (Exception e) {
//						}
//						field.set(result, value);
//					}
//				} catch (Exception e) {
//				}
//			}
//		}
//		return result;
//	}
//
//	public static <T> T decryptSelect(T resultObject) {
//		//基于selectList
//		if (resultObject instanceof ArrayList) {
//			ArrayList resultList = (ArrayList) resultObject;
//			if (!CollectionUtils.isEmpty(resultList)) {
//				for (Object result : resultList) {
//					decrypt(result);
//				}
//			}
//		} else {
//			decrypt(resultObject);
//		}
//		return resultObject;
//	}
//
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
//}
