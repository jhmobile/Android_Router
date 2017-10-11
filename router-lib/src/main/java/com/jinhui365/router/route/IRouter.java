package com.jinhui365.router.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.AnimRes;

import java.util.Map;

/**
 * Name:IRouter
 * Author:jmtian
 * Commemt:Router interface.
 * Date: 2017/9/22 14:20
 */

public interface IRouter {
    IRouter build(Uri uri);

    /**
     * the route parent
     * @param controller
     * @return
     */
    IRouter parent(RouteController controller);

    IRouter callback(RouteCallback callback);

    /**
     * Call <code>startActivityForResult</code>.
     */
    IRouter requestCode(int requestCode);

    /**
     * bundle.putXXX(String key, XXX value).
     */
    IRouter with(String key, Object value);

    /**
     * @param map
     * @return
     */
    IRouter with(Map<String, Object> map);

    IRouter with(String json);
    /**
     * @see Intent#addFlags(int)
     */
    IRouter addFlags(int flags);

    /**
     * @see android.app.Activity#overridePendingTransition(int, int)
     */
    IRouter anim(@AnimRes int enterAnim, @AnimRes int exitAnim);

    /**
     * Skip all the interceptors.
     */
    IRouter skipInterceptors();

    /**
     * Skip the named interceptors.
     */
    IRouter skipInterceptors(String... interceptors);

    /**
     * Add interceptors temporarily for current route request.
     */
    IRouter addInterceptors(String... interceptors);

    Intent getIntent(Context context);

    void go(Context context, RouteCallback callback);

    void go(Context context);

}
