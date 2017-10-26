package com.jinhui365.router.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Name:RouteVO
 * Author:jmtian
 * Commemt:路由VO
 * Date: 2017/10/19 13:24
 */


public class RouteVO {
    private static final String TAG = "RouteVO";

    private List<RouteOptionVO> interceptors;
    private Map<String, RouteVO> subRoutes;
    private String taskClass;//跳转匹配类
    private Map<String, Object> taskOptions;//路由目标

    public List<RouteOptionVO> getInterceptors() {
        if (null == interceptors || interceptors.isEmpty()) {
            interceptors = new ArrayList<>();
        }
        return interceptors;
    }

    public void setInterceptors(List<RouteOptionVO> interceptors) {
        this.interceptors = interceptors;
    }

    public Map<String, RouteVO> getSubRoutes() {
        return subRoutes;
    }

    public void setSubRoutes(Map<String, RouteVO> subRoutes) {
        this.subRoutes = subRoutes;
    }

}
