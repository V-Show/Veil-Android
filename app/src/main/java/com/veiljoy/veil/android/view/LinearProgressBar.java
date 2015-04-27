package com.veiljoy.veil.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.veiljoy.veil.R;

/**
 * Created by zhongqihong on 15/4/24.
 */
public class LinearProgressBar extends View {


    String TAG=this.getClass().getName();

    Paint paint = new Paint();
    TickTracker tickTracker;
    float ticks = 20;
    int range = 0;
    boolean isStarted=false;

    public LinearProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LinearProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs,0);

    }

    public void startProgress(){
        isStarted=true;
    }

    public void endProgress(){
        isStarted=false;
    }

    public LinearProgressBar(Context context) {
        super(context);
    }

    public  void setTickListener(TickTracker l){
        tickTracker=l;
    }

    private void adjustRange() {
        ticks =tickTracker.getTicks();

       // if (ticks > 0)
        range = (int)(ticks*getWidth());
    }

     private void init(){
       paint.setTypeface(Typeface.DEFAULT_BOLD);
       paint.setFakeBoldText(true);
       paint.setAntiAlias(true);
       paint.setColor(getResources().getColor(R.color.voice_progress_line));
       paint.setStrokeWidth((float) 1.0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isStarted){
            adjustRange();

            if (range>0&&range <= this.getWidth()) {
                canvas.drawLine(0, 0, range, 0, paint);
            }

        }
        invalidate();









    }
    public static interface TickTracker{
        float getTicks();
    }



}
