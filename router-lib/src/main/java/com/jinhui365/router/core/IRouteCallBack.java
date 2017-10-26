package com.jinhui365.router.core;

/**
 * Name:IRouteCallBack
 * Author:jmtian
 * Commemt: IRouteCallBack interface
 * Date: 2017/9/22 14:21
 */

public interface IRouteCallBack {
    /**
     * callback
     *
     * @param response
     */
    void onSucces(RouteResponse response);
    void onFail(RouteResponse response);
}
