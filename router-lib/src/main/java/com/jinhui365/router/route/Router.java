package com.jinhui365.router.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jinhui365.router.interceptor.InterceptorStateEnum;
import com.jinhui365.router.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Name:Router
 * Author:jmtian
 * Commemt:路由跳转总管类：
 * 管理路由库的数据初始化，
 * 路由库的调用入口
 * 路由库的上下文管理
 * Date: 2017/8/16 11:24
 */

public class Router {
    private static final String TAG = "Router";
    public static final String RAW_URI = "raw_uri";

    private RouteContext currentContext;

    private IErrorHandler handler;

    public RouteContext getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(RouteContext currentContext) {
        this.currentContext = currentContext;
    }
    public IErrorHandler getHandler() {
        return handler;
    }

    public void setHandler(IErrorHandler handler) {
        this.handler = handler;
    }

    private static final Router instance = new Router();

    private Router() {
    }

    public static Router getInstance() {
        return instance;
    }

    /**
     * Initialize router.
     *
     * @param configJsonString
     */
    public void initialize(String configJsonString, IErrorHandler handler) {
        this.handler = handler;
        RouteManager.getInstance().init(configJsonString);
    }

    /**
     * merge new config of the config's parts to default's config
     *
     * @param jsonString
     */
    public void mergeConfig(String jsonString) {
        RouteManager.getInstance().mergeConfig(jsonString);
    }

    /**
     * build routing by path
     *
     * @param path
     * @return
     */
    public RouteContextBuilder build(String path) {
        return build(path == null ? null : Uri.parse(path));
    }

    /**
     * build routing by Uri
     *
     * @param uri
     * @return
     */
    public RouteContextBuilder build(Uri uri) {
        return new RouteContextBuilder().build(uri);
    }

    /**
     * 拦截器执行完毕，回调路由库
     */
    public void interceptorForSkipResult(InterceptorStateEnum state) {
        interceptorForSkipResult(state, "");
    }


    public void interceptorForSkipResult(InterceptorStateEnum state, String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        interceptorForSkipResult(state, map);
    }

    public void interceptorForSkipResult(InterceptorStateEnum state, String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        interceptorForSkipResult(state, map);
    }

    public void interceptorForSkipResult(InterceptorStateEnum state, Map<String, Object> map) {
        if (null == currentContext || null == currentContext.getParent()) {
            return;
        }

        currentContext.getParent().skipResultCallBack(state, map);
    }

    /**
     * 通过路由库调用的页面，返回上一个页面
     *
     * @param context
     */
    public void goBack(Context context) {
        goBack(context, "");
    }

    public void goBack(Context context, String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        goBack(context, map);
    }

    public void goBack(Context context, String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        goBack(context, map);
    }

    public void goBack(Context context, Map<String, Object> map) {
        Intent intent = new Intent();

    }

    /**
     * 通过路由库跳转的目的页，可以在目的页获取当前页面uri
     *
     * @return
     */
    public String getCurrentUri() {
        return "";
    }
}
