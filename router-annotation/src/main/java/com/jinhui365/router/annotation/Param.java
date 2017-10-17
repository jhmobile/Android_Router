package com.jinhui365.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name:Param
 * Author:jmtian
 * Commemt:Annotation for injected params.
 * Date: 2017/9/21 16:38
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Param {
    /**
     * Map param field with the specify key in bundle.
     */
    String name() default "";
}