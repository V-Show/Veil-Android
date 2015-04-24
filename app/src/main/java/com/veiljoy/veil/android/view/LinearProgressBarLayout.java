package com.veiljoy.veil.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.veiljoy.veil.R;

/**
 * Created by zhongqihong on 15/4/24.
 */

public class LinearProgressBarLayout extends FrameLayout implements LinearProgressBar.TickTracker {
    String TAG=this.getClass().getName();
    LinearProgressBar progressBar;
    Context mContext;
    ImageView dot;
    Animation animation;
    long beforeTime;
    long afterTime;
    int totalTicks;
    public LinearProgressBarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initWidget(context );
    }

    public LinearProgressBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        initWidget(context);

    }

    public LinearProgressBarLayout(Context context) {
        super(context);
        initWidget(context);
    }

    private  void initWidget(Context context){
        Log.v(TAG,"initWidget ");
        mContext = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate( R.layout.common_progress_line , null );
        this.addView( layout );


        progressBar =(LinearProgressBar)this.findViewById(R.id.common_progress_line);
        dot=(ImageView)this.findViewById(R.id.common_progress_line_iv_dot);

        totalTicks= Integer.parseInt(mContext.getString(R.string.talk_time_limited))*100;

        animation= AnimationUtils.loadAnimation(
                mContext, R.anim.controller_enter);
        animation.setDuration(totalTicks);
        animation.setAnimationListener(new DotAnimationListener());
        animation.setFillAfter(true);

        dot.setAnimation(animation);
        progressBar.setTickListener(this);

    }

    public void start(){
        Log.v(TAG,"start animation");
        beforeTime = System.currentTimeMillis();
        dot.startAnimation(animation);
    }

    public void end(){
        animation.cancel();;
    }
    int[]location=new int[2];
    public int getTicks(){

     //   afterTime = System.currentTimeMillis();

        //long delta=(afterTime-beforeTime)/1000;

        float rate=(System.currentTimeMillis()-beforeTime)/(float)totalTicks;

        dot.getLocationOnScreen(location);
        Log.v(TAG,"start animation x "+ rate);


        return (int)rate;
    }

    class DotAnimationListener implements android.view.animation.Animation.AnimationListener
    {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }










}

