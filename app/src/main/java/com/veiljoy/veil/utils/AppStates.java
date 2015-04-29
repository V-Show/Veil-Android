package com.veiljoy.veil.utils;

import android.graphics.Bitmap;

import com.veiljoy.veil.im.IMMessageVoiceEntity;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongqihong on 15/4/15.
 */
public class AppStates {

    public static MultiUserChat multiUserChat;

    public static boolean alreadyLogined=false;

    public static IMMessageVoiceEntity imMessageVoiceEntity;

    public static Map<String,String>avatarMap=new HashMap<>();


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

    public static void putAvatar(String name,String avatarPath){
        avatarMap.put(name,avatarPath);
    }

    public static String getAvatar(String name){
        return avatarMap.get(name);
    }






    public static boolean verifyAccount() {
        //新用户检测
        if (SharePreferenceUtil.getName() == null || SharePreferenceUtil.getPasswd() == null) {
            return false;
        }

        //头像检测
        if (SharePreferenceUtil.getAvatar().equals("default_image")) {
            return false;
        }

        //检测性别
        if (SharePreferenceUtil.getGender() == -1) {
            return false;
        }

        //账号和密码匹配检测
        return true;
    }

    public static boolean verifyStates(){

        //上次没有进入房间
        if(SharePreferenceUtil.getRoom().equals("null")){
            return false;
        }

        //表示在后台运行，直接进入聊天室
       if(SharePreferenceUtil.getStatus()<=Constants.LOGIN_ERROR){
           return false;
        }


        return true;
    }

    public static void setMultiUserChat(MultiUserChat muc){
        multiUserChat=muc;
    }
    public static MultiUserChat getMultiUserChat(){
        return multiUserChat;
    }
}
