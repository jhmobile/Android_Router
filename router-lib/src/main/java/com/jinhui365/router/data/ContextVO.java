package com.jinhui365.router.data;

import com.jinhui365.router.route.RouteContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:jmtian
 * Commemt:一次路由对应的控制器，控制器有特殊实现，实现其子类
 * Date: 2017/8/23 14:44
 */


public class ContextVO implements Serializable {
    private static final String TAG = "ContextVO";

    private String clazz;//RouterContext子类
    private Map<String, String> options;//RouterContext子类需要的配置参数

    public String getClazzName() {
        return clazz;
    }

    /**
     * 获取vaseContext子类对应的class
     *
     * @return
     */
    public Class<RouteContext> getClazz() {
        Class<RouteContext> clz = null;
        try {
            clz = (Class<RouteContext>) Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clz;
    }

    public void setClazzName(String clazz) {
        this.clazz = clazz;
    }

    public Map<String, String> getOptions() {
        if (null == options) {
            options = new HashMap<>();
        }
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
