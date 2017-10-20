package com.jinhui365.router.route;

import android.content.Context;

/**
 * Name:IErrorHandler
 * Author:jmtian
 * Commemt:错误设置
 * Date: 2017/10/19 14:31
 */

public interface IErrorHandler {

    void onClientError(Context context, RouteResponse response);

    void onServerError(Context context, RouteResponse response);
}
