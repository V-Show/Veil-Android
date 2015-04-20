package com.veiljoy.veil.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.android.popupwidows.ChatPopupWindow;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;
import com.veiljoy.veil.memory.ImageCache;
import com.veiljoy.veil.utils.CommonUtils;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.utils.VoiceUtils;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.XMPPConnection;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityChat extends ActivityChatSupport implements View.OnLongClickListener,View.OnClickListener{


    ChatAdapter mChatAdapter;
    RelativeLayout mLayoutHeader;
    BaseApplication application;
    ListView mLVChat;
    Button mBtnTalk;
    ImageButton mBtnMenu;
    private boolean isTalking;
    String avatarPath;
    private String currMsgType;
    private String mVoiceFileName = null;

    /*
    * info for the girl
    * */
    private ImageView mIVGirlAvatar;
    private TextView mIVGrilName;
    /*
    *监控录音时间
    */
    long beforeTime;
    long afterTime;
    int timeDistance;

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

    /**/
    private ChatPopupWindow mChatPopupWindow;
    private int mWidth;
    private int mHeaderHeight;

    /*
    *
    * */


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        initViews();
        initEvents();


    }


    private void init() {


        mUserName = SharePreferenceUtil.getName();
        mPassword = SharePreferenceUtil.getPasswd();
        mRoomName = SharePreferenceUtil.getRoom();
        mXmppConnection = XmppConnectionManager.getInstance().getConnection();
        avatarPath = SharePreferenceUtil.getAvatar();
        application = (BaseApplication) getApplication();


        VoiceUtils.getmInstance().setOnVoiceRecordListener(new OnVoiceRecordListenerImpl());


    }


    private void initEvents() {
        mBtnTalk.setOnLongClickListener(this);
        mBtnTalk.setOnTouchListener(new OnTalkBtnTouch());
        mLVChat.setOnItemClickListener(new OnChatListItemClick());
        mLayoutHeader.setOnClickListener(this);
        mBtnMenu.setOnClickListener(this);

    }

    private void initViews() {
        mLayoutHeader=(RelativeLayout)this.findViewById(R.id.activity_chat_curr_user_layout);
        mIVGirlAvatar=(ImageView)this.findViewById(R.id.activity_chat_iv_girl_avatar);
        mIVGrilName=(TextView)this.findViewById(R.id.activity_chat_tv_girl_name);
     //   if(SharePreferenceUtil.getGender()==1)
        {
            mIVGirlAvatar.setImageBitmap(ImageCache.getAvatar(SharePreferenceUtil.getAvatar()));
            mIVGirlAvatar.setBackgroundColor(Color.TRANSPARENT);
            mIVGrilName.setText(SharePreferenceUtil.getName());
        }
        mChatAdapter = new ChatAdapter(application, this, getMessages());
        mBtnTalk = (Button) this.findViewById(R.id.activity_chat_btn_talk);
        mLVChat = (ListView) this.findViewById(R.id.activity_chat_list);
        mLVChat.setAdapter(mChatAdapter);
        mBtnMenu=(ImageButton)this.findViewById(R.id.include_app_topbar_ib_menu);

        initPopMenu();

    }

    private void initPopMenu(){

        mHeaderHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 25, getResources()
                        .getDisplayMetrics());

        mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                133, getResources().getDisplayMetrics());
        int mHeiht=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                155, getResources().getDisplayMetrics());
        mChatPopupWindow = new ChatPopupWindow(this, mWidth,
                mHeiht);
               // LayoutParams.WRAP_CONTENT);
    }

    public void refreshAdapter() {
        mChatAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.activity_chat_btn_talk:
                if (!isTalking) {
                    Log.v("chatActivity", "start record");
                    beforeTime = System.currentTimeMillis();
                    isTalking = true;
                    currMsgType = IMMessage.Scheme.VOICE.wrap("test");
                    mVoiceFileName = VoiceUtils.generateFileName();
                    VoiceUtils.getmInstance().startRecord(mVoiceFileName);
                }


                break;
        }


        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_chat_user_info_layout:

                break;
            case R.id.include_app_topbar_ib_menu:
//                 mChatPopupWindow.showAtLocation(mLayoutHeader, Gravity.RIGHT
//                            | Gravity.TOP, -10, mHeaderHeight );

                mChatPopupWindow.showAtLocation(mLayoutHeader, Gravity.RIGHT
                        | Gravity.TOP, -10, mHeaderHeight );

                break;
        }
    }
///mHeaderHeight
    class OnTalkBtnTouch implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (isTalking) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        Log.v("chatActivity", "stop record");
                        afterTime = System.currentTimeMillis();
                        isTalking = false;
                        VoiceUtils.getmInstance().stop();
                    }
                    break;
                }


            }

            return false;
        }
    }


    @Override
    protected void receiveNewMessage(IMMessage message) {

    }

    @Override
    protected void refreshMessage(List<IMMessage> messages) {
        mChatAdapter.refreshList(messagePool);
        mLVChat.setSelection(messagePool.size());
    }

    public IMMessage makeMessage() {

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

        return o;

    }

    /*
    * 发送语音消息
    * */

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

                String path = fileName.substring(fileName.lastIndexOf(File.separator), fileName.length());

                String file = path.substring(1, path.lastIndexOf(VoiceUtils.suffix));

                String voiceFile = CommonUtils.VOICE_SIGN
                        + CommonUtils.GetImageStr(mVoiceFileName) + "@" + file
                        + CommonUtils.VOICE_SIGN;
                Log.v(TAG, "voice file name " + file);
                sendMessage(voiceFile + "&"
                        + (afterTime - beforeTime) / 1000);


            }

        }

        @Override
        public void onPreRecord() {
            Log.v("chatActivity", "onPreRecord ");

        }
    }
    /*
    * 播放指定的语音项
    *
    * */

    class OnChatListItemClick implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            IMMessage msg = (IMMessage) getMessages().get(position);

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

    class OnChatMenuBtnSelectedListenerImpl implements ChatPopupWindow.OnChatMenuBtnSelectedListener{

        @Override
        public void onPpl0TalkAllowed(boolean selected) {

        }

        @Override
        public void onPpl1TalkAllowed(boolean selected) {

        }

        @Override
        public void onPpl2TalkAllowed(boolean selected) {

        }

        @Override
        public void onPpl0Changed(boolean selected) {

        }

        @Override
        public void onPpl1Changed(boolean selected) {

        }

        @Override
        public void onPpl2Changed(boolean selected) {

        }

        @Override
        public void onPpl0Reserved(boolean selected) {

        }

        @Override
        public void onPpl1Reserved(boolean selected) {

        }

        @Override
        public void onPpl2Reserved(boolean selected) {

        }
    }



}
