package com.jinhui365.router.matcher;

import com.jinhui365.router.route.RouteContext;

/**
 * Name:IMatcherTarget
 * Author:jmtian
 * Commemt:目标匹配接口
 * Date: 2017/10/19 15:55
 */

public interface IMatcherTarget {
    void matcher(RouteContext routeContext);
}
