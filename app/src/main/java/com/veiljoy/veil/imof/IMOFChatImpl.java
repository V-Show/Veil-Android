package com.veiljoy.veil.imof;

import android.util.Log;

import com.veiljoy.veil.im.IMChatBase;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.utils.SharePreferenceUtil;
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


    public void testHostRoom(){


        //服务器最上层的房间
        List<Map<String, String>> groups = new ArrayList<Map<String, String>>(); // 一级目录组
        //储存系统最上层房间名称，及其子房间的名称与jid
        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>(); // 二级目录组
        Map<String, String> group;
        try {

            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();
            Log.v("chatimpl", "connection " + connection == null ? "null" : "!=null");
            if (!connection.isConnected())
                connection.connect();
            ServiceDiscoveryManager discoManager = new ServiceDiscoveryManager(connection);
//
//            String username = SharePreferenceUtil.getName();
//            String password = SharePreferenceUtil.getPasswd();
//            connection.login(username, password);

            //第一次第二个参数为空，可以获取到系统最上层的房间，包括公共房间等,但是此时获取的房间是不能加入的
            Collection<HostedRoom>ServiceCollection=MultiUserChat.getHostedRooms(connection, "");
            //遍历讲上文获取到的房间的Jid传入第二个参数，从而获取子房间
            //此时获取的房间便能加入
            for (HostedRoom s : ServiceCollection) {
                group = new HashMap<String, String>();
                group.put("group", s.getName());
                groups.add(group);
                Log.v("chatimpl", "group name:" +s.getName()+" ,jid:"+s.getJid());

                childs.add(GetRoomFromServers(s.getJid(),connection));
            }

        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // 根据服务器获取房间列表
    private static List<Map<String, String>> GetRoomFromServers(String jid,XMPPConnection connection) {

        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        Map<String, String> result;
        try {

            Collection<HostedRoom> ServiceCollection = MultiUserChat
                    .getHostedRooms(connection, jid);

            for (HostedRoom s : ServiceCollection) {
                result = new HashMap<String, String>();
                result.put("child", s.getName());
                result.put("childid", s.getJid());

                Log.v("chatimpl", "child name:"+s.getName()+" ,jid:"+s.getJid());
                resultList.add(result);
            }

        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultList;
    }

    // 加入一个房间
    public static void JoinRoom(String jid) {

        MultiUserChat curmultchat=null;
        String user=null;
        try {

            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();
            Log.v("chatimpl", "connection " + connection == null ? "null" : "!=null");
            if (!connection.isConnected())
                connection.connect();

            MultiUserChat multiUserChat = new MultiUserChat(connection, jid);
            curmultchat = multiUserChat;

            multiUserChat.join(user); //user为你传入的用户名

            //RegisterRoomMessageListener();
        } catch (XMPPException e) {

            e.printStackTrace();
        }
    }




}
