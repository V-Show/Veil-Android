package com.veiljoy.veil.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhongqihong on 15/4/1.
 */
public class Constants {

    public static final int REGISTER_RESULT_OK=0;
    public static final int REGISTER_RESULT_ERROR=1;
    public static final int REGISTER_RESULT_USER_EXIST=2;

    public static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "ripple" + File.separator + "Avatar" + File.separator;

    public static  final String USER_AVATAR_FILE_PATH_KEY="USER_AVATAR_FILE_PATH_KEY";
}
