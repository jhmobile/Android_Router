package com.jinhui365.router.interceptor;

/**
 * Name:InterceptorEnum
 * Author:jmtian
 * Commemt:Interceptor验证结果
 * Date: 2017/8/21 14:17
 */


public enum InterceptorStateEnum {
    DEFAULT,//未验证
    SUCCESS,//验证成功
    FAIL,//验证失败
    PENDING,//验证进行中

    InterceptorStateEnum() {

    }
}
