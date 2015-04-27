package com.veiljoy.veil.bean;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class UserInfo {

    private String mName;
    private String mPassword;
    private int mGender; // men: 0; women: 1
    private byte[] mAvatar;

    public String getmName() {
        return mName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public int getmGender() {
        return mGender;
    }

    public byte[] getmAvatar() {
        return mAvatar;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }

    public void setmAvatar(byte[] mAvatar) {
        this.mAvatar = mAvatar;
    }
}
