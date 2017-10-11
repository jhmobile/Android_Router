package com.jinhui365.router.interceptor;

/**
 * Name:InterceptorLifeEnum
 * Author:jmtian
 * Commemt:Interceptor走过的生命周期
 * Date: 2017/8/24 13:38
 */


public enum InterceptorLifeEnum {
    DEFAULT,
    START,
    AFTER,
    BREAK;

    InterceptorLifeEnum() {
    }
}
