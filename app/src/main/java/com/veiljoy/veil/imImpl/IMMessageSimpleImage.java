package com.veiljoy.veil.imImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageItem;
import com.veiljoy.veil.utils.PhotoUtils;

/**
 * Created by zhongqihong on 15/4/2.
 */
public class IMMessageSimpleImage extends IMMessageItem {

    protected Bitmap mBitmap;
    protected ImageView mIvImage;


    public IMMessageSimpleImage(IMMessage msg,Context context){

        super(msg,context);

    }

    @Override
    protected void onInitViews() {
        View view = mInflater.inflate(R.layout.message_simple_image, null);
        mLayoutMessageContainer.addView(view);
        mIvImage = (ImageView) view.findViewById(R.id.message_iv_msg_simple_image);
    }

    @Override
    protected void onFillMessage() {

        int voiceResId=R.mipmap.person0_voice_0;
        if(mMsg.getmVoiceTimeRange()>10){
            voiceResId=R.mipmap.person0_voice_2;
        }
        else if(mMsg.getmVoiceTimeRange()>5){
            voiceResId=R.mipmap.person0_voice_1;
        }
        else{
            voiceResId=R.mipmap.person0_voice_0;
        }

        mBitmap= BitmapFactory.decodeResource(mContext.getResources(), voiceResId);

        //mBitmap = PhotoUtils.getBitmapFromFile(mMsg.getmContent());
        mIvImage.setImageBitmap(mBitmap);

    }
}
