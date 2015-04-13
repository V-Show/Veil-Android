package com.veiljoy.veil.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhongqihong on 15/4/1.
 */
public class Constants {


    /**
     * 注册提示
     */

    public static final int REGISTER_RESULT_SUCCESS = 0;
    public static final int REGISTER_RESULT_FAIL = 1;
    public static final int REGISTER_RESULT_EXIST = 2;

    /**
     * 登录提示
     */
    public static final int LOGIN_SUCCESS = 0;// 成功
    public static final int HAS_NEW_VERSION = 1;// 发现新版本
    public static final int IS_NEW_VERSION = 2;// 当前版本为最新
    public static final int LOGIN_ERROR_ACCOUNT_PASS = 3;// 账号或者密码错误
    public static final int SERVER_UNAVAILABLE = 4;// 无法连接到服务器
    public static final int LOGIN_ERROR = -1;// 连接失败


    public static final String XMPP_HOST_IP = "115.28.231.161";
    public static final String XMPP_HOST_NAME = "www.veiljoy.com";
    public static final String XMPP_HOST_PORT = "5222";


    public static final String SERVER_IP = XMPP_HOST_IP;
    public static final int SERVER_PORT = 81;


    /*
     * 注册提示
	 * */
    public static final int LOGIN_ERROR_ACCOUNT_EXIST = 6;// 账号已经存在
    public static final int LOGIN_ERROR_ACCOUNT_REGISTER_FAIL = 7;// 注册失败
    public static final int LOGIN_ERROR_ACCOUNT_REGISTER_SUCCESS = 8;// 注册失败
    public static final String XMPP_CONNECTION_CLOSED = "xmpp_connection_closed";// 连接中断
    public static final String LOGIN = "login"; // 登录
    public static final String RELOGIN = "relogin"; // 重新登录


    public static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "ripple" + File.separator + "Avatar" + File.separator;

    public static final String USER_AVATAR_FILE_PATH_KEY = "USER_AVATAR_FILE_PATH_KEY";

    public static final String USER_USER_PROFILE_PATH_KEY = "USER_USER_PROFILE_PATH_KEY";


    public static final String USER_DEFAULT_PASSWORD = "ABCabc123";
}
