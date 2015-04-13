package com.veiljoy.veil.im;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class IMUserBase {




    public static interface OnUserLogin{

        void preLogin();

        int onLogin(String name,String psw);

        void onLoginResult(int code);
    }

    public static interface OnUserRegister{

        void onPreRegister();

        int onRegister(String name,String psw);

        boolean onRegisterResult(int code);
    }




}
