package com.deepinnet.rainsfall.biz.annotation;

import com.deepinnet.rainsfall.biz.enums.*;

import java.lang.annotation.*;

/**
 * @author amos wong
 * @create 2024/7/1 16:48
 * @Description 开启全局分布式事务的注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalTransactionStart {

    /**
     * @see TransactionInvokeTypeEnum
     */
    String invokeType() default "system";

    /**
     * @see TransactionTypeEnum
     */
    String type();

    /**
     * @see TransactionExecuteTypeEnum
     */
    String executeType() default "retry";

    String rollbackInterface() default "";

    String rollbackMethod() default "";

    String rollbackParameter() default "";
}
