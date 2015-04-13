package com.veiljoy.veil.imImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;

/**
 * Created by zhongqihong on 15/4/2.
 */
public class IMMessageVoiceItem extends IMMessageImageBaseItem {

    protected Bitmap mBitmap;
    protected ImageView mIvImage;
    private IMMessageVoiceEntity mVoiceEntity;

    public IMMessageVoiceItem(IMMessage msg, Context context) {
        super(msg, context);

        mVoiceEntity = (IMMessageVoiceEntity) msg;
    }

    @Override
    public void initImages() {

        if (mVoiceEntity.getmVoiceTimeRange() > 10) {
            mVoiceResId = R.mipmap.person0_voice_2;
        } else if (mVoiceEntity.getmVoiceTimeRange() > 5) {
            mVoiceResId = R.mipmap.person0_voice_1;
        } else {
            mVoiceResId = R.mipmap.person0_voice_0;
        }
    }


}
