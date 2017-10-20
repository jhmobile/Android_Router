package com.jinhui365.router.matcher;

import android.content.Context;

import com.jinhui365.router.route.RouteContext;

/**
 * Name:IMatcherTarget
 * Author:jmtian
 * Commemt:目标匹配接口
 * Date: 2017/10/19 15:55
 */

public interface IMatcherTarget {
    void matcher(Context context, RouteContext routeContext);
}
