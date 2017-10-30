package com.jinhui365.router.core;

import java.util.Map;

import static com.jinhui365.router.core.InterceptorState.PENDING;
import static com.jinhui365.router.core.InterceptorState.UNVERIFIED;
import static com.jinhui365.router.core.InterceptorState.VERIFIED;

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
    protected Map<String, Object> options;//Interceptor需要的配置项


    private InterceptorState state;//Interceptor验证结果
    private InterceptorLifeCycle lifeState = InterceptorLifeCycle.DEFAULT;//Interceptor验证最后走的生命周期方法

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


    public AbsInterceptor(RouteContext routeContext, Map<String, Object> options) {
        this.routeContext = routeContext;
        this.options = options;
    }

    /**
     * Author:jmtian
     * Date: 2017/8/14 17:18
     * description: 初始验证
     */
    public void onIntercept() {
        lifeState = InterceptorLifeCycle.INTERCEPT;
        verify(this);
    }

    /**
     * Author:jmtian
     * Date: 2017/8/14 17:18
     * description: 验证完成回调
     */
    public void onComplete() {
        lifeState = InterceptorLifeCycle.COMPLETE;
        verify(this);
    }

    /**
     * Author:jmtian
     * Date: 2017/8/24 12:58
     * description: 验证打断
     */
    public void onInterrupt() {
        lifeState = InterceptorLifeCycle.INTERRUPT;
        interrupt();
    }

    /**
     * 一个前置条件的验证
     * 0 通过
     * 1 失败
     * 2 pending
     */
    protected abstract void verify(IInterceptCallBack stateCallBack);

    /**
     * start checkout success
     */
    protected void interceptOnVerifyPass() {
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
    protected void completeOnVerifying() {
    }

    /**
     * after checkout fail
     */
    protected void completeOnVerifyNotPass() {

    }

    /**
     * break
     */
    protected void interrupt() {

    }

    @Override
    public void stateCallBack(InterceptorState state) {
        this.state = state;//记录验证状态
        if (state.equals(VERIFIED) && lifeState.equals(InterceptorLifeCycle.INTERCEPT)) {
            interceptOnVerifyPass();
        } else if (state.equals(VERIFIED) && lifeState.equals(InterceptorLifeCycle.COMPLETE)) {
            completeOnVerifyPass();
        } else if (state.equals(UNVERIFIED) && lifeState.equals(InterceptorLifeCycle.INTERCEPT)) {
            interceptOnVerifyNotPass();
        } else if (state.equals(UNVERIFIED) && lifeState.equals(InterceptorLifeCycle.COMPLETE)) {
            completeOnVerifyNotPass();
        } else if (state.equals(PENDING) && lifeState.equals(InterceptorLifeCycle.INTERCEPT)) {
            interceptOnVerifying();
        } else if (state.equals(PENDING) && lifeState.equals(InterceptorLifeCycle.COMPLETE)) {
            completeOnVerifying();
        } else {
            interrupt();
        }

    }
}
