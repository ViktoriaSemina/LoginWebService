package com.example.viktoriasemina.webservicejson;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AndroidLoginController extends Application {

    //Initialize objects for Request queue - AndroidLoginController

    private static AndroidLoginController mInstance;
    private RequestQueue mRequestQueue;
    public static final String TAG = AndroidLoginController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AndroidLoginController getmInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
}
