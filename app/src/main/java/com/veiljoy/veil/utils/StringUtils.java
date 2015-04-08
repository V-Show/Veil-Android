package com.veiljoy.veil.utils;

import java.util.Random;

/**
 * Created by zhongqihong on 15/4/8.
 */
public class StringUtils {

    public static String getRandomString(int len) {
        String returnStr = "";
        char[] ch = new char[len];
        Random rd = new Random();
        for (int i = 0; i < len; i++) {
            ch[i] = (char) (rd.nextInt(9)+97);
        }
        returnStr = new String(ch);
        return returnStr;
    }

    public synchronized static String getSequenceId(){
        return TimeUtils.now()+getRandomString(10);
    }

}
