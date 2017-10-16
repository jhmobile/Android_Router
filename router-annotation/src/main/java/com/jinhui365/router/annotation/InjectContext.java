package com.jinhui365.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name:InjectContext
 * Author:jmtian
 * Commemt:Annotation for one router of context's class
 * Date: 2017/10/16 9:47
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface InjectContext {
    /**
     * context's name
     */
    String name() default "";

    /**
     * context's options
     * It's a jsonString
     * et:
     * {"detailVO":""}
     */
    String options() default "";
}
