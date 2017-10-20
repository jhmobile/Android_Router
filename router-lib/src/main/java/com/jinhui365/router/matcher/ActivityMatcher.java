package com.jinhui365.router.matcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.jinhui365.router.route.RouteContext;
import com.jinhui365.router.route.RouteRequest;
import com.jinhui365.router.utils.Util;

import java.util.Map;

/**
 * Name:ActivityMatcher
 * Author:jmtian
 * Commemt:Activity匹配类
 * Date: 2017/10/19 15:56
 */


public class ActivityMatcher implements IMatcherTarget {
    private static final String TAG = "ActivityMatcher";

    private Context context;

    Map<String, Object> options;
    Map<String, Object> params;
    @Override
    public void matcher(Context context, RouteContext routeContext) {
        this.context = context;
        this.routeRequest = routeRequest;
        options = routeRequest.getOptions();
        params = routeRequest.getParams();
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
        if (null == routeRequest.getTargetClass()) {
            Log.e(TAG, "no targetClass");
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
     * 清楚垃圾数据，context  options里面对应key的数据不需要传递给intent
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
}
