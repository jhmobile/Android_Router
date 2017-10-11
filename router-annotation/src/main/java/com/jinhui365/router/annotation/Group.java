package com.jinhui365.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 1 on 2017/10/9.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Group {
    String name() default "";
}
