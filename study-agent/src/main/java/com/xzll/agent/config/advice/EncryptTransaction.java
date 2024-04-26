package com.xzll.agent.config.advice;

import java.lang.annotation.*;


/**
 * 加密注解
 */
@Inherited
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptTransaction {
}
