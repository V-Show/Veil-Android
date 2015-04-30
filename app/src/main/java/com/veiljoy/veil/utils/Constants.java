package com.veiljoy.veil.utils;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    /**
     * 服务器的配置
     */
    public static final String LOGIN_SET = "eim_login_set";// 登录设置
    public static final String USERNAME = "username";// 账户
    public static final String PASSWORD = "password";// 密码
    public static final String XMPP_HOST = "xmpp_host";// 地址
    public static final String XMPP_PORT = "xmpp_port";// 端口
    public static final String XMPP_SEIVICE_NAME = "xmpp_service_name";// 服务名
    public static final String IS_AUTOLOGIN = "isAutoLogin";// 是否自动登录
    public static final String IS_NOVISIBLE = "isNovisible";// 是否隐身
    public static final String IS_REMEMBER = "isRemember";// 是否记住账户密码
    public static final String IS_FIRSTSTART = "isFirstStart";// 是否首次启动

    public static final String XMPP_HOST_IP = "115.28.231.161";
//    public static final String XMPP_HOST_IP = "10.0.0.107";
    public static final String XMPP_HOST_NAME = "veiljoy.com";
    public static final String XMPP_HOST_PORT = "5222";
    public static final String XMPP_SERVICE_NAME = "veil";

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


    /**
     * 用户默认密码
     */
    public static final String USER_DEFAULT_PASSWORD = "ABCabc123";


    /**
     * 精确到毫秒
     */
    public static final String MS_FORMART = "yyyy-MM-dd HH:mm:ss SSS";


    /**
     * 收到好友邀请请求
     */
    public static final String ROSTER_SUBSCRIPTION = "roster.subscribe";
    public static final String ROSTER_SUB_FROM = "roster.subscribe.from";
    public static final String NOTICE_ID = "notice.id";

    public static final String NEW_MESSAGE_ACTION = "roster.newmessage";

    /**
     * 我的消息
     */
    public static final String MY_NEWS = "my.news";
    public static final String MY_NEWS_DATE = "my.news.date";

    /*
    * 默认房间jid
    * */
    //public static final String DEFAULT_ROOM_JID = "veilgroup@conference.veil";
    public static final String DEFAULT_ROOM_JID = "veilgroup";
    /*
    *
    * */
    public static final String MAP_ROOM_NAME_KEY = "roomName";
    public static final String MAP_ROOM_JID_KEY = "roomJid";

    /*
    * 语音路径
    * */
    public final static String VOICE_AUDIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;


    /**
     * 个人名片属性
     * *
     */
    public final static String USER_CARD_FILED_NAME = "user_name";
    public final static String USER_CARD_FILED_AVATAR = "user_avatar";
    public final static String USER_CARD_FILED_GENDER = "user_gender";
    public final static String USER_CARD_FILED_PHONE = "user_phone";
    public final static String USER_CARD_FILED_DESC = "user_desc";
    public final static int USER_GENDER_MALE = 0;
    public final static int USER_GENDER_FEMALE = 1;

    /**
     * *
     */
    public static final String XMPP_HOST_NAME_PORT = XMPP_HOST_NAME + ":" + XMPP_HOST_PORT;

}
