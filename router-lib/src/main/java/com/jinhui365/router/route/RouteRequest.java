package com.jinhui365.router.route;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.jinhui365.router.data.RouteItemVO;
import com.jinhui365.router.data.RouteVO;
import com.jinhui365.router.interceptor.InterceptorImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name:RouteRequest
 * Author:jmtian
 * Commemt:Route request object.
 * Date: 2017/10/16 14:39
 */

public class RouteRequest implements Serializable {
    private static final int INVALID_REQUEST_CODE = -1;

    private Uri uri;

    private Map<String,Object> target;
    private Map<String, Object> params;
    private Map<String, Object> options;
    private int flags;
    private List<InterceptorImpl> interceptors;
    // skip all the interceptors
    private boolean skipInterceptors;
    // skip some interceptors temporarily
    private List<RouteItemVO> removedInterceptors;
    // add some interceptors temporarily
    @Nullable
    private List<RouteItemVO> addedInterceptors;
    @Nullable
    private RouteCallback callback;
    private IErrorHandler handler;
    private int requestCode = INVALID_REQUEST_CODE;
    private int enterAnim;
    private int exitAnim;


    public Map<String, Object> getTarget() {
        return target;
    }

    public void setTarget(Map<String, Object> target) {
        this.target = target;
    }

    public RouteRequest(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
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

    public List<InterceptorImpl> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<InterceptorImpl> interceptors) {
        this.interceptors = interceptors;
    }

    public boolean isSkipInterceptors() {
        return skipInterceptors;
    }

    public void setSkipInterceptors(boolean skipInterceptors) {
        this.skipInterceptors = skipInterceptors;
    }

    @Nullable
    public List<RouteItemVO> getAddedInterceptors() {
        return addedInterceptors;
    }

    @Nullable
    public List<RouteItemVO> getRemovedInterceptors() {
        return removedInterceptors;
    }

    public void addInterceptors(Class<InterceptorImpl> clazz, Map<String, Object> params, Map<String, Object> options, int index) {
        if (this.addedInterceptors == null) {
            this.addedInterceptors = new ArrayList<>();
        }
        this.addedInterceptors.add(new RouteItemVO());
    }

    public void removeInterceptors(Class<InterceptorImpl>... interceptors) {
        if (this.removedInterceptors == null) {
            this.removedInterceptors = new ArrayList<>();
        }
        this.removedInterceptors.add(new RouteItemVO());
    }

    @Nullable
    public RouteCallback getCallback() {
        return callback;
    }

    public void setCallback(@Nullable RouteCallback callback) {
        this.callback = callback;
    }

    public IErrorHandler getHandler() {
        return handler;
    }

    public void setHandler(IErrorHandler handler) {
        this.handler = handler;
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
