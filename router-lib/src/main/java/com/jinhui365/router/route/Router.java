package com.jinhui365.router.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jinhui365.router.annotation.Route;
import com.jinhui365.router.data.ResultVO;
import com.jinhui365.router.utils.GsonUtils;
import com.jinhui365.router.utils.RLog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Name:Router
 * Author:jmtian
 * Commemt:路由跳转总管类：
 * 管理路由库的数据初始化，
 * 路由库的调用入口
 * 路由库的上下文管理
 * Date: 2017/8/16 11:24
 */

public class Router extends AbsRouter {
    private static final String TAG = "Router";
    public static final String RAW_URI = "raw_uri";

    private RouteContext parentContext;
    private RouteContext currentContext;

    public RouteContext getParentContext() {
        return parentContext;
    }

    public void setParentContext(RouteContext parentContext) {
        this.parentContext = parentContext;
    }

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

    public AbsRouter build(String path) {
        return build(path == null ? null : Uri.parse(path));
    }

    /**
     * 注解参数
     *
     * @param object
     */
    public void injectParams(Object object) {

    }


    @Override
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

        if (null == resultVO.getRContext()) {
            currentContext = getRouteContext(resultVO, context);
        } else {
            currentContext = new RouteContext(mRouteRequest, context);
        }
        currentContext.setParent(parentContext);
        if (null != parentContext) {
            parentContext.addChild(currentContext);
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
    public void callback(RouteResult result, String msg) {
        if (result != RouteResult.SUCCEED) {
            RLog.w(msg);
        }
        if (mRouteRequest.getCallback() != null) {
            mRouteRequest.getCallback().callback(result, mRouteRequest.getUri(), msg);
        }
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
        if (null == parentContext) {
            return;
        }
        parentContext.skipResultCallBack(isBreak, map);
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
