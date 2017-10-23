package com.jinhui365.router.route;

import com.jinhui365.router.data.RouteItemVO;
import com.jinhui365.router.data.RouteVO;
import com.jinhui365.router.interceptor.InterceptorImpl;
import com.jinhui365.router.matcher.ActivityMatcher;
import com.jinhui365.router.utils.GsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author:jmtian
 * Commemt:Route数据初始化类
 * Date: 2017/7/24 14:53
 */
public class RouteManager {
    private static final String TAG = "RouteManager";

    private Map<String, RouteVO> configRouteMap;

    private static final RouteManager instance = new RouteManager();

    private RouteManager() {
    }

    public static RouteManager getInstance() {
        return instance;
    }

    /**
     * init data
     *
     * @param pageJsonString
     */
    public void init(String pageJsonString) {
        //初始化默认配置文件
        configRouteMap = GsonUtils.jsonToMap(pageJsonString, String.class, RouteVO.class);
    }

    //合并传递过来的部分配置文件
    public void mergeConfig(String jsonString) {
        Map<String, RouteVO> newMap = GsonUtils.jsonToMap(jsonString, String.class, RouteVO.class);
        configRouteMap.putAll(newMap);
    }

    /**
     * 根据path获取ResultVO
     *
     * @param path
     * @return
     */
    public RouteVO getResultVOByRoutePath(String path) {
        return getResultVOByRoutePath(path, null);
    }

    /**
     * 根据path 和params获取RouteVO
     *
     * @param path
     * @param params
     * @return
     */
    public RouteVO getResultVOByRoutePath(String path, Map<String, String> params) {
        RouteVO routeVO = new RouteVO();

        return routeVO;
    }

    /**
     * 根据Condition匹配ResultVO
     *
     * @param list
     * @param params
     * @return
     */
    private RouteVO getResultVOByCondition(List<RouteItemVO> list, Map<String, String> params) {
        RouteVO resultVO = null;

        return resultVO;
    }

    /**
     * 根据下标获取验证类
     *
     * @param index
     * @return
     */
    public InterceptorImpl getInterceptorByIndex(RouteContext context, int index) {
//        if (null == interceptors || interceptors.isEmpty()) {
//            return null;
//        }
//        if (index < interceptors.size()) {
//            InterceptorVO interceptorVO = interceptors.get(index);
//            try {
//                InterceptorImpl vo = interceptorVO.getInterceptorClazz()
//                        .getConstructor(RouteContext.class, Map.class, Map.class)
//                        .newInstance(context, interceptorVO.getParams(), interceptorVO.getOptions());
//                return vo;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    /**
     * 实例化Interceptor集合
     *
     * @param routeContext
     * @return
     */
    public List<InterceptorImpl> getInterceptors(RouteContext routeContext) {
//        List<InterceptorImpl> list = new ArrayList<>();
//        if (null == interceptors || interceptors.isEmpty()) {
//            return list;
//        }
//        for (int i = 0; i < interceptors.size(); i++) {
//            list.add(getInterceptorByIndex(routeContext, i));
//        }
        return null;
    }


}
