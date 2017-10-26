package com.jinhui365.router.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.jinhui365.router.core.IRouteTask;
import com.jinhui365.router.route.RouteContext;
import com.jinhui365.router.core.RouteRequest;
import com.jinhui365.router.utils.Util;

/**
 * Name:ActivityTask
 * Author:jmtian
 * Commemt:Activity匹配类
 * Date: 2017/10/19 15:56
 */


public class ActivityTask implements IRouteTask {
    private static final String TAG = "ActivityTask";

    private Context context;
    private RouteContext routeContext;
    private RouteRequest routeRequest;

    @Override
    public void execute(RouteContext routeContext) {
        this.context = routeContext.getContext();
        this.routeContext = routeContext;
        this.routeRequest = routeContext.getRouteRequest();
        if (context instanceof Activity) {
            Intent intent = assembleFlagsIntent();
            if (intent == null) {
                return;
            }
            if (routeRequest.getRequestCode() > 0) {
                ActivityCompat.startActivityForResult((Activity) context, intent, routeRequest.getRequestCode(), null);
            } else {
                ActivityCompat.startActivity((Activity) context, intent, null);
            }
            assembleAnim();
        }
    }

    protected Intent getIntent() {
        if (null == routeRequest.getTarget() || routeRequest.getTarget().isEmpty()) {
            Log.e(TAG, "no target");
            return null;
        }
        Class<?> targetClass = null;
        if (routeRequest.getTarget().containsKey("clazz")) {
            String clazzName = (String) routeRequest.getTarget().get("clazz");
            targetClass = getActivityClass(clazzName);
            Intent intent = new Intent();
            clearRubbishParams();
            Bundle bundle = new Bundle();
            if (routeContext.getParams() != null && !routeContext.getParams().isEmpty()) {
                for (String key : routeContext.getParams().keySet()) {
                    bundle.putAll(Util.getBundle(bundle, key, routeContext.getParams().get(key)));
                }
            }
            intent.setClass(context, targetClass);
            return intent;
        }
        return null;
    }

    /**
     * 获取activity对呀的class
     *
     * @return
     */
    protected Class<?> getActivityClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            Log.e(TAG, "no activity class");
        }
        return null;
    }

    /**
     * 设置跳转动画
     */
    protected void assembleAnim() {
        if (routeRequest.getEnterAnim() != 0 && routeRequest.getExitAnim() != 0) {
            // Add transition animation.
            ((Activity) context).overridePendingTransition(
                    routeRequest.getEnterAnim(), routeRequest.getExitAnim());
        }

    }

    /**
     * 设置目标activity 的flags
     *
     * @return
     */
    protected Intent assembleFlagsIntent() {
        Intent intent = getIntent();
        intent.addFlags(routeRequest.getFlags());
        return intent;
    }

    /**
     * 清楚垃圾数据
     */
    private void clearRubbishParams() {

    }
}
