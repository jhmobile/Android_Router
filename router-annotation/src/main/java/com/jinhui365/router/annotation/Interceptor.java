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
     * Interceptor name.
     */
    String value();
}
