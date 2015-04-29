package com.veiljoy.veil.im;

import java.util.List;

/**
 * Created by zhongqihong on 15/4/29.
 */
public interface IMRoom {

    String getRoomName();
    List<String> getRoomMembers();
    boolean enterRoom(String roomName);


}
