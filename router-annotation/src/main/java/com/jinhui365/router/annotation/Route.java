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
     * the conditions' path
     * It's a jsonString
     * et:
     * {"type": "spirit","rateType": "unfix"}
     */
    String conditions() default "";

    /**
     * the path's group
     */
    Class<?> group() default Class.class;

    /**
     * The interceptors's name array.
     */
    String[] interceptors() default {};

    /**
     * the route of params
     * it's a jsonString
     * et:
     * {"MAIN_TAB":"1"}
     */
    String params() default "";

    /**
     * one router of context's class
     */
    String injectContext() default "routeContext";
}
