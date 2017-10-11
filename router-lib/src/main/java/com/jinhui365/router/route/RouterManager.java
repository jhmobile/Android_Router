package com.jinhui365.router.route;

import android.text.TextUtils;

import com.jinhui365.router.data.ResultVO;
import com.jinhui365.router.data.RouteItemVO;
import com.jinhui365.router.utils.GsonUtils;

import java.util.List;
import java.util.Map;


/**
 * Author:jmtian
 * Commemt:Route数据初始化类
 * Date: 2017/7/24 14:53
 */
public class RouterManager {
    private static final String TAG = "RouterManager";

    private String pageJsonString;
    private Map<String, List<RouteItemVO>> pageMap;

    private static final RouterManager instance = new RouterManager();

    private RouterManager() {
    }

    public static RouterManager getInstance() {
        return instance;
    }

    public void init(String pageJsonString) {
        this.pageJsonString = pageJsonString;
    }

    /**
     * 根据pageID获取ResultVO
     * 不能共用解析结果，每次都要用最新的解析，否则就会出现一个跳转，里面有非初始化数据
     *
     * @param pageID
     * @return
     */
    public ResultVO getResultVOByPageID(String pageID) {
        return getResultVOByPageID(pageID, null);
    }

    public ResultVO getResultVOByPageID(String pageID, Map<String, String> params) {
        pageMap = GsonUtils.jsonToMapList(pageJsonString, String.class, RouteItemVO.class);
        if (TextUtils.isEmpty(pageID) || null == pageMap || pageMap.isEmpty()) {
            return null;
        }

        ResultVO itemVO = null;
        if (pageMap.containsKey(pageID)) {
            List<RouteItemVO> list = pageMap.get(pageID);
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
            Map<String, String> map = resultVO.getParams();
            map.putAll(params);
        }
        return resultVO;
    }
}
