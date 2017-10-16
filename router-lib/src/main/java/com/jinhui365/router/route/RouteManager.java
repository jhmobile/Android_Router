package com.jinhui365.router.route;

import android.text.TextUtils;

import com.jinhui365.router.data.ResultVO;
import com.jinhui365.router.data.RouteItemVO;
import com.jinhui365.router.utils.GsonUtils;

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

    private Map<String, List<RouteItemVO>> configRouteMap;
    private Map<String, List<RouteItemVO>> annotationRouteMap;

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
        if (TextUtils.isEmpty(pageJsonString)) {//初始化注解，只需要启动应用的时候进行初始化

        }

        //初始化热更获取的配置文件
        configRouteMap = GsonUtils.jsonToMapList(pageJsonString, String.class, RouteItemVO.class);
    }

    /**
     * 根据path获取ResultVO
     *
     * @param path
     * @return
     */
    public ResultVO getResultVOByRoutePath(String path) {
        return getResultVOByRoutePath(path, null);
    }

    /**
     * 根据path 和params获取ResultVO
     *
     * @param path
     * @param params
     * @return
     */
    public ResultVO getResultVOByRoutePath(String path, Map<String, String> params) {
        Map<String, List<RouteItemVO>> routeMap = getRouteMap();
        if (TextUtils.isEmpty(path) || null == routeMap || routeMap.isEmpty()) {
            return null;
        }

        ResultVO itemVO = null;
        if (routeMap.containsKey(path)) {
            List<RouteItemVO> list = routeMap.get(path);
            itemVO = getResultVOByCondition(list, params);
        }
        return itemVO;
    }

    /**
     * 根据Condition匹配ResultVO
     *
     * @param list
     * @param params
     * @return
     */
    private ResultVO getResultVOByCondition(List<RouteItemVO> list, Map<String, String> params) {
        ResultVO resultVO = null;
        if (null == list || list.isEmpty()) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            boolean isExist = true;
            RouteItemVO itemVO = list.get(i);
            Map<String, String> map = itemVO.getCondition();
            if (null != map && !map.isEmpty()) {
                for (String key : map.keySet()) {
                    String value = map.get(key);
                    String valueParams = params.get(key);
                    if (null == value || null == valueParams || !value.equals(valueParams)) {
                        isExist = false;
                        break;
                    }
                }
            }
            if (isExist) {
                resultVO = itemVO.getResult();
                break;
            }
        }
        if (null != resultVO && null != params && !params.isEmpty()) {
            Map<String, Object> map = resultVO.getParams();
            map.putAll(params);
        }
        return resultVO;
    }

    /**
     * 获取路由信息初始化数据集合
     *
     * @return
     */
    private Map<String, List<RouteItemVO>> getRouteMap() {
        Map<String, List<RouteItemVO>> map = new HashMap<>();
        if (null != annotationRouteMap && !annotationRouteMap.isEmpty()) {
            map.putAll(annotationRouteMap);
        }
        if (null != configRouteMap && !configRouteMap.isEmpty()) {
            map.putAll(configRouteMap);
        }
        return map;
    }
}
