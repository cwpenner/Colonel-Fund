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

    AppSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    synchronized AppSingleton getInstance(Context context) {
        return new AppSingleton(context);
    }

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

    <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public void cancelAll() {
        mRequestQueue.cancelAll(mRequestQueue);
        mRequestQueue.cancelAll(getRequestQueue());



    }


}