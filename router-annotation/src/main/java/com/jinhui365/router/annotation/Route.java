package com.jinhui365.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name:Route
 * Author:jmtian
 * Commemt:Annotation for route.
 * Date: 2017/9/21 16:40
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Route {
    /**
     * Route path.
     */
    String[] path();

    /**
     * The interceptors' name.
     */
    String[] interceptors() default {};
    /**
     * the conditions' path
     * It's a jsonString
     */
    String conditions();
}
