package com.jinhui365.router.core;

import com.jinhui365.router.utils.JsonUtil;

import java.util.List;
import java.util.Map;


/**
 * Author:jmtian
 * Commemt:Route数据初始化类
 * Date: 2017/7/24 14:53
 */
public class ConfigManager {
    private static final String TAG = "ConfigManager";

    private Map<String, RouteVO> configRouteMap;

    private static final ConfigManager instance = new ConfigManager();

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    /**
     * init data
     *
     * @param pageJsonString
     */
    public void init(String pageJsonString) {
        //初始化默认配置文件
        configRouteMap = JsonUtil.jsonToMap(pageJsonString, String.class, RouteVO.class);

    }

    //合并传递过来的部分配置文件
    public void mergeConfig(String jsonString) {
        Map<String, RouteVO> newMap = JsonUtil.jsonToMap(jsonString, String.class, RouteVO.class);
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
    private RouteVO getResultVOByCondition(List<RouteOptionVO> list, Map<String, String> params) {
        RouteVO resultVO = null;

        return resultVO;
    }

    /**
     * 根据下标获取验证类
     *
     * @param index
     * @return
     */
    public AbsInterceptor getInterceptorByIndex(RouteContext context, int index) {
//        if (null == interceptors || interceptors.isEmpty()) {
//            return null;
//        }
//        if (index < interceptors.size()) {
//            InterceptorVO interceptorVO = interceptors.get(index);
//            try {
//                AbsInterceptor vo = interceptorVO.getInterceptorClazz()
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
    public List<AbsInterceptor> getInterceptors(RouteContext routeContext) {
//        List<AbsInterceptor> list = new ArrayList<>();
//        if (null == interceptors || interceptors.isEmpty()) {
//            return list;
//        }
//        for (int i = 0; i < interceptors.size(); i++) {
//            list.add(getInterceptorByIndex(routeContext, i));
//        }
        return null;
    }


}
