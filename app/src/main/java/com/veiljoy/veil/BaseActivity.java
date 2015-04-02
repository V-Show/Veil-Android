package com.veiljoy.veil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class BaseActivity extends Activity {


    /** 含有Bundle通过Class跳转界面 **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
