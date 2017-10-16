package com.jinhui365.router.route;
import java.util.List;

/**
 * Name:RouteGroup
 * Author:jmtian
 * Commemt:路由分组管理，对拦截器
 * Date: 2017/10/12 14:55
 */


public abstract class RouteGroup {
    /**
     * 设置拦截器
     */
    abstract List<String> getInterceptor();
}
