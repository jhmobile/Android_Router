package com.jinhui365.router.core;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.AnimRes;
import android.util.Log;

import com.jinhui365.router.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Name:RouteContextBuilder
 * Author:jmtian
 * Commemt:one router of api
 * Date: 2017/10/16 14:18
 */

public class RouteContextBuilder {
    private static final String TAG = "RouteContextBuilder";

    RouteRequest mRouteRequest;

    public RouteContextBuilder build(Uri uri) {
        mRouteRequest = new RouteRequest(uri);
        Map<String, Object> params = new HashMap<>();
        params.put(RouteManager.RAW_URI, uri == null ? null : uri.toString());
        mRouteRequest.setParams(params);
        return this;
    }

    public RouteContextBuilder callback(IRouteCallBack callback) {
        mRouteRequest.setCallback(callback);
        return this;
    }

    public RouteContextBuilder requestCode(int requestCode) {
        mRouteRequest.setRequestCode(requestCode);
        return this;
    }

    public RouteContextBuilder withParams(String key, Object value) {
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

    public RouteContextBuilder withParams(Map<String, Object> map) {
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

    public RouteContextBuilder withParams(String json) {
        Map<String, Object> map = JsonUtil.jsonToMap(json, String.class, Object.class);
        withParams(map);
        return this;
    }

    public RouteContextBuilder addFlags(int flags) {
        mRouteRequest.addFlags(flags);
        return this;
    }

    public RouteContextBuilder anim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        mRouteRequest.setEnterAnim(enterAnim);
        mRouteRequest.setExitAnim(exitAnim);
        return this;
    }

    /**
     * 跳过所有拦截器，级别最高。
     *
     * @return
     */
    public RouteContextBuilder skipInterceptors() {
        mRouteRequest.setSkipInterceptors(true);
        return this;
    }

    /**
     * 跳过指定拦截器
     *
     * @param interceptors
     * @return
     */
    public RouteContextBuilder skipInterceptors(Class<AbsInterceptor>... interceptors) {
        mRouteRequest.removeInterceptors(interceptors);
        return this;
    }

    /**
     * 添加拦截器
     *
     * @param clazz
     * @param options
     * @return
     */
    public RouteContextBuilder addInterceptor(Class<AbsInterceptor> clazz, Map<String, Object> options) {
        addInterceptor(clazz, options, -1);
        return this;
    }

    public RouteContextBuilder addInterceptor(Class<AbsInterceptor> clazz, String optionsJson) {
        addInterceptor(clazz, optionsJson, -1);
        return this;
    }

    public RouteContextBuilder addInterceptor(Class<AbsInterceptor> clazz, String optionsJson, int index) {
        Map<String, Object> options = JsonUtil.jsonToMap(optionsJson, String.class, Object.class);
        addInterceptor(clazz, options, index);
        return this;
    }

    public RouteContextBuilder addInterceptor(Class<AbsInterceptor> clazz,  Map<String, Object> options, int index) {
        mRouteRequest.addInterceptors(clazz, options, index);
        return this;
    }

    public void go(Context context, IRouteCallBack callback) {
        mRouteRequest.setCallback(callback);
        go(context);
    }

    public void go(Context context) {
        doing(context);
    }


    /**
     * 路由数据处理
     */
    private void doing(Context context) {
//        if (mRouteRequest.getUri() == null) {
//            callback(RouteResult.FAILED, "uri == null.");
//        }
//
//        RouteVO resultVO = ConfigManager.getInstance().getResultVOByRoutePath(mRouteRequest.getUri().getPath());
//        if (null == resultVO) {
//            callback(RouteResult.FAILED, "resultVO == null.");
//        }
//        if (null != resultVO.getParams() && !resultVO.getParams().isEmpty()) {
//            mRouteRequest.getParams().putAll(resultVO.getParams());
//        }
//
        RouteContext currentContext = null;
//        if (null == resultVO.getRContext()) {
//            currentContext = getRouteContext(resultVO, context);
//        } else {
//            currentContext = new RouteContext(mRouteRequest, context);
//        }
        currentContext.setParent(RouteManager.getInstance().getCurrentContext());
        if (null != RouteManager.getInstance().getCurrentContext()) {
            RouteManager.getInstance().getCurrentContext().addChild(currentContext);
        }
        currentContext.next();
    }


    /**
     * one router of context's class
     *
     * @param resultVO
     * @param context
     * @return
     */
    private RouteContext getRouteContext(RouteVO resultVO, Context context) {
//        if (null != resultVO.getRContext().getOptions() && resultVO.getRContext().getOptions().isEmpty()) {
//            mRouteRequest.getOptions().putAll(resultVO.getRContext().getOptions());
//        }
//
//        Class clazz = resultVO.getRContext().getClazz();
        RouteContext currentContext = null;
//        if (null != clazz) {
//            try {
//                currentContext = (RouteContext) clazz.getConstructor(new Class[]{ResultVO.class, Context.class}).newInstance(new Object[]{resultVO, context});
//            } catch (Exception var) {
//                currentContext = new RouteContext(mRouteRequest, context);
//            }
//        } else {
//            currentContext = new RouteContext(mRouteRequest, context);
//        }

        return currentContext;
    }

    /**
     * 路由处理回调
     *
     * @param response
     */
    private void callback(RouteResponse response) {
        if (mRouteRequest.getCallback() != null) {

        }
    }

}
