package com.jinhui365.router.core;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name:RouteRequest
 * Author:jmtian
 * Commemt:RouteContext request object.
 * Date: 2017/10/16 14:39
 */

public class RouteRequest implements Serializable {
    private static final int INVALID_REQUEST_CODE = -1;

    private Uri url;

    private Map<String,Object> target;
    private Map<String, Object> params;
    private Map<String, Object> options;
    private int flags;
    private List<AbsInterceptor> interceptors;
    // skip all the interceptors
    private boolean skipInterceptors;
    // skip some interceptors temporarily
    private List<RouteOptionVO> removedInterceptors;
    // add some interceptors temporarily
    @Nullable
    private List<RouteOptionVO> addedInterceptors;
    @Nullable
    private IRouteCallBack callback;

    private int requestCode = INVALID_REQUEST_CODE;
    private int enterAnim;
    private int exitAnim;


    public Map<String, Object> getTarget() {
        return target;
    }

    public void setTarget(Map<String, Object> target) {
        this.target = target;
    }

    public RouteRequest(Uri url) {
        this.url = url;
    }

    public Uri getUri() {
        return url;
    }

    public void setUri(Uri url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        if (null == params || params.isEmpty()) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getOptions() {
        if (null == options || options.isEmpty()) {
            options = new HashMap<>();
        }
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void addFlags(int flags) {
        this.flags |= flags;
    }

    public List<AbsInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<AbsInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public boolean isSkipInterceptors() {
        return skipInterceptors;
    }

    public void setSkipInterceptors(boolean skipInterceptors) {
        this.skipInterceptors = skipInterceptors;
    }

    @Nullable
    public List<RouteOptionVO> getAddedInterceptors() {
        return addedInterceptors;
    }

    @Nullable
    public List<RouteOptionVO> getRemovedInterceptors() {
        return removedInterceptors;
    }

    public void addInterceptors(Class<AbsInterceptor> clazz,Map<String, Object> options, int index) {
        if (this.addedInterceptors == null) {
            this.addedInterceptors = new ArrayList<>();
        }
        this.addedInterceptors.add(new RouteOptionVO());
    }

    public void removeInterceptors(Class<AbsInterceptor>... interceptors) {
        if (this.removedInterceptors == null) {
            this.removedInterceptors = new ArrayList<>();
        }
        this.removedInterceptors.add(new RouteOptionVO());
    }

    @Nullable
    public IRouteCallBack getCallback() {
        return callback;
    }

    public void setCallback(@Nullable IRouteCallBack callback) {
        this.callback = callback;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        if (requestCode < 0) {
            this.requestCode = INVALID_REQUEST_CODE;
        } else {
            this.requestCode = requestCode;
        }
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public void setEnterAnim(int enterAnim) {
        this.enterAnim = enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public void setExitAnim(int exitAnim) {
        this.exitAnim = exitAnim;
    }

}
