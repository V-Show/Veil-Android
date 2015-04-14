package com.veiljoy.veil.activity;

import android.os.Bundle;

import com.veiljoy.veil.BaseActivity;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.DateUtils;
import com.veiljoy.veil.xmpp.base.MessageManager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhongqihong on 15/4/13.
 */
public abstract class ActivityChatSupport extends BaseActivity {


    protected Chat chat=null;
    private static int pageSize = 10;
    protected  String to;
     protected List<IMMessage> messagePool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chat = XmppConnectionManager.getInstance().getConnection()
                .getChatManager().createChat(to, null);

        }

    @Override
    protected void onResume() {


        // 第一次查询
        messagePool = MessageManager.getInstance(this)
                .getMessageListByFrom(to, 1, pageSize);
        if (null != messagePool && messagePool.size() > 0)
            Collections.sort(messagePool);
        if (messagePool == null) {
            messagePool = new ArrayList<IMMessage>();
        }


    }

    protected void sendMessage(String messageContent,String scheme) throws Exception {

        String time = DateUtils.date2Str(Calendar.getInstance(),
                Constants.MS_FORMART);
        Message message = new Message();
        message.setProperty(IMMessage.KEY_TIME, time);
        message.setBody(messageContent);
        chat.sendMessage(message);



        IMMessage newMessage = new IMMessage();
        newMessage.setmMessageType(IMMessage.SEND);
        newMessage.setmFrom(chat.getParticipant());
        newMessage.setmContent(messageContent);
        newMessage.setmUri(scheme);
        newMessage.setmTime(time);
        messagePool.add(newMessage);
        MessageManager.getInstance(this).saveIMMessage(newMessage);
        // MChatManager.message_pool.add(newMessage);

        // 刷新视图
        refreshMessage(messagePool);

    }

    protected abstract void receiveNewMessage(IMMessage message);

    protected abstract void refreshMessage(List<IMMessage> messages);

    protected List<IMMessage> getMessages() {
        return messagePool;
    }

}
