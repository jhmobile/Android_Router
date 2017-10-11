package com.jinhui365.router.interceptor;

import com.jinhui365.router.interceptor.IInterceptor;
import com.jinhui365.router.interceptor.InterceptorLifeEnum;
import com.jinhui365.router.interceptor.InterceptorStateCallBack;
import com.jinhui365.router.interceptor.InterceptorStateEnum;
import com.jinhui365.router.route.RouteController;

import java.util.Map;

import static com.jinhui365.router.interceptor.InterceptorLifeEnum.AFTER;
import static com.jinhui365.router.interceptor.InterceptorLifeEnum.START;
import static com.jinhui365.router.interceptor.InterceptorStateEnum.STATE_FAIL;
import static com.jinhui365.router.interceptor.InterceptorStateEnum.STATE_PENDING;
import static com.jinhui365.router.interceptor.InterceptorStateEnum.STATE_SUCCESS;

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

public abstract class InterceptorImpl implements IInterceptor, InterceptorStateCallBack {

    protected RouteController routeController;
    protected Map<String, Object> params;//Interceptor传递的参数
    protected Map<String, Object> options;//condition需要的配置项


    private InterceptorStateEnum state = InterceptorStateEnum.STATE_DEFAULT;//Interceptor验证结果
    private InterceptorLifeEnum lifeState = InterceptorLifeEnum.DEFAULT;//Interceptor验证最后走的生命周期方法

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

    public InterceptorStateEnum getState() {
        return state;
    }

    public void setState(InterceptorStateEnum state) {
        this.state = state;
    }

    public InterceptorLifeEnum getLifeState() {
        return lifeState;
    }

    public void setLifeState(InterceptorLifeEnum lifeState) {
        this.lifeState = lifeState;
    }


    public InterceptorImpl(RouteController routeController, Map<String, Object> params, Map<String, Object> options) {
        this.routeController = routeController;
        this.params = params;
        this.options = options;
    }


    /**
     * Author:jmtian
     * Date: 2017/8/14 17:18
     * description: 初始验证
     */
    @Override
    public void onStart() {
        lifeState = START;
        checkoutState(this);
    }

    /**
     * Author:jmtian
     * Date: 2017/8/14 17:18
     * description: 验证完成回调
     */
    @Override
    public void onAfter() {
        lifeState = InterceptorLifeEnum.AFTER;
        checkoutState(this);
    }

    /**
     * Author:jmtian
     * Date: 2017/8/24 12:58
     * description: 验证打断
     */
    @Override
    public void onBreak() {
        lifeState = InterceptorLifeEnum.BREAK;
        onEndBreak();
    }

    /**
     * 一个前置条件的验证
     * 0 通过
     * 1 失败
     * 2 pending
     */
    @Override
    public abstract void checkoutState(InterceptorStateCallBack stateCallBack);

    @Override
    public void onSuccessBefore() {
        routeController.next();
    }

    @Override
    public void onPendingBefore() {
    }

    @Override
    public abstract void onFailBefore();

    public void onSuccessAfter() {
        routeController.next();
    }

    public void onPendingAfter() {
    }

    public void onFailAfter() {

    }

    public void onEndBreak() {

    }

    @Override
    public void stateCallBack(InterceptorStateEnum state) {
        this.state = state;//记录验证状态
        if (state.equals(STATE_SUCCESS) && lifeState.equals(START)) {
            onSuccessBefore();
        } else if (state.equals(STATE_SUCCESS) && lifeState.equals(AFTER)) {
            onSuccessAfter();
        } else if (state.equals(STATE_FAIL) && lifeState.equals(START)) {
            onFailBefore();
        } else if (state.equals(STATE_FAIL) && lifeState.equals(AFTER)) {
            onFailAfter();
        } else if (state.equals(STATE_PENDING) && lifeState.equals(START)) {
            onPendingBefore();
        } else if (state.equals(STATE_PENDING) && lifeState.equals(AFTER)) {
            onPendingAfter();
        } else {
            onEndBreak();
        }

    }
}
