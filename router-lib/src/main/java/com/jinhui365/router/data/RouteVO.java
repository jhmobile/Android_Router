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
    private List<RouteItemVO> interceptors;
    private Map<String, RouteVO> subRoutes;
    private RouteItemVO target;
    private RouteItemVO routeContext;

    public List<RouteItemVO> getInterceptors() {
        if(null == interceptors || interceptors.isEmpty()){
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
}
