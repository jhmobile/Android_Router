package com.jinhui365.router.route;

/**
 * Name:RouteResponse
 * Author:jmtian
 * Commemt: 路由请求结果
 * Date: 2017/10/19 14:29
 */
public class RouteResponse {
    private RouteState code;
    private String msg;
    private RouteContext context;

    public RouteState getCode() {
        return code;
    }

    public void setCode(RouteState code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RouteContext getContext() {
        return context;
    }

    public void setContext(RouteContext context) {
        this.context = context;
    }
}
