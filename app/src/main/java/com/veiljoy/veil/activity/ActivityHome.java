package com.veiljoy.veil.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.imof.MUCJoinTask;
import com.veiljoy.veil.imof.UserAccessManager;
import com.veiljoy.veil.init.InitializationTask;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityHome extends BaseActivity {

    IMUserBase.OnUserLogin mUserLoginTask;
    ImageView ivLoadingLeft;
    ImageView ivLoadingRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();
        showWelcomeAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LOG("ON RESUME....");
        init();
    }

    public void init() {
        new InitializationTask(new OnInitListener()).execute();
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
        // FIXME SharePreferenceUtil.getAvatar()函数返回值不可能为null，默认值是"default_image"
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
            //showCustomToast("正在登录...");
            mUserLoginTask.preLogin();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String name = SharePreferenceUtil.getName();
            String psw = SharePreferenceUtil.getPasswd();

            int ret = mUserLoginTask.onLogin(name, psw);
            return ret;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Log.v("login", "login code " + code);

            switch (code) {
                case Constants.LOGIN_SUCCESS: // 登录成功
                    //showCustomToast(R.string.login_success);
                    break;
                case Constants.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                    //showCustomToast(R.string.message_invalid_username_password);
                    break;
                case Constants.SERVER_UNAVAILABLE:// 服务器连接失败
                    //showCustomToast(R.string.message_server_unavailable);
                    break;
                case Constants.LOGIN_ERROR:// 未知异常
                    //showCustomToast(R.string.unrecoverable_error);
                    break;
            }

            boolean rel = mUserLoginTask.onLoginResult(code);
            Log.v("home", "home rel " + rel);

            if (!rel) {

                startActivity(ActivityRegister.class, null);
                finish();
            } else {
                AppStates.setAlreadyLogined(true);
                new MUCJoinTask(null, ActivityHome.this).execute("");

            }
        }
    }

    private void initViews() {
        ivLoadingLeft = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_left);
        ivLoadingRight = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_right);
    }

    private void showWelcomeAnimation() {
        Animation animationLeft = AnimationUtils.loadAnimation(ActivityHome.this, R.anim.common_loading_zoom_left);

        ivLoadingLeft.startAnimation(animationLeft);

        Animation animationRight = AnimationUtils.loadAnimation(ActivityHome.this, R.anim.common_loading_zoom_right);

        ivLoadingRight.startAnimation(animationRight);
    }

    class OnInitListener implements InitializationTask.InitializationListener {

        @Override
        public void onResult(int code) {
            if (code != -1) {
                mUserLoginTask = new UserAccessManager(ActivityHome.this);
                enter();
            }
        }
    }

}
