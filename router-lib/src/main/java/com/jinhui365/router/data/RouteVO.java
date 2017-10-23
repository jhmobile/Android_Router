package com.jinhui365.router.data;

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

    private List<RouteItemVO> interceptors;
    private Map<String, RouteVO> subRoutes;
    private RouteItemVO matcher;//跳转匹配类
    private Map<String, String> target;//路由目标

    public List<RouteItemVO> getInterceptors() {
        if (null == interceptors || interceptors.isEmpty()) {
            interceptors = new ArrayList<>();
        }
        return interceptors;
    }

    public void setInterceptors(List<RouteItemVO> interceptors) {
        this.interceptors = interceptors;
    }

    public Map<String, RouteVO> getSubRoutes() {
        return subRoutes;
    }

    public void setSubRoutes(Map<String, RouteVO> subRoutes) {
        this.subRoutes = subRoutes;
    }

    public RouteItemVO getMatcher() {
        return matcher;
    }

    public void setMatcher(RouteItemVO matcher) {
        this.matcher = matcher;
    }

    public Map<String, String> getTarget() {
        return target;
    }

    public void setTarget(Map<String, String> target) {
        this.target = target;
    }
}
