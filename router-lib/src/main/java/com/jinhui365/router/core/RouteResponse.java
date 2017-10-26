package com.jinhui365.router.core;

/**
 * Name:RouteResponse
 * Author:jmtian
 * Commemt: 路由请求结果
 * Date: 2017/10/19 14:29
 */
public class RouteResponse {
    private int code;
    private String msg;
    private RouteContext routeContext;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RouteContext getRouteContext() {
        return routeContext;
    }

    public void setRouteContext(RouteContext routeContext) {
        this.routeContext = routeContext;
    }
}
