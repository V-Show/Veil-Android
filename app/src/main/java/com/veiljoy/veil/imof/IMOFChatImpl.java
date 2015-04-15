package com.veiljoy.veil.imof;

import android.util.Log;

import com.veiljoy.veil.im.IMChatBase;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.utils.StringUtils;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqihong on 15/4/11.
 */
public class IMOFChatImpl implements IMChatBase {



    @Override
    public void send(IMMessage msg) {

    }

    @Override
    public void wrap() {

    }

    @Override
    public void receive() {

    }




    // 加入一个房间
    public static MultiUserChat JoinRoom(String jid) {

        if(StringUtils.empty(jid)){
            jid= Constants.DEFAULT_ROOM_JID;
        }

        MultiUserChat curmultchat=null;
        String user=null;
        try {

            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();
            if (!connection.isConnected())
                connection.connect();

//            String username = SharePreferenceUtil.getName();
//            String password = SharePreferenceUtil.getPasswd();
//            connection.login(username, password);

            MultiUserChat multiUserChat = new MultiUserChat(connection, jid);
            curmultchat = multiUserChat;

            multiUserChat.join(SharePreferenceUtil.getName()); //user为你传入的用户名

            //RegisterRoomMessageListener();
        } catch (XMPPException e) {

            e.printStackTrace();
        }

        return curmultchat;
    }




}
