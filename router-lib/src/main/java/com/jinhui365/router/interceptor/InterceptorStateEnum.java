package com.jinhui365.router.interceptor;

/**
 * Name:InterceptorEnum
 * Author:jmtian
 * Commemt:Interceptor验证结果
 * Date: 2017/8/21 14:17
 */


public enum InterceptorStateEnum {
    STATE_DEFAULT,//未验证
    STATE_SUCCESS,//验证成功
    STATE_FAIL,//验证失败
    STATE_PENDING,//验证进行中

    InterceptorStateEnum() {

    }
}
