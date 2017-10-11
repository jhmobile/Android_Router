package com.jinhui365.router.data;

import com.jinhui365.router.interceptor.InterceptorImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:jmtian
 * Date: 2017/8/14 17:06
 * description: 每个拦截器对应的itemVO
 */


public class InterceptorVO implements Serializable {
    /**
     * 验证条件传入参数
     */
    private Map<String, String> params;
    /**
     * 验证条件配置项
     */
    private Map<String, String> options;
    /**
     * 前置条件类
     */
    private String interceptorClazz;

    public Map<String, String> getParams() {
        if (null == params) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getInterceptorClazzName() {
        return interceptorClazz;
    }

    public void setInterceptorClazz(String interceptorClazz) {
        this.interceptorClazz = interceptorClazz;
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

    /**
     * 获取验证类反射获得的class
     *
     * @return
     */
    public Class<InterceptorImpl> getInterceptorClazz() {
        try {
            return (Class<InterceptorImpl>) Class.forName(interceptorClazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
