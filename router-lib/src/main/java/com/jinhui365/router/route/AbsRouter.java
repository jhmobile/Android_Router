package com.jinhui365.router.route;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.AnimRes;
import android.util.Log;

import com.jinhui365.router.utils.GsonUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
abstract class AbsRouter implements IRouter {
    private static final String TAG = "AbsRouter";

    RouteRequest mRouteRequest;

    @Override
    public IRouter build(Uri uri) {
        mRouteRequest = new RouteRequest(uri);
        Map<String, Object> params = new HashMap<>();
        params.put(Router.RAW_URI, uri == null ? null : uri.toString());
        mRouteRequest.setParams(params);
        return this;
    }

    @Override
    public IRouter parent(RouteController controller) {
        mRouteRequest.setParent(controller);
        return this;
    }

    @Override
    public IRouter callback(RouteCallback callback) {
        mRouteRequest.setCallback(callback);
        return this;
    }

    @Override
    public IRouter requestCode(int requestCode) {
        mRouteRequest.setRequestCode(requestCode);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRouter with(String key, Object value) {
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

    @Override
    public IRouter with(Map<String, Object> map) {
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

    @Override
    public IRouter with(String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        with(map);
        return this;
    }

    @Override
    public IRouter addFlags(int flags) {
        mRouteRequest.addFlags(flags);
        return this;
    }

    @Override
    public IRouter anim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        mRouteRequest.setEnterAnim(enterAnim);
        mRouteRequest.setExitAnim(exitAnim);
        return this;
    }

    @Override
    public IRouter skipInterceptors() {
        mRouteRequest.setSkipInterceptors(true);
        return this;
    }

    @Override
    public IRouter skipInterceptors(String... interceptors) {
        mRouteRequest.removeInterceptors(interceptors);
        return this;
    }

    @Override
    public IRouter addInterceptors(String... interceptors) {
        mRouteRequest.addInterceptors(interceptors);
        return this;
    }

    @Override
    public void go(Context context, RouteCallback callback) {
        mRouteRequest.setCallback(callback);
        go(context);
    }
}
