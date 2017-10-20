package com.jinhui365.router.route;

import android.app.Activity;
import android.content.Context;

import com.jinhui365.router.interceptor.InterceptorImpl;
import com.jinhui365.router.interceptor.InterceptorStateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jinhui365.router.interceptor.InterceptorStateEnum.DEFAULT;

/**
 * Author:jmtian
 * Date: 2017/8/14 17:10
 * description:
 * 一次路由跳转的控制器
 * 包含了跳转需要的初始信息，包括但不限于发起跳转的源、跳转配置、跳转参数
 * 管理执行跳转过程中的运行时信息,前置条件的管理
 */
public class RouteContext {
    private static final String TAG = "RouteContext";
    /**
     * 当前在执行的前置条件下标
     */
    private int currentInterceptorIndex = -1;

    protected Context context;
    private RouteRequest routeRequest;
    private List<InterceptorImpl> interceptors;//Interceptor集合
    private Map<String, Object> params;//跳转需要的参数
    private Map<String, Object> options;//baseContext子类需要的配置项参数
    private RouteContext parent;
    private List<RouteContext> children = new ArrayList<>();

    public RouteContext getParent() {
        return parent;
    }

    public void setParent(RouteContext parent) {
        this.parent = parent;
    }


    /**
     * add a child of RouteContext
     *
     * @param routeContext
     */
    public void addChild(RouteContext routeContext) {
        children.add(routeContext);
    }

    /**
     * 获取当前RouteContext的子Context的个数
     *
     * @return
     */
    public int getChildrenSize() {
        return children.size();
    }

    public RouteContext(RouteRequest routeRequest, Context context) {
        this.routeRequest = routeRequest;
        this.context = context;
        this.interceptors = routeRequest.getInterceptors();
        this.params = routeRequest.getParams();
        this.options = routeRequest.getOptions();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    /**
     * 获取执行过的Interceptor集合
     *
     * @return
     */
    public List<InterceptorImpl> getInterceptors() {
        return interceptors;
    }

    /**
     * 获取传递过来的所有参数
     *
     * @return
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 获取baseContext子类的配置项参数
     *
     * @return
     */
    public Map<String, Object> getOptions() {
        return options;
    }

    /**
     * 检查当前条件以及未验证条件，返回是否还有未验证的前置条件
     */
    public boolean hasUnVerifyInterceptor() {
        return !interceptors.isEmpty() &&
                currentInterceptorIndex < (interceptors.size() - 1);
    }

    /**
     * 获取当前在执行的权限
     *
     * @return
     */
    public InterceptorImpl getCurrentInterceptor() {
        if (-1 == currentInterceptorIndex || currentInterceptorIndex >= interceptors.size()) {
            return null;
        }
        return interceptors.get(currentInterceptorIndex);
    }

    /**
     * 执行下一个前置条件
     */
    public void next() {
        if (hasUnVerifyInterceptor()) {
            InterceptorImpl nextInterceptor = interceptors.get(++currentInterceptorIndex);
            nextInterceptor.onStart();
        } else {
            gotoTarget();
        }
    }

    /**
     * 权限验证回调
     *
     * @param state 回调状态
     * @param map   回调参数
     */
    public void skipResultCallBack(InterceptorStateEnum state, Map<String, Object> map) {
        InterceptorImpl currentInterceptor = getCurrentInterceptor();
        if (currentInterceptor != null) {
            if (null != map && !map.isEmpty()) {
                currentInterceptor.getParams().putAll(map);
            }
            // 回调是默认状态，则代表打断
            if (DEFAULT.equals(state)) {
                currentInterceptor.onBreak();
            } else {
                currentInterceptor.onAfter();
            }
        }
    }

    /**
     * 直接跳转到目标页面
     * 默认实现是创建新的控制器对象并直接跳转
     * 如果需要可以直接跳转
     */
    private void gotoTarget() {
        Router.getInstance().setCurrentContext(parent);
//        callback(RouteState.SUCCEED, null);

    }

}
