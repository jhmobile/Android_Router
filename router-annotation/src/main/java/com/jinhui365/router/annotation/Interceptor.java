package com.jinhui365.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name:Interceptor
 * Author:jmtian
 * Commemt:Annotation for injected Interceptor.
 * Date: 2017/9/21 16:39
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Interceptor {
    /**
     * Interceptor's name.
     */
    String name() default "";

    /**
     * Interceptor's params.
     * It's a jsonString
     * et:
     * {"state":"-1"}
     */
    String params() default "";

    /**
     * Interceptor's options.
     * It's a jsonString
     * et:
     * {"url":"https://jinhui365.cn"}
     */
    String options() default "";
}
