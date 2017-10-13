package com.jinhui365.router.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.jinhui365.router.data.ResultVO;
import com.jinhui365.router.interceptor.InterceptorImpl;
import com.jinhui365.router.utils.RLog;
import com.jinhui365.router.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author:jmtian
 * Date: 2017/8/14 17:10
 * description:
 * 一次路由跳转的控制器
 * 包含了跳转需要的初始信息，包括但不限于发起跳转的源、跳转配置、跳转参数
 * 管理执行跳转过程中的运行时信息,前置条件的管理
 */
public class RouteController {
    private static final String TAG = "RouteController";
    /**
     * 当前在执行的前置条件下标
     */
    private int currentInterceptorIndex = -1;

    protected Context context;
    private RouteRequest routeRequest;
    private Class targetClass;
    private List<InterceptorImpl> interceptors;//Interceptor集合
    private Map<String, Object> params;//跳转需要的参数
    private Map<String, Object> options;//baseContext子类需要的配置项参数
    private List<RouteController> children = new ArrayList<>();

    /**
     * add a child of RouteController
     * @param controller
     */
    public void addChild(RouteController controller) {
        children.add(controller);
    }

    public RouteController(RouteRequest routeRequest, Context context) {
        this.routeRequest = routeRequest;
        this.context = context;
        this.targetClass = routeRequest.getTargetClass();
        this.interceptors = routeRequest.getInterceptors();
        this.params = routeRequest.getParams();
        this.options = routeRequest.getOptions();
    }

    public Class getTargetClass() {
        return routeRequest.getTargetClass();
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
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
     * 清楚垃圾数据，controller  options里面对应key的数据不需要传递给intent
     */
    private void clearRubbishParams() {
        if (options != null && !options.isEmpty() && params != null && !params.isEmpty()) {
            for (String key : options.keySet()) {
                if (params.containsKey(key)) {
                    params.remove(key);
                }
            }
        }
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
     * @param isBreak 是否是打断
     * @param map     回调传回的结果map
     */
    public void skipResultCallBack(boolean isBreak, Map<String, Object> map) {
        InterceptorImpl currentInterceptor = getCurrentInterceptor();
        if (currentInterceptor != null) {
            if (null != map && !map.isEmpty()) {
                currentInterceptor.getParams().putAll(map);
            }
            // 如果是打断,执行break,否则执行after，验证状态
            if (isBreak) {
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
    protected void gotoTarget() {
        Intent intent = assembleFlagsIntent();
        if (intent == null) {
            return;
        }

        if (context instanceof Activity) {
            if (routeRequest.getRequestCode() > 0) {
                ActivityCompat.startActivityForResult((Activity) context, intent, routeRequest.getRequestCode(), null);
            } else {
                ActivityCompat.startActivity((Activity) context, intent, null);
            }
            assembleAnim();
        }
        RealRouter.getInstance().callback(RouteResult.SUCCEED, null);
    }

    protected Intent getIntent() {
        if (null == targetClass) {
            RLog.e("no targetClass");
            return null;
        }
        Intent intent = new Intent();
        clearRubbishParams();
        Bundle bundle = new Bundle();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                bundle.putAll(Util.getBundle(bundle, key, params.get(key)));
            }
        }
        intent.setClass(context, targetClass);
        return intent;
    }

    protected void assembleAnim() {
        if (context instanceof Activity) {
            if (routeRequest.getEnterAnim() != 0 && routeRequest.getExitAnim() != 0) {
                // Add transition animation.
                ((Activity) context).overridePendingTransition(
                        routeRequest.getEnterAnim(), routeRequest.getExitAnim());
            }

        }
    }

    protected Intent assembleFlagsIntent() {
        Intent intent = getIntent();
        intent.addFlags(routeRequest.getFlags());
        return intent;
    }

}
