package com.jinhui365.router.route;

import android.content.Context;
import android.content.Intent;

import com.jinhui365.router.data.ResultVO;
import com.jinhui365.router.utils.RLog;

import java.util.Map;
import java.util.Set;


public class RealRouter extends AbsRouter {
    private static RealRouter sInstance;
    private ResultVO resultVO;
    private RouteController controller;

    public RouteController getController() {
        return controller;
    }

    public void setController(RouteController controller) {
        this.controller = controller;
    }

    private RealRouter() {

    }

    static synchronized RealRouter getInstance() {
        if (sInstance == null) {
            sInstance = new RealRouter();
        }
        return sInstance;
    }

    @Override
    public void go(Context context) {
        if (null != controller) {
            mRouteRequest.setParent(controller);
        }
        doing();
    }

    /**
     * 数据预处理
     */
    private void doing() {
        if (mRouteRequest.getUri() == null) {
            callback(RouteResult.FAILED, "uri == null.");
        }

        resultVO = RouterManager.getInstance().getResultVOByPageID(mRouteRequest.getUri().getPath());
        if (null != resultVO) {//配置文件不为空，走配置文件
            with(resultVO.getParams());
        } else {//走注解
            Set<Map.Entry<String, Class<?>>> entries = AptHub.routeTable.entrySet();


        }

    }

    public void callback(RouteResult result, String msg) {
        if (result != RouteResult.SUCCEED) {
            RLog.w(msg);
        }
        if (mRouteRequest.getCallback() != null) {
            mRouteRequest.getCallback().callback(result, mRouteRequest.getUri(), msg);
        }
    }

    public void interceptorForSkipResult(boolean isBreak, Map<String, Object> map) {
        if (null == controller) {
            return;
        }
        controller.skipResultCallBack(isBreak, map);
    }

    public void goBackFromContext(Context context) {
        //返回目标页
    }
}
