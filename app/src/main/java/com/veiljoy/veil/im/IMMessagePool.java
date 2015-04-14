package com.veiljoy.veil.im;

/**
 * Created by zhongqihong on 15/4/13.
 */
public interface IMMessagePool {

    IMMessage pop();
    void push(IMMessage msg);


}
