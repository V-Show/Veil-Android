package com.veiljoy.veil.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.bean.BaseInfo;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;
import com.veiljoy.veil.imof.MUCHelper;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.DateUtils;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.utils.VoiceUtils;
import com.veiljoy.veil.xmpp.base.MessageManager;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityChat extends ActivityChatSupport implements View.OnLongClickListener {


    ChatAdapter mChatAdapter;
    ArrayList<BaseInfo> objs = null;
    BaseApplication application;
    ListView mLVChat;
    Button mBtnTalk;
    private boolean isTalking;
    String avatarPath;
    private String currMsgType;
    private String mVoiceFileName = null;

    /*
    * 自己的名字和密码
    * */
    private String mUserName;
    private String mPassword;
    /*
    * 当前房间的名字
    * */
    private String mRoomName;

    /*
    * XMPP链接
    *
    * */
    private XMPPConnection mXmppConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        initViews();
        initEvents();
    }



    private void init() {


        mUserName=SharePreferenceUtil.getName();
        mPassword=SharePreferenceUtil.getPasswd();
        mRoomName=SharePreferenceUtil.getRoom();
        mXmppConnection= XmppConnectionManager.getInstance().getConnection();
        avatarPath = SharePreferenceUtil.getAvatar();
        application = (BaseApplication) getApplication();

        objs = new ArrayList<BaseInfo>();
        for (int i = 0; i < 2; i++) {
            IMMessage o = new IMMessage();
            o.setmUri(IMMessage.Scheme.IMAGE.wrap(""));
            Random r = new Random(System.currentTimeMillis());
            o.setmMessageType(Math.abs(r.nextInt() % 2));
            o.setmAvatar(avatarPath);
            Log.v("activityChat", "avatar " + avatarPath);
            objs.add(o);
        }


        VoiceUtils.getmInstance().setOnVoiceRecordListener(new OnVoiceRecordListenerImpl());




    }

    private void registerBroadcast(){

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NEW_MESSAGE_ACTION);
        registerReceiver(receiver, filter);

    }

    private void initEvents() {
        mBtnTalk.setOnLongClickListener(this);
        mBtnTalk.setOnTouchListener(new OnTalkBtnTouch());
        mLVChat.setOnItemClickListener(new OnChatListItemClick());
        if(mMultiUserChat!=null){
            mMultiUserChat.addMessageListener(new multiListener());
        }

    }

    private void initViews() {
        mChatAdapter = new ChatAdapter(application, this, objs);
        mBtnTalk = (Button) this.findViewById(R.id.activity_chat_btn_talk);
        mLVChat = (ListView) this.findViewById(R.id.activity_chat_list);
        mLVChat.setAdapter(mChatAdapter);

    }

    public void refreshAdapter() {
        mChatAdapter.notifyDataSetChanged();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.NEW_MESSAGE_ACTION.equals(action)) {
                IMMessage message = intent
                        .getParcelableExtra(IMMessage.IMMESSAGE_KEY);

                message.setmUri(IMMessage.Scheme.TEXT.wrap("test..."));
                message.setmMessageType(IMMessage.RECV);
                messagePool.add(message);
                receiveNewMessage(message);
                refreshMessage(messagePool);
            }
        }

    };

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.activity_chat_btn_talk:
                if (!isTalking) {
                    Log.v("chatActivity", "start record");
                    isTalking = true;
                    currMsgType = IMMessage.Scheme.VOICE.wrap("test");
                    VoiceUtils.getmInstance().startRecord(null);
                }


                break;
        }


        return true;
    }

    class OnTalkBtnTouch implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (isTalking) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        Log.v("chatActivity", "stop record");
                        isTalking = false;
                        VoiceUtils.getmInstance().stop();
                        refreshList();
                    }
                    break;
                }


            }

            return false;
        }
    }
    /**
     * 會議室信息監聽事件
     *
     */
    public class multiListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) {
            Message message = (Message) packet;
            // 接收来自聊天室的聊天信息
            if (message != null && message.getBody() != null
                    && !message.getBody().equals("null")) {

                String time = DateUtils.date2Str(Calendar.getInstance(),
                        Constants.MS_FORMART);
                String from = message.getFrom().split("/")[0];
                IMMessage newMessage = new IMMessage();
                newMessage.setmMessageType(IMMessage.RECV);
                newMessage.setmFrom(from);
                newMessage.setmContent(message.getBody());
                newMessage.setmTime(time);

                Intent intent = new Intent(Constants.NEW_MESSAGE_ACTION);
                intent.putExtra(IMMessage.IMMESSAGE_KEY, newMessage);
                sendBroadcast(intent);

                Log.v("multi","you hava new msg: "+newMessage.getmContent()+" ,from:"+newMessage.getmFrom());
            }
        }
    }

    public void refreshList() {
        mChatAdapter.refreshList(objs);
        mLVChat.setSelection(objs.size());
    }
    @Override
    protected void receiveNewMessage(IMMessage message) {

    }

    @Override
    protected void refreshMessage(List<IMMessage> messages) {

    }

    public void makeMessage() {

        IMMessage o = null;
        IMMessage.Scheme type = IMMessage.Scheme.ofUri(currMsgType);
        switch (type) {
            case VOICE:
                o = new IMMessageVoiceEntity();
                o.setmUri(IMMessage.Scheme.VOICE.wrap(""));
                Random r = new Random(System.currentTimeMillis());
                o.setmMessageType(Math.abs(r.nextInt() % 2));
                o.setmAvatar(avatarPath);
                ((IMMessageVoiceEntity) o).setmVoiceTimeRange(Math.abs(r.nextInt() % 20));
                ((IMMessageVoiceEntity) o).setmVoiceFileName(mVoiceFileName);

                break;
        }

        if (o != null)
            objs.add(o);
        refreshList();
    }


    class OnVoiceRecordListenerImpl implements VoiceUtils.OnVoiceRecordListener {

        @Override
        public void onBackgroundRunning() {

            Log.v("chatActivity", "onBackgroundRunning");
        }

        @Override
        public void onResult(String fileName) {

            Log.v("chatActivity", "onResult " + fileName);
            if (fileName != null) {
                mVoiceFileName = fileName;
                makeMessage();
            }

        }

        @Override
        public void onPreRecord() {
            Log.v("chatActivity", "onPreRecord ");

        }
    }

    class OnChatListItemClick implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            IMMessage msg = (IMMessage) objs.get(position);

            String uri = msg.getmUri();

            switch (IMMessage.Scheme.ofUri(uri)) {

                case VOICE:


                    IMMessageVoiceEntity voiceEntity = (IMMessageVoiceEntity) msg;
                    Log.v("chatActivity", "OnChatListItemClick " + voiceEntity.getmVoiceFileName());
                    VoiceUtils.getmInstance().play(voiceEntity.getmVoiceFileName());


                    break;
                case IMAGE:

                    break;

            }


        }
    }


}
