package com.veiljoy.veil.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {

    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static Context mContext;

    public static void InitPreferenceUtil(Context context, String file) {

        sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
        editor = sp.edit();
    }


    public static void setPasswd(String passwd) {
        editor.putString("passwd", passwd);
        editor.commit();
    }

    public static String getPasswd() {
        return sp.getString("passwd", "");
    }

    public static void setId(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public static String getId() {
        return sp.getString("id", "");
    }

    public static String getName() {
        return sp.getString("name", "");
    }

    public static void setName(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public static String getEmail() {
        return sp.getString("email", "");
    }

    public static void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    // ip
    public static void setIp(String ip) {
        editor.putString("ip", ip);
        editor.commit();
    }

    public static String getIp() {
        return sp.getString("ip", Constants.SERVER_IP);
    }

    public static void setPort(int port) {
        editor.putInt("port", port);
        editor.commit();
    }

    public static int getPort() {
        return sp.getInt("port", Constants.SERVER_PORT);
    }

    public static void setIsStart(boolean isStart) {
        editor.putBoolean("isStart", isStart);
        editor.commit();
    }

    public static boolean getIsStart() {
        return sp.getBoolean("isStart", false);
    }

    public static void setIsFirst(boolean isFirst) {
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public static boolean getisFirst() {
        return sp.getBoolean("isFirst", true);
    }

    public static void setGender(int gender) {
        editor.putInt("gender", gender);
        editor.commit();
    }

    public static int getGender() {
        return sp.getInt("gender", -1);
    }

    public static void setAvatar(String avatar) {
        editor.putString("avatar", avatar);
        editor.commit();
    }

    public static String getAvatar() {
        return sp.getString("avatar", "default_image");
    }


    public static void setPhone(String phone) {
        editor.putString("phone", phone);
        editor.commit();
    }

    public static String getPhone() {
        return sp.getString("phone", "18601709039");
    }

    public static void setAge(int age) {
        editor.putInt("age", age);
        editor.commit();
    }

    public static int getAge() {
        return sp.getInt("age", 18);
    }

    public static void setPhoto(String photo) {
        editor.putString("photo", photo);
        editor.commit();
    }

    public static String getPhoto() {

        return sp.getString("photo", "18601709039");
    }

    public static int getStatus() {
        return sp.getInt("status", 0);
    }

    public static void setStatus(int status) {
        editor.putInt("status", status);
        editor.commit();
    }
    public static void setRoom(String name) {
        editor.putString("room_name", name);
        editor.commit();
    }
    public static String getRoom() {
        return sp.getString("room_name", "null");
    }



}
