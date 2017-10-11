package com.jinhui365.router.route;

import android.net.Uri;

/**
 * Name:RouteCallback
 * Author:jmtian
 * Commemt: RouteCallback interface
 * Date: 2017/9/22 14:21
 */

public interface RouteCallback {
    /**
     * Callback
     *
     * @param state   {@link RouteResult}
     * @param uri     Uri
     * @param message notice msg
     */
    void callback(RouteResult state, Uri uri, String message);
}
