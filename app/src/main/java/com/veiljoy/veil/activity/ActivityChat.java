package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.veiljoy.veil.BaseActivity;
import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.bean.BaseInfo;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.VoiceUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityChat extends BaseActivity implements View.OnLongClickListener{


    ChatAdapter mChatAdapter;
    ArrayList<BaseInfo> objs=null;
    BaseApplication application;
    ListView mLVChat;
    Button mBtnTalk;
    private boolean isTalking;
    String avatarPath;
    private String currMsgType;
    private String mVoiceFileName =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        initViews();
        initEvents();
    }

    private void init(){


        Bundle bundle    =getIntent().getExtras();
        avatarPath=bundle.getString(Constants.USER_AVATAR_FILE_PATH_KEY);
        application=(BaseApplication)getApplication();

        objs=new ArrayList<BaseInfo>();


        for(int i=0;i<2;i++){
            IMMessage o=new IMMessage();
            o.setmUri(IMMessage.Scheme.IMAGE.wrap(""));
            Random r=new Random(    System.currentTimeMillis());
            o.setmMessageSource(Math.abs(r.nextInt() % 2));
            o.setmAvatar(avatarPath);
            Log.v("activityChat","avatar "+avatarPath);
            objs.add(o);
        }



        VoiceUtils.getmInstance().setOnVoiceRecordListener(new OnVoiceRecordListenerImpl());


    }

    private void initEvents(){
        mBtnTalk.setOnLongClickListener(this);
        mBtnTalk.setOnTouchListener(new OnTalkBtnTouch());
        mLVChat.setOnItemClickListener(new OnChatListItemClick());
    }

    private void initViews(){
        mChatAdapter=new ChatAdapter(application,this,objs);
        mBtnTalk=(Button)this.findViewById(R.id.activity_chat_btn_talk);
        mLVChat=(ListView)this.findViewById(R.id.activity_chat_list);
        mLVChat.setAdapter(mChatAdapter);

    }

    public void refreshAdapter() {
        mChatAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onLongClick(View v) {

        switch(v.getId()){
            case R.id.activity_chat_btn_talk:
                if(!isTalking){
                    Log.v("chatActivity","start record");
                    isTalking=true;
                    currMsgType =IMMessage.Scheme.VOICE.wrap("test");
                    VoiceUtils.getmInstance().startRecord(null);
                }


                break;
        }


        return true;
    }

    class OnTalkBtnTouch implements  View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(isTalking){
                switch(event.getAction()){
                    case MotionEvent.ACTION_UP:
                    {
                        Log.v("chatActivity","stop record");
                        isTalking=false;
                        VoiceUtils.getmInstance().stop();
                        refreshList();
                    }
                    break;
                }


            }

            return false;
        }
    }

    public void refreshList(){
        mChatAdapter.refreshList(objs);
        mLVChat.setSelection(objs.size());
    }

    public void makeMessage(){

        IMMessage o=null;
        IMMessage.Scheme type=IMMessage.Scheme.ofUri(currMsgType);
        switch (type){
            case VOICE:
                o =new IMMessageVoiceEntity();
                o.setmUri(IMMessage.Scheme.VOICE.wrap(""));
                Random r=new Random(    System.currentTimeMillis());
                o.setmMessageSource(Math.abs(r.nextInt() % 2));
                o.setmAvatar(avatarPath);
                ((IMMessageVoiceEntity)o).setmVoiceTimeRange(Math.abs(r.nextInt() % 20));
                ((IMMessageVoiceEntity)o).setmVoiceFileName(mVoiceFileName);

                break;
        }

        if(o!=null)
            objs.add(o);
        refreshList();
    }



    class OnVoiceRecordListenerImpl implements VoiceUtils.OnVoiceRecordListener {

        @Override
        public void onBackgroundRunning() {

            Log.v("chatActivity","onBackgroundRunning");
        }

        @Override
        public void onResult(String fileName) {

            Log.v("chatActivity","onResult "+fileName);
            if(fileName!=null){
                mVoiceFileName =fileName;
                makeMessage();
            }

        }

        @Override
        public void onPreRecord() {
            Log.v("chatActivity","onPreRecord ");

        }
    }

    class OnChatListItemClick implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            IMMessage msg=(IMMessage)objs.get(position);

            String uri =msg.getmUri();

            switch (IMMessage.Scheme.ofUri(uri)){

                case VOICE:


                    IMMessageVoiceEntity voiceEntity=(IMMessageVoiceEntity)msg;
                    Log.v("chatActivity","OnChatListItemClick "+voiceEntity.getmVoiceFileName());
                    VoiceUtils.getmInstance().play(voiceEntity.getmVoiceFileName());



                    break;
                case IMAGE:

                    break;

            }


        }
    }


}
