package com.veiljoy.veil.android.popupwidows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BasePopupWindow;

import java.util.ArrayList;

/**
 * Created by zhongqihong on 15/4/20.
 */
public class ChatPopupWindow extends BasePopupWindow implements View.OnClickListener{



    OnChatMenuBtnClickListener mOnChatMenuBtnSelected;

    ImageButton mIBtn0;
    ImageButton mIBtn1;
    ImageButton mIBtn2;





    public ChatPopupWindow(Context context, int width, int height) {
        super(LayoutInflater.from(context).inflate(
                R.layout.popup_chat_menu, null), width, height);
        setAnimationStyle(R.style.Popup_Animation_Alpha);
        initViews();
        initEvents();
//        init();
    }

    public void setOnChatMenuBtnSelected(OnChatMenuBtnClickListener l){
        this.mOnChatMenuBtnSelected=l;

    }

    @Override
    public void initViews() {

        mIBtn1=(ImageButton)this.findViewById(R.id.popup_chat_menu_ibtn1);

    }

    @Override
    public void initEvents() {
        mIBtn1.setOnClickListener(this);


    }

    @Override
    public void init() {



    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.popup_chat_menu_ibtn1:
            mOnChatMenuBtnSelected.onBtn1Click();
            break;
    }

    }

    public static interface OnChatMenuBtnClickListener {

        public void onBtn0Click( );
        public void onBtn1Click( );
        public void onBtn2Click( );
    }


}
