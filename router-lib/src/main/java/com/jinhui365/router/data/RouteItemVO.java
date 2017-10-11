package com.jinhui365.router.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Name:RouteItemVO
 * Author:jmtian
 * Commemt:每个路由对应的item
 * Date: 2017/8/24 9:51
 */


public class RouteItemVO implements Serializable{
    private Map<String, String> condition;//选择result需要的匹配参数
    private ResultVO result;//pageID+Interceptor匹配的结果

    public Map<String, String> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, String> condition) {
        this.condition = condition;
    }

    public ResultVO getResult() {
        return result;
    }

    public void setResult(ResultVO result) {
        this.result = result;
    }
}
