package com.jinhui365.router.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.jinhui365.router.interceptor.InterceptorImpl;
import com.jinhui365.router.utils.RLog;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RealRouter extends AbsRouter {
    private static RealRouter sInstance;
    private static final String PARAM_CLASS_SUFFIX = "$$Router$$ParamInjector";

    private Map<String, InterceptorImpl> mInterceptorInstance = new HashMap<>();


    private RealRouter() {
    }

    static synchronized RealRouter getInstance() {
        if (sInstance == null) {
            sInstance = new RealRouter();
        }
        return sInstance;
    }

    /**
     * Auto inject params from bundle.
     *
     * @param obj Activity or Fragment.
     */
    void injectParams(Object obj) {
        if (obj instanceof Activity || obj instanceof Fragment || obj instanceof android.app.Fragment) {
            String key = obj.getClass().getCanonicalName();
            Class<ParamInjector> clz;
            if (!AptHub.injectors.containsKey(key)) {
                try {
                    //noinspection unchecked
                    clz = (Class<ParamInjector>) Class.forName(key + PARAM_CLASS_SUFFIX);
                    AptHub.injectors.put(key, clz);
                } catch (ClassNotFoundException e) {
                    RLog.e("Inject params failed.", e);
                    return;
                }
            } else {
                clz = AptHub.injectors.get(key);
            }
            try {
                ParamInjector injector = clz.newInstance();
                injector.inject(obj);
            } catch (Exception e) {
                RLog.e("Inject params failed.", e);
            }
        } else {
            RLog.e("The obj you passed must be an instance of Activity or Fragment.");
        }
    }

    private void callback(RouteResult result, String msg) {
        if (result != RouteResult.SUCCEED) {
            RLog.w(msg);
        }
        if (mRouteRequest.getCallback() != null) {
            mRouteRequest.getCallback().callback(result, mRouteRequest.getUri(), msg);
        }
    }

    @Override
    public Intent getIntent(Context context) {
        if (mRouteRequest.getUri() == null) {
            callback(RouteResult.FAILED, "uri == null.");
            return null;
        }

        List<AbsMatcher> matcherList = MatcherRegistry.getMatcher();
        if (matcherList.isEmpty()) {
            callback(RouteResult.FAILED, "The MatcherRegistry contains no Matcher.");
            return null;
        }

        Set<Map.Entry<String, Class<?>>> entries = AptHub.routeTable.entrySet();

        for (AbsMatcher matcher : matcherList) {
            if (AptHub.routeTable.isEmpty()) { // implicit totally.
                if (matcher.match(context, mRouteRequest.getUri(), null, mRouteRequest)) {
                    RLog.i("Caught by " + matcher.getClass().getCanonicalName());
                    return finalizeIntent(context, matcher, null);
                }
            } else {
                for (Map.Entry<String, Class<?>> entry : entries) {
                    if (matcher.match(context, mRouteRequest.getUri(), entry.getKey(), mRouteRequest)) {
                        RLog.i("Caught by " + matcher.getClass().getCanonicalName());
                        return finalizeIntent(context, matcher, entry.getValue());
                    }
                }
            }
        }

        callback(RouteResult.FAILED, String.format(
                "Can not find an Activity that matches the given uri: %s", mRouteRequest.getUri()));
        return null;
    }

    /**
     * Do intercept and then generate intent by the given matcher, finally assemble extras.
     *
     * @param context Context
     * @param matcher current matcher
     * @param target  route target
     * @return Finally intent.
     */
    private Intent finalizeIntent(Context context, AbsMatcher matcher, @Nullable Class<?> target) {
        if (intercept(context, target)) {
            return null;
        }
        Object intent = matcher.generate(context, mRouteRequest.getUri(), target);
        if (intent instanceof Intent) {
            assembleIntent((Intent) intent);
            return (Intent) intent;
        } else {
            callback(RouteResult.FAILED, String.format(
                    "The matcher can't generate an intent for uri: %s",
                    mRouteRequest.getUri().toString()));
            return null;
        }
    }

    private void assembleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        if (mRouteRequest.getExtras() != null && !mRouteRequest.getExtras().isEmpty()) {
            intent.putExtras(mRouteRequest.getExtras());
        }
        if (mRouteRequest.getFlags() != 0) {
            intent.addFlags(mRouteRequest.getFlags());
        }
    }

    /**
     * Find interceptors
     *
     * @param context Context
     * @param target  target page.
     * @return True if intercepted, false otherwise.
     */
    private boolean intercept(Context context, @Nullable Class<?> target) {
        if (mRouteRequest.isSkipInterceptors()) {
            return false;
        }
        // Assemble final interceptors
        List<String> finalInterceptors = new ArrayList<>();
        if (target != null) {
            // 1. Add original interceptors in Map
            String[] baseInterceptors = AptHub.targetInterceptors.get(target);
            if (baseInterceptors != null && baseInterceptors.length > 0) {
                Collections.addAll(finalInterceptors, baseInterceptors);
            }
            // 2. Skip temp removed interceptors
            if (mRouteRequest.getRemovedInterceptors() != null) {
                finalInterceptors.removeAll(mRouteRequest.getRemovedInterceptors());
            }
        }
        // 3. Add temp added interceptors
        if (mRouteRequest.getAddedInterceptors() != null) {
            finalInterceptors.addAll(mRouteRequest.getAddedInterceptors());
        }

        if (!finalInterceptors.isEmpty()) {
            for (String name : finalInterceptors) {
                InterceptorImpl interceptor = mInterceptorInstance.get(name);
                if (interceptor == null) {
                    Class<? extends InterceptorImpl> clz = AptHub.interceptorTable.get(name);
                    try {
                        Constructor<? extends InterceptorImpl> constructor = clz.getConstructor();
                        interceptor = constructor.newInstance();
                        mInterceptorInstance.put(name, interceptor);
                    } catch (Exception e) {
                        RLog.e("Can't construct a interceptor with name: " + name);
                        e.printStackTrace();
                    }
                }
                // do intercept
                if (interceptor != null && interceptor.intercept(context, mRouteRequest)) {
                    callback(RouteResult.INTERCEPTED, String.format(
                            "Intercepted: {uri: %s, interceptor: %s}",
                            mRouteRequest.getUri().toString(), name));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void go(Context context) {
        Intent intent = getIntent(context);
        if (intent == null) {
            return;
        }

        Bundle options = mRouteRequest.getActivityOptionsCompat() == null ?
                null : mRouteRequest.getActivityOptionsCompat().toBundle();

        if (context instanceof Activity) {
            ActivityCompat.startActivityForResult((Activity) context, intent,
                    mRouteRequest.getRequestCode(), options);

            if (mRouteRequest.getEnterAnim() != 0 && mRouteRequest.getExitAnim() != 0) {
                // Add transition animation.
                ((Activity) context).overridePendingTransition(
                        mRouteRequest.getEnterAnim(), mRouteRequest.getExitAnim());
            }
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startActivity(context, intent, options);
        }

        callback(RouteResult.SUCCEED, null);
    }
}
