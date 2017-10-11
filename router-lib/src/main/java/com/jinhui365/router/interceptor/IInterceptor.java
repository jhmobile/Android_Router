package com.jinhui365.router.interceptor;

/**
 * Name:IInterceptor
 * Author:jmtian
 * Commemt://拦截器API
 * Date: 2017/10/11 15:07
 */


public interface IInterceptor {

    /**
     * interceptor  validate start
     */
    void onStart();

    /**
     * interceptor  validate of after
     */
    void onAfter();

    /**
     * interceptor  validate break
     */
    void onBreak();

    /**
     * interceptor  checkout
     */
    void checkoutState(InterceptorStateCallBack stateCallBack);

    void onSuccessBefore();

    void onPendingBefore();

    void onFailBefore();

    void onSuccessAfter();

    void onPendingAfter();

    void onFailAfter();

    void onEndBreak();
}
