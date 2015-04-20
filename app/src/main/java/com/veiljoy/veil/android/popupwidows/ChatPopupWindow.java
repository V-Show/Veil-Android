package com.veiljoy.veil.android.popupwidows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BasePopupWindow;

import java.util.ArrayList;

/**
 * Created by zhongqihong on 15/4/20.
 */
public class ChatPopupWindow extends BasePopupWindow implements View.OnClickListener{

    private String TAG=this.getClass().getName().toString();

    OnChatMenuBtnSelectedListener mOnChatMenuBtnSelected;

    ImageView mIVPpl0Talk;
    ImageView mIVppl0Change;
    ImageView mIVppl0Reserve;

    ImageView mIVPpl1Talk;
    ImageView mIVppl1Change;
    ImageView mIVppl1Reserve;

    ImageView mIVPpl2Talk;
    ImageView mIVppl2Change;
    ImageView mIVppl2Reserve;

    boolean isPpl0TalkAllowed =false;
    boolean isPpl1TalkAllowed =false;
    boolean isPpl2TalkAllowed =false;

    boolean isPpl0Changed=false;
    boolean isPpl1Changed=false;
    boolean isPpl2Changed=false;

    boolean isPpl0Reserved =false;
    boolean isPpl1Reserved =false;
    boolean isPpl2Reserved =false;


    ArrayList<ViewHolder> viewHolders ;


    public ChatPopupWindow(Context context, int width, int height) {
        super(LayoutInflater.from(context).inflate(
                R.layout.popup_chat_menu, null), width, height);
        setAnimationStyle(R.style.Popup_Animation_Alpha);
        initViews();
        initEvents();
        init();
    }

    public void setOnChatMenuBtnSelected(OnChatMenuBtnSelectedListener l){
        this.mOnChatMenuBtnSelected=l;

    }

    @Override
    public void initViews() {

        mIVPpl0Talk=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people0_talk);
        mIVppl0Change=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people0_change);
        mIVppl0Reserve=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people0_reserve);

        mIVPpl1Talk=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people1_talk);
        mIVppl1Change=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people1_change);
        mIVppl1Reserve=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people1_reserve);

        mIVPpl2Talk=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people2_talk);
        mIVppl2Change=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people2_change);
        mIVppl2Reserve=(ImageView)this.findViewById(R.id.popup_chat_menu_iv_people2_reserve);
    }

    @Override
    public void initEvents() {
        mIVPpl0Talk.setOnClickListener(this);
        mIVppl0Change.setOnClickListener(this);
        mIVppl0Reserve.setOnClickListener(this);

        mIVPpl1Talk.setOnClickListener(this);
        mIVppl1Change.setOnClickListener(this);
        mIVppl1Reserve.setOnClickListener(this);

        mIVPpl2Talk.setOnClickListener(this);
        mIVppl2Change.setOnClickListener(this);
        mIVppl2Reserve.setOnClickListener(this);

    }

    @Override
    public void init() {



    }


    @Override
    public void onClick(View v) {

       switch (v.getId()){
           case R.id.popup_chat_menu_iv_people0_talk:
               setSelected(isPpl0TalkAllowed);
               mOnChatMenuBtnSelected.onPpl0TalkAllowed(isPpl0TalkAllowed);
               mIVPpl0Talk.setSelected(isPpl0TalkAllowed);
               break;
           case R.id.popup_chat_menu_iv_people1_talk:
               setSelected(isPpl1TalkAllowed);
               mOnChatMenuBtnSelected.onPpl1TalkAllowed(isPpl1TalkAllowed);
               mIVPpl1Talk.setSelected(isPpl1TalkAllowed);
               break;
           case R.id.popup_chat_menu_iv_people2_talk:
               setSelected(isPpl2TalkAllowed);
               mOnChatMenuBtnSelected.onPpl2TalkAllowed(isPpl2TalkAllowed);
               mIVPpl2Talk.setSelected(isPpl2TalkAllowed);
               break;

           case R.id.popup_chat_menu_iv_people0_change:
               setSelected(isPpl0Changed);
               mOnChatMenuBtnSelected.onPpl0Changed(isPpl0Changed);
               mIVppl0Change.setSelected(isPpl0Changed);
               break;
           case R.id.popup_chat_menu_iv_people1_change:
               setSelected(isPpl1Changed);
               mOnChatMenuBtnSelected.onPpl1Changed(isPpl1Changed);
               mIVppl1Change.setSelected(isPpl1Changed);
               break;
           case R.id.popup_chat_menu_iv_people2_change:
               setSelected(isPpl2Changed);
               mOnChatMenuBtnSelected.onPpl2Changed(isPpl2Changed);
               mIVppl2Change.setSelected(isPpl2Changed);
               break;

           case R.id.popup_chat_menu_iv_people0_reserve:
               setSelected(isPpl0Reserved);
               mOnChatMenuBtnSelected.onPpl0Reserved(isPpl0Reserved);
               mIVppl0Reserve.setSelected(isPpl0Reserved);
               break;
           case R.id.popup_chat_menu_iv_people1_reserve:
               setSelected(isPpl1Reserved);
               mOnChatMenuBtnSelected.onPpl1Reserved(isPpl1Reserved);
               mIVppl1Reserve.setSelected(isPpl1Reserved);
               break;
           case R.id.popup_chat_menu_iv_people2_reserve:
               setSelected(isPpl2Reserved);
               mOnChatMenuBtnSelected.onPpl2Reserved(isPpl2Reserved);
               mIVppl2Reserve.setSelected(isPpl2Reserved);
               break;
       }

    }

    public static interface OnChatMenuBtnSelectedListener {

        public void onPpl0TalkAllowed(boolean selected);
        public void onPpl1TalkAllowed(boolean selected);
        public void onPpl2TalkAllowed(boolean selected);
        public void onPpl0Changed(boolean selected);
        public void onPpl1Changed(boolean selected);
        public void onPpl2Changed(boolean selected);
        public void onPpl0Reserved(boolean selected);
        public void onPpl1Reserved(boolean selected);
        public void onPpl2Reserved(boolean selected);

    }

    public void setSelected(Boolean value){

       value=!value;

    }

    class ViewHolder{

        public int viewId;
        public View view;
        public  boolean selected;
    }
}
