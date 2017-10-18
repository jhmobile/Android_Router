package com.jinhui365.router.route;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.AnimRes;
import android.util.Log;

import com.jinhui365.router.data.ResultVO;
import com.jinhui365.router.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Name:ConfigRouter
 * Author:jmtian
 * Commemt:one router of api
 * Date: 2017/10/16 14:18
 */

public class ConfigRouter {
    private static final String TAG = "ConfigRouter";

    RouteRequest mRouteRequest;

    public ConfigRouter build(Uri uri) {
        mRouteRequest = new RouteRequest(uri);
        Map<String, Object> params = new HashMap<>();
        params.put(Router.RAW_URI, uri == null ? null : uri.toString());
        mRouteRequest.setParams(params);
        return this;
    }

    public ConfigRouter callback(RouteCallback callback) {
        mRouteRequest.setCallback(callback);
        return this;
    }

    public ConfigRouter requestCode(int requestCode) {
        mRouteRequest.setRequestCode(requestCode);
        return this;
    }

    public ConfigRouter with(String key, Object value) {
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

    public ConfigRouter with(Map<String, Object> map) {
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

    public ConfigRouter with(String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        with(map);
        return this;
    }

    public ConfigRouter addFlags(int flags) {
        mRouteRequest.addFlags(flags);
        return this;
    }

    public ConfigRouter anim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        mRouteRequest.setEnterAnim(enterAnim);
        mRouteRequest.setExitAnim(exitAnim);
        return this;
    }

    public ConfigRouter skipInterceptors() {
        mRouteRequest.setSkipInterceptors(true);
        return this;
    }


    public ConfigRouter skipInterceptors(String... interceptors) {
        mRouteRequest.removeInterceptors(interceptors);
        return this;
    }

    public ConfigRouter addInterceptors(String... interceptors) {
        mRouteRequest.addInterceptors(interceptors);
        return this;
    }


    public void go(Context context, RouteCallback callback) {
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
        if (mRouteRequest.getUri() == null) {
            callback(RouteResult.FAILED, "uri == null.");
        }

        ResultVO resultVO = RouteManager.getInstance().getResultVOByRoutePath(mRouteRequest.getUri().getPath());
        if (null == resultVO) {
            callback(RouteResult.FAILED, "resultVO == null.");
        }
        if (null != resultVO.getParams() && !resultVO.getParams().isEmpty()) {
            mRouteRequest.getParams().putAll(resultVO.getParams());
        }

        RouteContext currentContext;
        if (null == resultVO.getRContext()) {
            currentContext = getRouteContext(resultVO, context);
        } else {
            currentContext = new RouteContext(mRouteRequest, context);
        }
        currentContext.setParent(Router.getInstance().getCurrentContext());
        if (null != Router.getInstance().getCurrentContext()) {
            Router.getInstance().getCurrentContext().addChild(currentContext);
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
    private RouteContext getRouteContext(ResultVO resultVO, Context context) {
        if (null != resultVO.getRContext().getOptions() && resultVO.getRContext().getOptions().isEmpty()) {
            mRouteRequest.getOptions().putAll(resultVO.getRContext().getOptions());
        }

        Class clazz = resultVO.getRContext().getClazz();
        RouteContext currentContext;
        if (null != clazz) {
            try {
                currentContext = (RouteContext) clazz.getConstructor(new Class[]{ResultVO.class, Context.class}).newInstance(new Object[]{resultVO, context});
            } catch (Exception var) {
                currentContext = new RouteContext(mRouteRequest, context);
            }
        } else {
            currentContext = new RouteContext(mRouteRequest, context);
        }

        return currentContext;
    }

    /**
     * 路由处理回调
     *
     * @param result
     * @param msg
     */
    private void callback(RouteResult result, String msg) {
        if (result != RouteResult.SUCCEED) {
            Log.e(TAG,msg);
        }
        if (mRouteRequest.getCallback() != null) {
            mRouteRequest.getCallback().callback(result, mRouteRequest.getUri(), msg);
        }
    }

}
