package com.jinhui365.router.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
public class RouteController extends AbsRouter {
    private static final String TAG = "RouteController";
    /**
     * 当前在执行的前置条件下标
     */
    private int currentInterceptorIndex = -1;

    protected Context context;
    protected ResultVO resultVO;
    protected List<InterceptorImpl> interceptors;//Interceptor集合
    protected Map<String, Object> params;//跳转需要的参数
    protected Map<String, String> options;//baseContext子类需要的配置项参数
    protected List<RouteController> list = new ArrayList<>();


    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public int getCurrentInterceptorIndex() {
        return currentInterceptorIndex;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public RouteController() {

    }

    public RouteController(RouteController parent, ResultVO resultVO) {
        if (null != parent) {
            parent.list.add(this);
        }
        this.resultVO = resultVO;
        this.interceptors = resultVO.getInterceptors(this);
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
        Intent intent = getIntent(context);
        if (intent == null) {
            return;
        }

        if (context instanceof Activity) {
            (Activity)context.startActivityForResult(intent, mRouteRequest.getRequestCode());

            if (mRouteRequest.getEnterAnim() != 0 && mRouteRequest.getExitAnim() != 0) {
                // Add transition animation.
                ((Activity) context).overridePendingTransition(
                        mRouteRequest.getEnterAnim(), mRouteRequest.getExitAnim());
            }
        }
        callback(RouteResult.SUCCEED, null);
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
        if (getOptions() != null && !getOptions().isEmpty()) {
            for (String key : getOptions().keySet()) {
                if (getParams().containsKey(key)) {
                    getParams().remove(key);
                }
            }
        }
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
    public Map<String, String> getOptions() {
        if (null != resultVO.getController()) {
            return resultVO.getController().getOptions();
        } else {
            return options;
        }
    }

    /**
     * 根据下标获取权限验证类
     *
     * @param index
     * @return
     */
    public InterceptorImpl getAuthInterceptorByIndex(int index) {
        return interceptors.get(index);
    }

    /**
     * 获取权限组的数量
     *
     * @return
     */
    public int getAuthInterceptorSize() {
        return interceptors.size();
    }

    private void callback(RouteResult result, String msg) {
        if (result != RouteResult.SUCCEED) {
            RLog.w(msg);
        }
        if (mRouteRequest.getCallback() != null) {
            mRouteRequest.getCallback().callback(result, mRouteRequest.getUri(), msg);
        }
    }

    private void doing(Context context) {
        if (mRouteRequest.getUri() == null) {
            callback(RouteResult.FAILED, "uri == null.");
        }
        Set<Map.Entry<String, Class<?>>> entries = AptHub.routeTable.entrySet();

        ResultVO resultVO = RouterManager.getInstance().getResultVOByPageID(mRouteRequest.getUri().getPath());
        if (null != resultVO) {//配置文件不为空，走配置文件
            next();
        } else {//走默认注解

        }
    }

    @Override
    public Intent getIntent(Context context) {
        Class activityClass = null;
        if (resultVO != null) {//热更文件获取
            activityClass = resultVO.getActivityClass();
        } else {//本地反射获取

        }
        if (null == activityClass) {
            return null;
        }
        Intent intent = new Intent();
        clearRubbishParams();
        Bundle bundle = new Bundle();
        if (getParams() != null && !getParams().isEmpty()) {
            for (String key : getParams().keySet()) {
                bundle.putAll(Util.getBundle(bundle, key, getParams().get(key)));
            }
        }
        intent.setClass(context, activityClass);
        return intent;
    }


    @Override
    public void go(Context context) {
        this.context = context;
        doing(context);
        if (null != mRouteRequest.getParent()) {
            mRouteRequest.getParent().list.add(this);
            Router.routeController = mRouteRequest.getParent();
        }

    }
}
