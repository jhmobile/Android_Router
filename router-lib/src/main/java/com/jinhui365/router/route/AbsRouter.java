package com.jinhui365.router.route;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.AnimRes;
import android.util.Log;

import com.jinhui365.router.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Name:AbsRouter
 * Author:jmtian
 * Commemt:one router of api
 * Date: 2017/10/16 14:18
 */

abstract class AbsRouter {
    private static final String TAG = "AbsRouter";

    RouteRequest mRouteRequest;

    public AbsRouter build(Uri uri) {
        mRouteRequest = new RouteRequest(uri);
        Map<String, Object> params = new HashMap<>();
        params.put(Router.RAW_URI, uri == null ? null : uri.toString());
        mRouteRequest.setParams(params);
        return this;
    }

    public AbsRouter callback(RouteCallback callback) {
        mRouteRequest.setCallback(callback);
        return this;
    }

    public AbsRouter requestCode(int requestCode) {
        mRouteRequest.setRequestCode(requestCode);
        return this;
    }

    public AbsRouter with(String key, Object value) {
        if (value == null) {
            Log.w(TAG, "Ignored: The extra value is null.");
            return this;
        }
        Map<String, Object> params = mRouteRequest.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        mRouteRequest.setParams(params);
        return this;
    }

    public AbsRouter with(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            Log.w(TAG, "Ignored: The extra value is null.");
            return this;
        }
        Map<String, Object> params = mRouteRequest.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.putAll(map);
        mRouteRequest.setParams(params);
        return this;
    }

    public AbsRouter with(String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        with(map);
        return this;
    }

    public AbsRouter addFlags(int flags) {
        mRouteRequest.addFlags(flags);
        return this;
    }

    public AbsRouter anim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        mRouteRequest.setEnterAnim(enterAnim);
        mRouteRequest.setExitAnim(exitAnim);
        return this;
    }

    public AbsRouter skipInterceptors() {
        mRouteRequest.setSkipInterceptors(true);
        return this;
    }


    public AbsRouter skipInterceptors(String... interceptors) {
        mRouteRequest.removeInterceptors(interceptors);
        return this;
    }

    public AbsRouter addInterceptors(String... interceptors) {
        mRouteRequest.addInterceptors(interceptors);
        return this;
    }


    public void go(Context context, RouteCallback callback) {
        mRouteRequest.setCallback(callback);
        go(context);
    }

    public void go(Context context) {

    }

}
