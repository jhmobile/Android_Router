package com.jinhui365.router.data;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Name:RouteItemVO
 * Author:jmtian
 * Commemt:每个目标类对应的VO
 * Date: 2017/8/24 9:51
 */


public class RouteItemVO implements Serializable {
    private static final String TAG = "RouteItemVO";
    private String clazz;//类名
    private Map<String, Object> params;//传入当前类的参数
    private Map<String, Object> options;//在当前类使用的配置项

    public String getClazz() {
        return clazz;
    }

    public Class<?> getClazzName() {
        try {
            return Class.forName(clazz);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "========activity转class失败==========");
        }
        return null;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Map<String, Object> getParams() {
        if (null == params) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getOptions() {
        if (null == options) {
            options = new HashMap<>();
        }
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
