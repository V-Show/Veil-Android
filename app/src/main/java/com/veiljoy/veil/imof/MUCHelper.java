package com.veiljoy.veil.imof;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;

import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.utils.StringUtils;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.net.*;
/**
 * Created by zhongqihong on 15/4/14.
 */
public class MUCHelper {

    public static final String TAG="MUCHelper";

    public static XMPPConnection connection;
    public static String XMPP_SERVER_URL="http://my.openfire.com:9090/plugins/presence/status?jid=user1@my.openfire.com&type=xml";

    public static void init(XMPPConnection xmppConnection){
        connection=xmppConnection;

    }


    /**
     * 创建房间
     *
     * @param roomName 房间名称
     */
    public static boolean createRoom(String roomName) {


        if(connection==null){
           return false;
        }
        try {


            // 创建一个MultiUserChat
            MultiUserChat muc = new MultiUserChat(connection, roomName
                    + "@conference." + connection.getServiceName());
            // 创建聊天室
            muc.create(roomName); // roomName房间的名字
            // 获得聊天室的配置表单
            Form form = muc.getConfigurationForm();
            // 根据原始表单创建一个要提交的新表单。
            Form submitForm = form.createAnswerForm();
            // 向要提交的表单添加默认答复
            for (Iterator<FormField> fields = form.getFields(); fields
                    .hasNext();) {
                FormField field = (FormField) fields.next();
                if (!FormField.TYPE_HIDDEN.equals(field.getType())
                        && field.getVariable() != null) {
                    // 设置默认值作为答复
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            // 设置聊天室的新拥有者
            List<String> owners = new ArrayList<String>();
            owners.add(connection.getUser());// 用户JID
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            // 房间仅对成员开放
            submitForm.setAnswer("muc#roomconfig_membersonly", false);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            // 进入是否需要密码
            //submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
            // 设置进入密码
            //submitForm.setAnswer("muc#roomconfig_roomsecret", "password");
            // 能够发现占有者真实 JID 的角色
            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
            // 登录房间对话
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 仅允许注册的昵称登录
            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
            // 允许使用者修改昵称
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
            submitForm.setAnswer("x-muc#roomconfig_registration", false);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            muc.sendConfigurationForm(submitForm);

            return true;
        } catch (XMPPException e) {
            Log.v("MUCHelper","exception:"+e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 加入会议室
     * @param roomsName 会议室名
     */
    public static MultiUserChat joinMultiUserChat(
            String roomsName) {

        String user=SharePreferenceUtil.getName();
        String password=SharePreferenceUtil.getPasswd();
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            MultiUserChat muc = new MultiUserChat(connection, roomsName
                    + "@conference." + connection.getServiceName());
            // 聊天室服务将会决定要接受的历史记录数量
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(0);
            //history.setSince(new Date());
            // 用户加入聊天室
            muc.join(user, password, history, SmackConfiguration.getPacketReplyTimeout());
            Log.v("MUCHelper","会议室加入成功........");
            return muc;
        } catch (XMPPException e) {
            e.printStackTrace();
            Log.v("MUCHelper","会议室加入失败........");
            return null;
        }
    }



    public static void fetchHostRoom(List<List<Map<String, String>>> childs){

        //        //服务器最上层的房间
        List<Map<String, String>> groups = new ArrayList<Map<String, String>>(); // 一级目录组


        Map<String, String> group;
        try {

            XMPPConnection connection ;

            if(!AppStates.isAlreadyLogined()){
                connection=login(SharePreferenceUtil.getName(),SharePreferenceUtil.getPasswd());
            }
            else
            {
                connection=XmppConnectionManager.getInstance()
                        .getConnection();
                if (!connection.isConnected())
                    connection.connect();
            }

            //在加入房间之前，先把个人资料上传到服务器



            ServiceDiscoveryManager discoManager = new ServiceDiscoveryManager(connection);

            //第一次第二个参数为空，可以获取到系统最上层的房间，包括公共房间等,但是此时获取的房间是不能加入的
            Collection<HostedRoom> ServiceCollection=MultiUserChat.getHostedRooms(connection, "");
            //遍历讲上文获取到的房间的Jid传入第二个参数，从而获取子房间
            //此时获取的房间便能加入
            for (HostedRoom s : ServiceCollection) {
                group = new HashMap<String, String>();
                group.put("group", s.getName());
                groups.add(group);
                Log.v("chatimpl", "group name:" +s.getName()+" ,jid:"+s.getJid());

                childs.add(GetRoomFromServers(s.getJid(),connection));
            }
            IMOFChatImpl.getUserAvatar(connection);

        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /*
    * 获取任意一个有效房间。
    * @return Map(key 房间名字 ，value 房间的jid)
    * */

    public static Map<String,String> getAnyRoom(){
        HashMap<String,String> anyRoom= new HashMap<>();
        boolean flag=false;
        String roomName=null;
        String roomJid=null;


        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
        MUCHelper.fetchHostRoom(childs);


        for (int i = 0; i < childs.size(); i++) {
            if (!flag) {
                //获得上层房间组
                List<Map<String, String>> roomGroup = childs.get(i);
                for (int j = 0; j < roomGroup.size(); j++) {
                    Map<String, String> room = roomGroup.get(j);
                    if (room.containsKey(Constants.MAP_ROOM_NAME_KEY)) {
                        if (room.get(Constants.MAP_ROOM_NAME_KEY) != null) {
                            roomName = room.get(Constants.MAP_ROOM_NAME_KEY);
                        }
                    }

                    if (room.containsKey(Constants.MAP_ROOM_JID_KEY)) {
                        if (room.get(Constants.MAP_ROOM_JID_KEY) != null) {
                            roomJid = room.get(Constants.MAP_ROOM_JID_KEY);
                        }
                    }

                    if (roomName != null && roomJid != null) {
                        flag = true;
                        anyRoom.put(roomName,roomJid);
                        break;
                    }

                }
            } else {
                break;
            }

        }


        return anyRoom;
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
                result.put(Constants.MAP_ROOM_NAME_KEY, s.getName());
                result.put(Constants.MAP_ROOM_JID_KEY, s.getJid());
                Log.v("chatimpl", "child name:"+s.getName()+" ,jid:"+s.getJid());
                resultList.add(result);
            }

        } catch (XMPPException e) {

            e.printStackTrace();
        }
        return resultList;
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


            MultiUserChat multiUserChat = new MultiUserChat(connection, jid);
            curmultchat = multiUserChat;


            // 聊天室服务将会决定要接受的历史记录数量
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(0);

            multiUserChat.join(SharePreferenceUtil.getName(),SharePreferenceUtil.getPasswd(),
                    history, SmackConfiguration.getPacketReplyTimeout()); //user为你传入的用户名

            //RegisterRoomMessageListener();
        } catch (XMPPException e) {

            e.printStackTrace();
        }

        return curmultchat;
    }

    /**
    * 查询会议室成员名字
    * @param muc
    */
    public static List<String> findMulitUser(MultiUserChat muc){
        List<String> listUser = new ArrayList<String>();
        Iterator<String> it = muc.getOccupants();
        //遍历出聊天室人员名称
        while (it.hasNext()) {
            // 聊天室成员名字
            String name = it.next();
            listUser.add(name);
        }
        return listUser;
    }


    public static XMPPConnection login(String username,String password) {

        try {
            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();

            if(AppStates.isAlreadyLogined()){
                return connection;
            }
            if (!connection.isConnected())
                connection.connect();
            connection.login(username, password);
            connection.sendPacket(new Presence(Presence.Type.available));
            AppStates. setAlreadyLogined(true);
            IsUserOnLine(username);
            return connection;

        } catch (XMPPException e) {

            e.printStackTrace();
            return null;
        }



    }

    public static String combineCheckingPrecenceUrl(String uid){

        return String.format("http://"+Constants.XMPP_HOST_NAME_PORT+"/plugins/presence/status?" +
            "jid={0}@"+Constants.XMPP_HOST_NAME+"&type=xml",uid);

    }
    /**
     * 判断openfire用户的状态
     * strUrl : url格式 - http://my.openfire.com:9090/plugins/presence/status?jid=user1@my.openfire.com&type=xml
     * 返回值 : 0 - 用户不存在; 1 - 用户在线; 2 - 用户离线
     * 说明   ：必须要求 openfire加载 presence 插件，同时设置任何人都可以访问
     */

    public  static short IsUserOnLine(String uid)
    {
        short            shOnLineState    = 0;    //-不存在-
        String strUrl=combineCheckingPrecenceUrl(uid);

        Log.v(TAG,"strUrl "+strUrl);

        try
        {
            URL             oUrl     = new URL(strUrl);
            URLConnection     oConn     = oUrl.openConnection();
            if(oConn!=null)
            {
                BufferedReader     oIn = new BufferedReader(new InputStreamReader(oConn.getInputStream()));
                if(null!=oIn)
                {
                    String strFlag = oIn.readLine();
                    oIn.close();

                    if(strFlag.indexOf("type=\"unavailable\"")>=0)
                    {
                        shOnLineState = 2;
                    }
                    if(strFlag.indexOf("type=\"error\"")>=0)
                    {
                        shOnLineState = 0;
                    }
                    else if(strFlag.indexOf("priority")>=0 || strFlag.indexOf("id=\"")>=0)
                    {
                        shOnLineState = 1;
                    }
                }
            }
        }
        catch(Exception e)
        {
        }

        return     shOnLineState;
    }


}
