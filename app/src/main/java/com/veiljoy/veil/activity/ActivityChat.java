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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.android.popupwidows.ChatPopupWindow;
import com.veiljoy.veil.android.view.LinearProgressBar;
import com.veiljoy.veil.android.view.LinearProgressBarLayout;
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
public class ActivityChat extends ActivityChatSupport implements View.OnLongClickListener,View.OnClickListener,LinearProgressBarLayout.OnVoiceRecordTimeOut {


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
    * 好友
    * */

    private LinearLayout mPpl0Layout;
    private LinearLayout mPpl1Layout;
    private LinearLayout mPpl2Layout;

    private ImageButton mIBPpl0Change;
    private ImageButton mIBPpl1Change;
    private ImageButton mIBPpl2Change;


    private ImageButton mIBPpl0Kick;
    private ImageButton mIBPpl1Kick;
    private ImageButton mIBPpl2Kick;

    private ImageButton mIBPpl0Avatar;
    private ImageButton mIBPpl1Avatar;
    private ImageButton mIBPpl2Avatar;

    private CheckBox mIBPpl0Voice;
    private CheckBox mIBPpl1Voice;
    private CheckBox mIBPpl2Voice;

    private boolean mIsPplOptionShow;
    private boolean mIsPpl1ptionShow;
    private boolean mIsPpl2ptionShow;
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
    LinearProgressBarLayout mLinearProgressBarLayout;

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

//        mIsVoiceAllowedPpl0=mIBPpl0Voice.isChecked();
//        mIsVoiceAllowedPpl1=mIBPpl1Voice.isChecked();
//        mIsVoiceAllowedPpl2=mIBPpl2Voice.isChecked();


        VoiceUtils.getmInstance().setOnVoiceRecordListener(new OnVoiceRecordListenerImpl());


    }


    private void initEvents() {
        mBtnTalk.setOnLongClickListener(this);
        mBtnTalk.setOnTouchListener(new OnTalkBtnTouch());
        mLVChat.setOnItemClickListener(new OnChatListItemClick());
        mLayoutHeader.setOnClickListener(this);
        mBtnMenu.setOnClickListener(this);
        mIBPpl0Avatar.setOnClickListener(new OnPpl0AvatarClick());
        mIBPpl1Avatar.setOnClickListener(new OnPpl1AvatarClick());
        mIBPpl2Avatar.setOnClickListener(new OnPpl2AvatarClick());
        mIBPpl0Voice.setOnCheckedChangeListener(new OnPp0VoiceCheckedListener());
        mIBPpl1Voice.setOnCheckedChangeListener(new OnPp1VoiceCheckedListener());
        mIBPpl2Voice.setOnCheckedChangeListener(new OnPp2VoiceCheckedListener());
        mLinearProgressBarLayout.setOnVoiceRecordTimeOut(this);

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


        mPpl0Layout =(LinearLayout)this.findViewById(R.id.include_chat_ppl0);
        mPpl1Layout =(LinearLayout)this.findViewById(R.id.include_chat_ppl1);
        mPpl2Layout =(LinearLayout)this.findViewById(R.id.include_chat_ppl2);




        mIBPpl0Change=(ImageButton)mPpl0Layout.findViewById(R.id.include_chat_user_action_change);
        mIBPpl1Change=(ImageButton)mPpl1Layout.findViewById(R.id.include_chat_user_action_change);
        mIBPpl2Change=(ImageButton)mPpl2Layout.findViewById(R.id.include_chat_user_action_change);

        mIBPpl0Kick=(ImageButton)mPpl0Layout.findViewById(R.id.include_chat_user_action_kick);
        mIBPpl1Kick=(ImageButton)mPpl1Layout.findViewById(R.id.include_chat_user_action_kick);
        mIBPpl2Kick=(ImageButton)mPpl2Layout.findViewById(R.id.include_chat_user_action_kick);

        mIBPpl0Voice=(CheckBox)mPpl0Layout.findViewById(R.id.include_chat_user_action_voice_allowed);
        mIBPpl1Voice=(CheckBox)mPpl1Layout.findViewById(R.id.include_chat_user_action_voice_allowed);
        mIBPpl2Voice=(CheckBox)mPpl2Layout.findViewById(R.id.include_chat_user_action_voice_allowed);


        mIBPpl0Avatar=(ImageButton)mPpl0Layout.findViewById(R.id.userphoto_avatar_item_iv_cover);
        mIBPpl1Avatar=(ImageButton)mPpl1Layout.findViewById(R.id.userphoto_avatar_item_iv_cover);
        mIBPpl2Avatar=(ImageButton)mPpl2Layout.findViewById(R.id.userphoto_avatar_item_iv_cover);

        mLinearProgressBarLayout=(LinearProgressBarLayout)this.findViewById(R.id.activity_chat_talk_progressbar_layout);


        initPopMenu();



    }

    private void initPopMenu(){



        int[] location = new int[2];
        mBtnMenu.getLocationOnScreen(location);
        mHeaderHeight=location[1]+mBtnMenu.getHeight();

        Log.v(TAG,"mHeaderHeight "+mHeaderHeight);

        mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                133, getResources().getDisplayMetrics());

        mChatPopupWindow = new ChatPopupWindow(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public void refreshAdapter() {
        mChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecordTimeOut() {
        mBtnTalk.requestFocus();
        recordStop();
    }

    public void recordStop(){
        Log.v("chatActivity", "stop record");
        afterTime = System.currentTimeMillis();
        isTalking = false;
        VoiceUtils.getmInstance().stop();
        mLinearProgressBarLayout.end();
    }




    class OnPpl0AvatarClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mIsPplOptionShow=!mIsPplOptionShow;
            mLinearProgressBarLayout.end();
            if(mIsPplOptionShow){
                mIBPpl0Change.setVisibility(View.VISIBLE);
                mIBPpl0Kick.setVisibility(View.VISIBLE);
                mIBPpl0Voice.setVisibility(View.VISIBLE);
            }
            else{
                mIBPpl0Change.setVisibility(View.INVISIBLE);
                mIBPpl0Kick.setVisibility(View.INVISIBLE);
                mIBPpl0Voice.setVisibility(View.INVISIBLE);
            }

        }


    }

    class OnPpl1AvatarClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mIsPpl1ptionShow=!mIsPpl1ptionShow;
            if(mIsPpl1ptionShow){
                mIBPpl1Change.setVisibility(View.VISIBLE);
                mIBPpl1Kick.setVisibility(View.VISIBLE);
                mIBPpl1Voice.setVisibility(View.VISIBLE);
            }
            else{
                mIBPpl1Change.setVisibility(View.INVISIBLE);
                mIBPpl1Kick.setVisibility(View.INVISIBLE);
                mIBPpl1Voice.setVisibility(View.INVISIBLE);
            }
        }
    }

    class OnPpl2AvatarClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mIsPpl2ptionShow=!mIsPpl2ptionShow;
            if(mIsPpl2ptionShow){
                mIBPpl2Change.setVisibility(View.VISIBLE);
                mIBPpl2Kick.setVisibility(View.VISIBLE);
                mIBPpl2Voice.setVisibility(View.VISIBLE);

            }
            else{
                mIBPpl2Change.setVisibility(View.INVISIBLE);
                mIBPpl2Kick.setVisibility(View.INVISIBLE);
                mIBPpl2Voice.setVisibility(View.INVISIBLE);
            }

        }
    }




    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.activity_chat_btn_talk:
                if (!isTalking) {
                    mLinearProgressBarLayout.start();
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


                mChatPopupWindow.showAsDropDown(mBtnMenu,0,mHeaderHeight);

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
                        recordStop();
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


    class OnPp0VoiceCheckedListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        }
    }

    class OnPp1VoiceCheckedListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    }
    class OnPp2VoiceCheckedListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    }

}
