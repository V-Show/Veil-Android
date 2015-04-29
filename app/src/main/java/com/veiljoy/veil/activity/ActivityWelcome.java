package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.R;

/**
 * Created by zhongqihong on 15/4/16.
 */
public class ActivityWelcome extends BaseActivity {

    ImageView ivLoadingLeft;
    ImageView ivLoadingRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_welcome);
        initViews();
        showWelcomeAnimation();
    }

    private void initViews() {
        ivLoadingLeft = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_left);
        ivLoadingRight = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_right);
    }

    private void showWelcomeAnimation() {
        Animation animationLeft = AnimationUtils.loadAnimation(ActivityWelcome.this, R.anim.common_loading_zoom_left);

        ivLoadingLeft.startAnimation(animationLeft);

        Animation animationRight = AnimationUtils.loadAnimation(ActivityWelcome.this, R.anim.common_loading_zoom_right);

        ivLoadingRight.startAnimation(animationRight);
    }

}
