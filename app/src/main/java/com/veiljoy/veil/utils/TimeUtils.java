package com.veiljoy.veil.utils;


import android.text.format.Time;

/**
 * Created by zhongqihong on 15/4/8.
 */
public class TimeUtils {

    public static String now()
    {
        Time localTime = new Time();
        localTime.setToNow();
        return localTime.format("%Y%m%d%H%M%S");
    }

}
