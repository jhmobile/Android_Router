package com.jinhui365.router.route;

import android.content.Context;
import android.net.Uri;

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

    private static boolean sDebuggable = false;

    public static RouteController routeController;

    /**
     * Initialize router.
     *
     * @param context placeholder for a future usage.
     */
    public static void initialize(Context context) {
        initialize(context, false);
    }

    /**
     * Initialize router.
     *
     * @param context    placeholder for a future usage.
     * @param debuggable {@link #setDebuggable(boolean)}.
     */
    public static void initialize(Context context, boolean debuggable) {
        if (debuggable) {
            setDebuggable(true);
        }
        AptHub.initDefault();
    }

    public static void setDebuggable(boolean debuggable) {
        sDebuggable = debuggable;
        RLog.showLog(debuggable);
    }

    public static IRouter build(String path) {
        return build(path == null ? null : Uri.parse(path));

    }

    public static IRouter build(Uri uri) {
        IRouter iRouter = null;
        try {
            RouteController controller = (RouteController) Class.forName(RouteController.class.getSimpleName()).newInstance();

            iRouter = controller.build(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iRouter;
    }

    /**
     * 拦截验证完回调
     */
    public static void interceptorForSkipResult(boolean isBreak) {
        interceptorForSkipResult(isBreak, "");
    }

    public static void interceptorForSkipResult(boolean isBreak, String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        interceptorForSkipResult(isBreak, map);
    }

    public static void interceptorForSkipResult(boolean isBreak, Map<String, Object> map) {
        if (null == routeController) {
            return;
        }
        routeController.skipResultCallBack(isBreak, map);
    }
}
