package com.xzll.agent.config.advice;

import java.lang.annotation.*;


/**
 * 解密注解
 */
@Inherited
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecryptTransaction {
    String value() default "";
}
