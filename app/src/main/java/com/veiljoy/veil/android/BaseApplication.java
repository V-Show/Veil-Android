package com.veiljoy.veil.android;

import android.app.Application;

import com.veiljoy.veil.memory.ImageCache;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharePreferenceUtil.InitPreferenceUtil(this, Constants.USER_USER_PROFILE_PATH_KEY);
        ImageCache.init(this);
    }

    public void exit(){
        XmppConnectionManager.getInstance().disconnect();
        SharePreferenceUtil.setStatus(Constants.LOGIN_ERROR);
        SharePreferenceUtil.setRoom("null");
        System.exit(0);
    }



    public void enter(){
        SharePreferenceUtil.setStatus(Constants.LOGIN_SUCCESS);
    }




}
