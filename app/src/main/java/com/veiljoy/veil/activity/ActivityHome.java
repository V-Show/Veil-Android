package com.veiljoy.veil.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.veiljoy.veil.BaseActivity;
import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.imof.IMOFChatImpl;
import com.veiljoy.veil.imof.LoginConfig;
import com.veiljoy.veil.imof.MUCHelper;
import com.veiljoy.veil.imof.MUCJoinTask;
import com.veiljoy.veil.imof.UserAccessManager;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityHome extends BaseActivity {


    IMUserBase.OnUserLogin mUserLoginTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final XMPPConnection connection=XmppConnectionManager.getInstance().getConnection();
//        MUCHelper.init(connection);
//
//
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (connection != null) {
//                    String username = SharePreferenceUtil.getName();
//                    String password = SharePreferenceUtil.getPasswd();
//                    try {
//                        Log.v("connection", "username " + username + " ,password " + password);
//                        if (!connection.isConnected())
//                            connection.connect();
//
//                        connection.login(username, password);
//                    } catch (Exception xee) {
//                        xee.printStackTrace();
//                        Log.v("connection", "login failed " + username + " ,password " + password);
//                    }
//
//                }
////////
////                MUCHelper.createRoom("veilGroup");
//////
////                new IMOFChatImpl().testHostRoom();
//
//               IMOFChatImpl.JoinRoom("");
//
//
//            }
//        }).start();


        init();
        enter();
    }




    public void init() {
        mUserLoginTask = new UserAccessManager(this);
        final XMPPConnection connection=XmppConnectionManager.getInstance().getConnection();
        MUCHelper.init(connection);


    }

    public void enter() {


        if (verifyAccount()) {

            Log.v("home", "verify account pass");
            new UserLoginTask().execute();

        } else {
            startActivity(ActivityRegister.class, null);
        }
    }

    public boolean verifyAccount() {

        //新用户检测
        if (SharePreferenceUtil.getName() == null || SharePreferenceUtil.getPasswd() == null) {

            return false;
        }
        //头像检测
        if (SharePreferenceUtil.getAvatar() == null) {

            return false;
        }

        //检测性别
        if (SharePreferenceUtil.getGender() == -1) {

            return false;
        }

        //账号和密码匹配检测

        return true;
    }


    class UserLoginTask extends AsyncTask<Integer, Integer, Integer> {


        @Override
        protected void onPreExecute() {
            mUserLoginTask.preLogin();

        }

        @Override
        protected Integer doInBackground(Integer... params) {


            String name = SharePreferenceUtil.getName();
            String psw = SharePreferenceUtil.getPasswd();

            return mUserLoginTask.onLogin(name, psw);


        }

        @Override
        protected void onPostExecute(Integer code) {

            Log.v("login", "login code " + code);

            switch (code) {

                case Constants.LOGIN_SUCCESS: // 登录成功
                    showCustomToast(R.string.login_success);
                    break;
                case Constants.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                    showCustomToast(R.string.message_invalid_username_password);
                    break;
                case Constants.SERVER_UNAVAILABLE:// 服务器连接失败
                    showCustomToast(R.string.message_server_unavailable);
                    break;
                case Constants.LOGIN_ERROR:// 未知异常
                    showCustomToast(R.string.unrecoverable_error);
                    break;
            }

            boolean rel = mUserLoginTask.onLoginResult(code);
            Log.v("home", "home rel " + rel);

            if (!rel) {

                startActivity(ActivityRegister.class, null);
                finish();
            } else {
                AppStates.setAlreadyLogined(true);
                new MUCJoinTask(null,ActivityHome.this).execute("");

            }

        }
    }


}
