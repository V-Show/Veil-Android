package com.veiljoy.veil.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.veiljoy.veil.BaseActivity;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.CommonUtils;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.utils.VoiceUtils;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.DateUtils;
import com.veiljoy.veil.xmpp.base.MessageManager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhongqihong on 15/4/13.
 */
public abstract class ActivityChatSupport extends BaseActivity {

    final String TAG = this.getClass().getName();

    MultiUserChat mMultiUserChat;
    private static int pageSize = 10;
    protected String to;
    protected List<IMMessage> messagePool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMultiUserChat = AppStates.getMultiUserChat();
        mMultiUserChat.addMessageListener(new MUCPackageListener());
        mMultiUserChat.addParticipantStatusListener(new ParticipantStatus());
        // 第一次查询
        messagePool = MessageManager.getInstance(this)
                .getMessageListByFrom(to, 1, pageSize);
        if (null != messagePool && messagePool.size() > 0)
            Collections.sort(messagePool);
        if (messagePool == null) {
            messagePool = new ArrayList<IMMessage>();
        }
    }

    private void registerBroadcast() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NEW_MESSAGE_ACTION);
        registerReceiver(receiver, filter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.NEW_MESSAGE_ACTION.equals(action)) {

                Log.v(TAG, "new msg notice...");

                IMMessageVoiceEntity message = AppStates.getImMessageVoiceEntity();
                //intent.getParcelableExtra(IMMessage.IMMESSAGE_KEY);
                messagePool.add(message);
                receiveNewMessage(message);
                refreshMessage(messagePool);
            }
        }

    };

    @Override
    protected void onResume() {

        super.onResume();
        registerBroadcast();


    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    protected void sendMessage(String content) {

        Log.v("ChatActivity", "prepare to send message....");

        String time = DateUtils.date2Str(Calendar.getInstance(),
                Constants.MS_FORMART);
        IMMessage newMessage = makeMessage();
        Message message = new Message();
        message.setProperty(IMMessage.KEY_TIME, time);
        message.setBody(newMessage.getmContent());

        try {

            Log.v("ChatActivity", "start  sending....");
            mMultiUserChat.sendMessage(content);
            Log.v("ChatActivity", "start  sent success!");
        } catch (XMPPException e) {
            Log.v("ChatActivity", e.getMessage());
        }


        newMessage.setmMessageType(IMMessage.SEND);
        newMessage.setmFrom(mMultiUserChat.getNickname());
        newMessage.setmTime(time);
        messagePool.add(newMessage);
        //MessageManager.getInstance(this).saveIMMessage(newMessage);
        // MChatManager.message_pool.add(newMessage);

        // 刷新视图
        refreshMessage(messagePool);

    }

    /**
     */
    public class MUCPackageListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) {


            Message message = (Message) packet;
             String voiceFileDir=null;
            Log.v(TAG, "receive a message: " + message.getBody());

            // 接收来自聊天室的聊天信息
            if (message != null && message.getBody() != null
                    && !message.getBody().equals("null")) {

                String from = message.getFrom().split("/")[1];
                if (SharePreferenceUtil.getName().equals(from)) {
                    Log.v("multi", "message sent by myself.."
                    );

                    return;
                }

                if (message.getBody().contains(CommonUtils.VOICE_SIGN)) {

                    String[] strarray;
                    String voiceFile;
                    String voiceTime;
                    int msgViewTime;
                    boolean result = false;
                    List<String> list = CommonUtils.getImagePathFromSD();
                    strarray = message.getBody().split("&");
                    voiceFile = strarray[0];
                    voiceTime = strarray[1];

                    msgViewTime = Integer.parseInt(voiceTime);
                    String msgViewLength = "";

                    Log.v(TAG,"voice time "+msgViewTime);
//                    voiceTimeView.setVisibility(View.VISIBLE);
//                    voiceTimeView.setText(voiceTime + "\"");
                    for (int i = 0; i < msgViewTime; i++) {
                        msgViewLength += "  ";
                    }
                    String voiceArr[] = voiceFile.split(CommonUtils.VOICE_SIGN);
                    String voiceBrr[] = voiceArr[1].split("@");
                    String imgFilePath = Constants.VOICE_AUDIR + voiceBrr[1] + VoiceUtils.suffix;

                    result = CommonUtils.judge(list, imgFilePath);

                    if (result) {
                        voiceFileDir = imgFilePath;
                    } else {
                        voiceFileDir = CommonUtils.GenerateVoic(voiceBrr[0],
                                voiceBrr[1]);

                    }
                }

                String time = DateUtils.date2Str(Calendar.getInstance(),
                        Constants.MS_FORMART);


                IMMessageVoiceEntity newMessage = new IMMessageVoiceEntity();
                newMessage.setmMessageType(IMMessage.RECV);
                newMessage.setmFrom(from);
                newMessage.setmContent(message.getBody());
                newMessage.setmTime(time);
                newMessage.setmUri(IMMessage.Scheme.VOICE.wrap(""));
                newMessage.setmAvatar(SharePreferenceUtil.getAvatar());
                newMessage.setmVoiceFileName(voiceFileDir);

                Log.v(TAG,"voice file name "+newMessage.getmVoiceFileName());
                AppStates.setImMessageVoiceEntity(newMessage);


                Intent intent = new Intent(Constants.NEW_MESSAGE_ACTION);
                intent.putExtra(IMMessage.IMMESSAGE_KEY, newMessage);
                sendBroadcast(intent);

                Log.v("multi", "you hava new msg: " + newMessage.getmContent() + " ,from:" + message.getFrom()
                );
            }
        }
    }
    class ParticipantStatus implements ParticipantStatusListener {

        @Override
        public void adminGranted(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void adminRevoked(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void banned(String arg0, String arg1, String arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void joined(String participant) {
            System.out.println(StringUtils.parseResource(participant)+ " has joined the room.");
        }

        @Override
        public void kicked(String arg0, String arg1, String arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void left(String participant) {
            // TODO Auto-generated method stub
            LOG(StringUtils.parseResource(participant)+ " has left the room.");

        }

        @Override
        public void membershipGranted(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void membershipRevoked(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void moderatorGranted(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void moderatorRevoked(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void nicknameChanged(String participant, String newNickname) {
            LOG(StringUtils.parseResource(participant)+ " is now known as " + newNickname + ".");
        }

        @Override
        public void ownershipGranted(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void ownershipRevoked(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void voiceGranted(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void voiceRevoked(String arg0) {
            // TODO Auto-generated method stub

        }

    }


    protected abstract void receiveNewMessage(IMMessage message);

    protected abstract void refreshMessage(List<IMMessage> messages);

    protected abstract IMMessage makeMessage();

    protected List<IMMessage> getMessages() {
        return messagePool;
    }

}
