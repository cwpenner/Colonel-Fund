package com.colonelfund.colonelfund;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * For making calls to remote DB API
 */
class AppSingleton {
    private RequestQueue mRequestQueue;
    private Context mContext;

    /**
     * Basic constructor
     *
     * @param context of activity.
     */
    AppSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Get instance of app.
     *
     * @param context of activity.
     * @return app singleton.
     */
    synchronized AppSingleton getInstance(Context context) {
        return new AppSingleton(context);
    }

    /**
     * Request queue for app.
     *
     * @return request queue.
     */
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.

            // TODO: 12/21/2017 com.android.volley.toolbox.StringRequest is causing memory leaks
            // using the request in this way is causing a memory leak. follow the StackOverflow
            // guide to solve this issue
            // https://stackoverflow.com/questions/39630712/anonymous-listener-of-volley-request-causing-memory-leak
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    /**
     * Adds request to queue
     *
     * @param req of request.
     * @param tag of request.
     * @param <T> of request.
     */
    <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    /**
     * Cancels all requests in queue.
     */
    public void cancelAll() {
        mRequestQueue.cancelAll(mRequestQueue);
        mRequestQueue.cancelAll(getRequestQueue());
    }
}