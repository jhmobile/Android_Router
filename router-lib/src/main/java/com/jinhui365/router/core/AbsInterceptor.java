package com.jinhui365.router.core;

import com.jinhui365.router.route.RouteContext;

import java.util.Map;

/**
 * Author:jmtian
 * Date: 2017/8/14 17:15
 * Commemt:
 * 拦截器的默认实现，子类实现对应的生命周期函数
 * 拦截器封装包括：
 * 验证前判断
 * 验证状态
 * 验证后实现
 */

public abstract class AbsInterceptor implements IInterceptCallBack {

    protected RouteContext routeContext;
    protected Map<String, Object> options;//condition需要的配置项


    private InterceptorState state = InterceptorState.STATE_DEFAULT;//Interceptor验证结果
    private InterceptorLifeCycle lifeState = InterceptorLifeCycle.DEFAULT;//Interceptor验证最后走的生命周期方法

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public InterceptorState getState() {
        return state;
    }

    public void setState(InterceptorState state) {
        this.state = state;
    }

    public InterceptorLifeCycle getLifeState() {
        return lifeState;
    }

    public void setLifeState(InterceptorLifeCycle lifeState) {
        this.lifeState = lifeState;
    }


    public AbsInterceptor(RouteContext routeContext, Map<String, Object> params, Map<String, Object> options) {
        this.routeContext = routeContext;
        this.params = params;
        this.options = options;
    }

    /**
     * Author:jmtian
     * Date: 2017/8/14 17:18
     * description: 初始验证
     */
    public void onIntercept() {
        lifeState = InterceptorLifeCycle.START;
        checkoutState(this);
    }

    /**
     * Author:jmtian
     * Date: 2017/8/14 17:18
     * description: 验证完成回调
     */
    public void onComplete() {
        lifeState = InterceptorLifeCycle.AFTER;
        checkoutState(this);
    }

    /**
     * Author:jmtian
     * Date: 2017/8/24 12:58
     * description: 验证打断
     */
    public void onBreak() {
        lifeState = InterceptorLifeCycle.BREAK;
        onEndBreak();
    }

    /**
     * 一个前置条件的验证
     * 0 通过
     * 1 失败
     * 2 pending
     */
    protected abstract void verifiy(IInterceptCallBack stateCallBack);

    /**
     * start checkout success
     */
    protected void interceptOnVerifyPass (){
        routeContext.next();
    }

    /**
     * start checkout pending
     */
    protected void interceptOnVerifying() {
    }

    /**
     * start checkout fail
     */
    protected abstract void interceptOnVerifyNotPass();

    /**
     * after checkout success
     */
    protected void completeOnVerifyPass() {
        routeContext.next();
    }

    /**
     * after checkout pending
     */
    protected void onPendingAfter() {
    }

    /**
     * after checkout fail
     */
    protected void onFailAfter() {

    }

    /**
     * break
     */
    protected void onEndBreak() {

    }

    @Override
    public void stateCallBack(InterceptorState state) {
        this.state = state;//记录验证状态
        if (state.equals(STATE_SUCCESS) && lifeState.equals(InterceptorLifeCycle.START)) {
            onSuccessBefore();
        } else if (state.equals(STATE_SUCCESS) && lifeState.equals(InterceptorLifeCycle.AFTER)) {
            onSuccessAfter();
        } else if (state.equals(STATE_FAIL) && lifeState.equals(InterceptorLifeCycle.START)) {
            onFailBefore();
        } else if (state.equals(STATE_FAIL) && lifeState.equals(InterceptorLifeCycle.AFTER)) {
            onFailAfter();
        } else if (state.equals(STATE_PENDING) && lifeState.equals(InterceptorLifeCycle.START)) {
            onPendingBefore();
        } else if (state.equals(STATE_PENDING) && lifeState.equals(InterceptorLifeCycle.AFTER)) {
            onPendingAfter();
        } else {
            onEndBreak();
        }

    }
}
