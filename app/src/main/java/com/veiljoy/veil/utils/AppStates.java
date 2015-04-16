package com.veiljoy.veil.utils;

import android.graphics.Bitmap;

import com.veiljoy.veil.im.IMMessageVoiceEntity;

import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Created by zhongqihong on 15/4/15.
 */
public class AppStates {

    public static MultiUserChat multiUserChat;

    public static boolean alreadyLogined=false;

    public static IMMessageVoiceEntity imMessageVoiceEntity;


    public static Bitmap getUserAvatar() {
        return userAvatar;
    }

    public static void setUserAvatar(Bitmap userAvatar) {
        AppStates.userAvatar = userAvatar;
    }

    public static Bitmap userAvatar;



    public static IMMessageVoiceEntity getImMessageVoiceEntity() {
        return imMessageVoiceEntity;
    }

    public static void setImMessageVoiceEntity(IMMessageVoiceEntity imMessageVoiceEntity) {
        AppStates.imMessageVoiceEntity = imMessageVoiceEntity;
    }

    public static boolean isAlreadyLogined() {
        return alreadyLogined;
    }

    public static void setAlreadyLogined(boolean alreadyLogined) {
        AppStates.alreadyLogined = alreadyLogined;
    }







    public static void setMultiUserChat(MultiUserChat muc){
        multiUserChat=muc;
    }
    public static MultiUserChat getMultiUserChat(){
        return multiUserChat;
    }
}
