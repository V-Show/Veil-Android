package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.renderscript.BaseObj;
import android.widget.ListView;

import com.veiljoy.veil.BaseActivity;
import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.bean.BaseInfo;
import com.veiljoy.veil.im.IMMessage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityChat extends BaseActivity {


    ChatAdapter mChatAdapter;
    ArrayList<BaseInfo> objs;
    BaseApplication application;
    ListView mLVChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        initViews();
    }

    private void init(){
        application=(BaseApplication)getApplication();

        objs=new ArrayList<BaseInfo>();

        for(int i=0;i<15;i++){
            IMMessage o=new IMMessage();
            o.setmUri(IMMessage.Scheme.IMAGE.wrap(""));
            Random r=new Random(    System.currentTimeMillis());
            o.setmMessageType(r.nextInt()%2);

            objs.add(o);
        }

        mChatAdapter=new ChatAdapter(application,this,objs);


    }

    private void initViews(){



        mLVChat=(ListView)this.findViewById(R.id.activity_chat_list);
        mLVChat.setAdapter(mChatAdapter);
    }

    public void refreshAdapter() {
        mChatAdapter.notifyDataSetChanged();
    }


}
