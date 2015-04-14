package com.veiljoy.veil.imof;

import android.util.Log;

import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhongqihong on 15/4/14.
 */
public class MUCHelper {


    public static XMPPConnection connection;


    public static void init(XMPPConnection xmppConnection){
        connection=xmppConnection;

    }


    /**
     * 创建房间
     *
     * @param roomName 房间名称
     */
    public static void createRoom(String roomName) {


        if(connection==null){
           return;
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
            submitForm.setAnswer("muc#roomconfig_persistentroom", false);
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
        } catch (XMPPException e) {
            Log.v("MUCHelper","exception:"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 加入会议室
     *
     * @param user 昵称
     * @param password 会议室密码
     * @param roomsName 会议室名
     * @param connection
     */
    public static MultiUserChat joinMultiUserChat(String user, String password, String roomsName,
                                                  XMPPConnection connection) {
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

}
