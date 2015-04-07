package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.renderscript.BaseObj;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.veiljoy.veil.BaseActivity;
import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.bean.BaseInfo;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.utils.Constants;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityChat extends BaseActivity implements View.OnLongClickListener{


    ChatAdapter mChatAdapter;
    ArrayList<BaseInfo> objs;
    BaseApplication application;
    ListView mLVChat;
    Button mBtnTalk;
    private boolean isTalking;
    String avatarPath;
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

        for(int i=0;i<10;i++){
            IMMessage o=new IMMessage();
            o.setmUri(IMMessage.Scheme.IMAGE.wrap(""));
            Random r=new Random(    System.currentTimeMillis());
            o.setmMessageType(Math.abs(r.nextInt()%2));
            o.setmAvatar(avatarPath);
            o.setmVoiceTimeRange(Math.abs(r.nextInt()%20));
            Log.v("activityChat","avatar "+avatarPath);
            //o.setmMessageType(0);
            objs.add(o);
        }

        mChatAdapter=new ChatAdapter(application,this,objs);


    }

    private void initEvents(){
        mBtnTalk.setOnLongClickListener(this);
        mBtnTalk.setOnTouchListener(new OnTalkBtnTouch());
    }

    private void initViews(){
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
                isTalking=true;
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
                        isTalking=false;

                        IMMessage o=new IMMessage();
                        o.setmUri(IMMessage.Scheme.IMAGE.wrap(""));
                        Random r=new Random(    System.currentTimeMillis());
                        o.setmMessageType(Math.abs(r.nextInt()%2));
                        o.setmAvatar(avatarPath);
                        o.setmVoiceTimeRange(Math.abs(r.nextInt()%20));
                        objs.add(o);

                        mChatAdapter.refreshList(objs);
                        mLVChat.setSelection(objs.size());
                    }
                    break;
                }


            }

            return false;
        }
    }


}
