package com.jinhui365.router.route;

import android.net.Uri;
import android.support.annotation.Nullable;

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

    private Class targetClass;
    private Map<String, Object> params;
    private Map<String, Object> options;
    private int flags;
    private List<InterceptorImpl> interceptors;
    // skip all the interceptors
    private boolean skipInterceptors;
    // skip some interceptors temporarily
    @Nullable
    private List<String> removedInterceptors;
    // add some interceptors temporarily
    @Nullable
    private List<String> addedInterceptors;
    @Nullable
    private RouteCallback callback;
    private int requestCode = INVALID_REQUEST_CODE;
    private int enterAnim;
    private int exitAnim;


    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
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
    public List<String> getAddedInterceptors() {
        return addedInterceptors;
    }

    @Nullable
    public List<String> getRemovedInterceptors() {
        return removedInterceptors;
    }

    public void addInterceptors(String... interceptors) {
        if (interceptors == null || interceptors.length <= 0) {
            return;
        }
        if (this.addedInterceptors == null) {
            this.addedInterceptors = new ArrayList<>(interceptors.length);
        }
        this.addedInterceptors.addAll(Arrays.asList(interceptors));
    }

    public void removeInterceptors(String... interceptors) {
        if (interceptors == null || interceptors.length <= 0) {
            return;
        }
        if (this.removedInterceptors == null) {
            this.removedInterceptors = new ArrayList<>(interceptors.length);
        }
        this.removedInterceptors.addAll(Arrays.asList(interceptors));
    }

    @Nullable
    public RouteCallback getCallback() {
        return callback;
    }

    public void setCallback(@Nullable RouteCallback callback) {
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
