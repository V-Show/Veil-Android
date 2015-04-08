package com.veiljoy.veil.im;

/**
 * Created by zhongqihong on 15/4/8.
 */
public class IMMessageVoiceEntity extends IMMessage{

    private int mVoiceTimeRange;

    private String mVoiceFileName;

    public int getmVoiceTimeRange() {
        return mVoiceTimeRange;
    }

    public void setmVoiceTimeRange(int mVoiceTimeRange) {
        this.mVoiceTimeRange = mVoiceTimeRange;
    }

    public String getmVoiceFileName() {
        return mVoiceFileName;
    }

    public void setmVoiceFileName(String mVoiceFileName) {
        this.mVoiceFileName = mVoiceFileName;
    }
}
