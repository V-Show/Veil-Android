package com.veiljoy.veil;

import android.app.Application;

import com.veiljoy.veil.memory.ImageCache;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

       SharePreferenceUtil. InitPreferenceUtil(this, Constants.USER_USER_PROFILE_PATH_KEY);
        ImageCache.init(this);
    }
}
