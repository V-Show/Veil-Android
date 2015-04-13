package com.veiljoy.veil.utils;

import android.widget.EditText;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by zhongqihong on 15/4/8.
 */
public class StringUtils {

    public static String getRandomString(int len) {
        String returnStr = "";
        char[] ch = new char[len];
        Random rd = new Random();
        for (int i = 0; i < len; i++) {
            ch[i] = (char) (rd.nextInt(9) + 97);
        }
        returnStr = new String(ch);
        return returnStr;
    }

    public synchronized static String getSequenceId() {
        return TimeUtils.now() + getRandomString(10);
    }

    public static boolean matchNumber(String text) {
        if (Pattern.compile("\\d{7,9}").matcher(text).matches()) {
            return true;
        }
        return false;
    }


    public static boolean matchAccount(String text) {
        if (Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]{2,16}$").matcher(text).matches()) {
            return true;
        } else return false;
    }

    public static boolean isNull(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text != null && text.length() > 0) {
            return false;
        }
        return true;
    }

    public static boolean matchEmail(String text) {
        if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text).matches()) {
            return true;
        }
        return false;
    }


    public static boolean matchPhone(String text) {
        if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
            return true;
        }
        return false;
    }


}
