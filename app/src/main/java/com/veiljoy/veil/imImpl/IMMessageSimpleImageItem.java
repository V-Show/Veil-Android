package com.veiljoy.veil.imImpl;

import android.content.Context;

import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMMessage;

/**
 * Created by zhongqihong on 15/4/8.
 */
public class IMMessageSimpleImageItem extends IMMessageImageBaseItem {


    public IMMessageSimpleImageItem(IMMessage msg, Context context) {

        super(msg, context);

    }

    @Override
    public void initImages() {
        mVoiceResId = R.mipmap.person0_voice_0;
    }
}
