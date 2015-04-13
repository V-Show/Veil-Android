package com.veiljoy.veil.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.veiljoy.veil.BaseActivity;
import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.imof.LoginConfig;
import com.veiljoy.veil.imof.UserAccessManager;
import com.veiljoy.veil.utils.SharePreferenceUtil;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityHome extends BaseActivity {


    boolean isNewUser=false;
    IMUserBase.OnUserLogin mUserLoginTask;
    LoginConfig loginConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enter();
    }


    public void init(){
        mUserLoginTask=new UserAccessManager(this);
    }

    public void enter(){


        if(isNewUser){

            startActivity(ActivityRegister.class,null);
        }
        else{
            new UserLoginTask().execute(loginConfig);
        }
    }


    class UserLoginTask extends AsyncTask<LoginConfig,Integer,Integer> {


        @Override
        protected void onPreExecute() {
            mUserLoginTask.preLogin();

        }

        @Override
        protected Integer doInBackground(LoginConfig... params) {

            //LoginConfig loginConfig = params[0];

            SharePreferenceUtil.setName("AAAAAAAAAA");
            SharePreferenceUtil.setPasswd("BBBBBBBBB");
            return mUserLoginTask.onLogin("AAAAAAAAAA", "BBBBBBBBB");


        }

        @Override
        protected void onPostExecute(Integer code) {

            mUserLoginTask.onLoginResult(code);

        }
    }
}
