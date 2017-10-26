package com.jinhui365.router.core;

import com.jinhui365.router.route.RouteContext;

/**
 * Name:IRouteTask
 * Author:jmtian
 * Commemt:目标匹配接口
 * Date: 2017/10/19 15:55
 */

public interface IRouteTask {
    void execute(RouteContext routeContext);
}
