package com.veiljoy.veil;

import android.app.Application;

import com.veiljoy.veil.memory.ImageCache;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageCache.init(this);
    }
}
