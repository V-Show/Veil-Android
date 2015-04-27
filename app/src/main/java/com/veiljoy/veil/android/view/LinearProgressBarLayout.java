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
    OnVoiceRecordTimeOut mOnVoiceRecordTimeOut;
    String TAG=this.getClass().getName();
    LinearProgressBar progressBar;
    Context mContext;
    ImageView dot;
    Animation animation;
    long beforeTime;
    long afterTime;
    int totalTicks;
    int rate=100;
    boolean timeOut=false;
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
      
        mContext = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate( R.layout.common_progress_line , null );
        this.addView( layout );


        progressBar =(LinearProgressBar)this.findViewById(R.id.common_progress_line);
        dot=(ImageView)this.findViewById(R.id.common_progress_line_iv_dot);

        totalTicks= Integer.parseInt(mContext.getString(R.string.talk_time_limited))*rate;

        animation= AnimationUtils.loadAnimation(
                mContext, R.anim.controller_enter);
        animation.setDuration(totalTicks);

        animation.setFillAfter(true);


        progressBar.setTickListener(this);

    }

    public void start(){
        Log.v(TAG,"start animation");
        timeOut=false;
        progressBar.startProgress();
        beforeTime = System.currentTimeMillis() ;
        //dot.setAnimation(animation);
       // dot.startAnimation(animation);
    }

    public void end(){
        timeOut=false;
        progressBar.endProgress();
        animation.cancel();;
    }

    int[]location=new int[2];
    final float VOICE_TIME_RANGE=6*1000;
    public float getTicks(){

        float delta=(System.currentTimeMillis()-beforeTime);

        float rate=delta/VOICE_TIME_RANGE;///(float)totalTicks;







        if(rate>1){
            rate=1;
            if(timeOut==false){
                timeOut=true;
                if(mOnVoiceRecordTimeOut!=null)
                    mOnVoiceRecordTimeOut.onRecordTimeOut();
            }
        }




        return rate;
    }

    public static interface OnVoiceRecordTimeOut{

        public void onRecordTimeOut();

    }


    public void setOnVoiceRecordTimeOut(OnVoiceRecordTimeOut l){
        this.mOnVoiceRecordTimeOut=l;
    }









}

