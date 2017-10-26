package com.jinhui365.router.core;

/**
 * Name:InterceptorLifeCycle
 * Author:jmtian
 * Commemt:Interceptor走过的生命周期
 * Date: 2017/8/24 13:38
 */


public enum InterceptorLifeCycle {
    DEFAULT,
    START,
    AFTER,
    BREAK;

    InterceptorLifeCycle() {
    }
}
