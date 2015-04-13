package com.veiljoy.veil.im;

/**
 * Created by zhongqihong on 15/4/11.
 */
public interface IMChatBase {

    public void send(IMMessage msg);

    public void wrap();

    public void receive();
}
