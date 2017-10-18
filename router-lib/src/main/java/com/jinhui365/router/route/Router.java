package com.jinhui365.router.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    public RouteContext getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(RouteContext currentContext) {
        this.currentContext = currentContext;
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
    public void initialize(String configJsonString) {
        RouteManager.getInstance().init(configJsonString);
    }

    public ConfigRouter build(String path) {
        return build(path == null ? null : Uri.parse(path));
    }

    public ConfigRouter build(Uri uri) {
        return new ConfigRouter().build(uri);
    }

    /**
     * 注解参数
     *
     * @param object
     */
    public void injectParams(Object object) {

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

    public void interceptorForSkipResult(boolean isBreak, String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        interceptorForSkipResult(isBreak, map);
    }

    public void interceptorForSkipResult(boolean isBreak, Map<String, Object> map) {
        if (null == currentContext || null == currentContext.getParent()) {
            return;
        }

        currentContext.getParent().skipResultCallBack(isBreak, map);
    }

    /**
     * 返回起始页面
     *
     * @param context
     */
    public void goBackFromContext(Context context) {
        goBackFromContext(context, "");
    }

    public void goBackFromContext(Context context, String json) {
        Map<String, Object> map = GsonUtils.jsonToMap(json, String.class, Object.class);
        goBackFromContext(context, map);
    }

    public void goBackFromContext(Context context, String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        goBackFromContext(context, map);
    }

    public void goBackFromContext(Context context, Map<String, Object> map) {
        Intent intent = new Intent();

    }
}
