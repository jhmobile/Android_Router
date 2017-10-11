package com.jinhui365.router.interceptor;

import com.jinhui365.router.interceptor.InterceptorImpl;

import java.util.Map;

/**
 * Interceptor table mapping.
 * <p>
 * Created by Enyu Chen on 2017/6/30.
 */
public interface InterceptorTable {
    /**
     * Mapping between name and interceptor.
     *
     * @param map name -> interceptor.
     */
    void handle(Map<String, Class<? extends InterceptorImpl>> map);
}
