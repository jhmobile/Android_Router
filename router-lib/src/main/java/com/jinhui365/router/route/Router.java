package com.jinhui365.router.route;

import android.content.Context;
import android.net.Uri;

import com.jinhui365.router.annotation.Route;
import com.jinhui365.router.utils.GsonUtils;
import com.jinhui365.router.utils.RLog;

import java.util.Map;


/**
 * Name:Router
 * Author:jmtian
 * Commemt:路由跳转调用类
 * Date: 2017/8/16 11:24
 */

public class Router {
    private static final String TAG = "Router";
    /**
     * You can get the raw uri in target page by call <code>intent.getStringExtra(Router.RAW_URI)</code>.
     */
    public static final String RAW_URI = "raw_uri";

    private static final Router instance = new Router();

    private Router() {
    }

    public static Router getInstance() {
        return instance;
    }

    /**
     * Initialize router.
     *
     * @param context placeholder for a future usage.
     */
    public void initialize(Context context) {
        initialize(context, false);
    }

    /**
     * Initialize router.
     *
     * @param context    placeholder for a future usage.
     * @param debuggable {@link #setDebuggable(boolean)}.
     */
    public void initialize(Context context, boolean debuggable) {
        if (debuggable) {
            setDebuggable(true);
        }
        AptHub.initDefault();
    }

    public void setDebuggable(boolean debuggable) {
        RLog.showLog(debuggable);
    }

    public IRouter build(String path) {
        return build(path == null ? null : Uri.parse(path));

    }

    public IRouter build(Uri uri) {
        return RealRouter.getInstance().build(uri);
    }

    /**
     * 拦截验证完回调
     */
    public void interceptorForSkipResult(boolean isBreak) {
        interceptorForSkipResult(isBreak, "");
    }

    public void interceptorForSkipResult(boolean isBreak, String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        interceptorForSkipResult(isBreak, map);
    }

    public void interceptorForSkipResult(boolean isBreak, Map<String, Object> map) {
        RealRouter.getInstance().interceptorForSkipResult(isBreak, map);
    }

    public void goBackFromContext(Context context) {
        RealRouter.getInstance().goBackFromContext(context);
    }
}
