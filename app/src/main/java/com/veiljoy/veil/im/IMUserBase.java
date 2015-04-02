package com.veiljoy.veil.im;

/**
 * Created by zhongqihong on 15/3/31.
 */
public interface IMUserBase {

    int register(String name,String psw);
    int login(String name,String psw);

}
