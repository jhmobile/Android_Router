package com.jinhui365.router.data;

import android.util.Log;
import com.jinhui365.router.interceptor.InterceptorImpl;
import com.jinhui365.router.route.RouteContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name:ResultVO
 * Author:jmtian
 * Commemt:一次路由跳转需要的信息
 * Date: 2017/8/24 9:44
 */


public class ResultVO implements Serializable {
    private static final String TAG = "ResultVO";

    private String activity;//activity Class
    private Map<String, Object> params;//传入activity的参数
    private String group;//路由所属分组
    private List<InterceptorVO> interceptors;//拦截器数组
    private ContextVO rContext;//路由控制器

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<InterceptorVO> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<InterceptorVO> interceptors) {
        this.interceptors = interceptors;
    }

    public ContextVO getRContext() {
        return rContext;
    }

    public void setRContext(ContextVO rContext) {
        this.rContext = rContext;
    }

    /**
     * 获取activity
     *
     * @return
     */
    public Class<?> getActivityClass() {
        try {
            return Class.forName(activity);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "========activity转class失败==========");
        }
        return null;
    }

    /**
     * 根据下标获取验证类
     *
     * @param index
     * @return
     */
    public InterceptorImpl getInterceptorByIndex(RouteContext context, int index) {
        if (null == interceptors || interceptors.isEmpty()) {
            return null;
        }
        if (index < interceptors.size()) {
            InterceptorVO interceptorVO = interceptors.get(index);
            try {
                InterceptorImpl vo = interceptorVO.getInterceptorClazz()
                        .getConstructor(RouteContext.class, Map.class, Map.class)
                        .newInstance(context, interceptorVO.getParams(), interceptorVO.getOptions());
                return vo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 实例化Interceptor集合
     *
     * @param routeContext
     * @return
     */
    public List<InterceptorImpl> getInterceptors(RouteContext routeContext) {
        List<InterceptorImpl> list = new ArrayList<>();
        if (null == interceptors || interceptors.isEmpty()) {
            return list;
        }
        for (int i = 0; i < interceptors.size(); i++) {
            list.add(getInterceptorByIndex(routeContext, i));
        }
        return list;
    }

    /**
     * 获取权限组的数量
     *
     * @return
     */
    public int getInterceptorSize() {
        return null == interceptors ? 0 : interceptors.size();
    }

}
